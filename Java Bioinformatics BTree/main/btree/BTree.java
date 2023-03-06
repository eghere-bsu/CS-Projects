package cs321.btree;

import cs321.common.Cache;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import static cs321.create.SequenceUtils.longToDNAString;

/**
 * This class represents a BTree. The BTree will store sequences in the
 * DNA file in nondecreasing order in a manner which will allow for 
 * quick in-order traversal and disk access.
 */
public class BTree implements BTreeInterface
{
    private int degree;
    private long nextAddress;
    private RandomAccessFile raf;
    private final int nodeSize;
    private BTreeNode root;
    private ByteBuffer buff;
    private boolean usingCache;
    private String btreeFileName;
    private Cache cache;

    public BTree(int degree, String fileName, int cacheSize) throws Exception {
        this.degree = (degree != 0) ? degree : 101;
        if (cacheSize > 100 && cacheSize < 500) {
            Cache cache = new Cache(cacheSize);
            this.cache = cache;
            this.usingCache = true;
        }
        raf = new RandomAccessFile(fileName, "rw");
        nodeSize = ((12 * (2*this.degree-1))+1) + (8*(2*this.degree+1)) + 16; //size of node for disk write
        nextAddress = 12; //next address for disk write
        BTreeNode x = new BTreeNode(this.degree);
        x.setLocation(nextAddress);
        x.setNumKeys(0);
        x.setIsLeaf(true);
        root = x;
        nextAddress += nodeSize;
        raf.writeInt(this.degree);
        raf.writeLong(x.getLocation());
        buff = ByteBuffer.allocate(nodeSize);
    }
    public BTree(String fileName, int cacheSize) throws Exception { //for GeneBankSearch
        if (cacheSize > 100 && cacheSize < 500) {
            Cache cache = new Cache(cacheSize);
            this.cache = cache;
            this.usingCache = true;
        }
        raf = new RandomAccessFile(fileName, "r");
        this.degree = raf.readInt();
        nodeSize = ((12 * (2*this.degree-1))+1) + (8*(2*this.degree+1)) + 16; //size of node for disk write
        buff = ByteBuffer.allocate(nodeSize);
        this.root = diskRead(raf.readLong());
        this.btreeFileName = fileName;
    }

    /**
     * Inserts a node into the Btree.
     */
    @Override
    public void insert(TreeObject newObj) throws IOException {
        BTreeNode r = root;
        if(root.getNumKeys() == (2*degree-1)) {
            BTreeNode s = new BTreeNode(degree);
            s.setLocation(nextAddress);
            nextAddress+=nodeSize;
            this.root = s;
            s.setNumKeys(0);
            s.setIsLeaf(false);
            s.setChild(1, r.getLocation());
            splitChild(s, 1, r);
            insertNonFull(s, newObj);
        }else {
            insertNonFull(r, newObj);
        }
    }

    /**
     * Splits a child node in the BTree.
     */
    @Override
    public void splitChild(BTreeNode x, int i, BTreeNode y) throws IOException {
        BTreeNode z = new BTreeNode(degree);
        z.setLocation(nextAddress);
        nextAddress += nodeSize;

        z.setIsLeaf(y.isLeaf());
        z.setNumKeys(degree-1);

        for (int j = 1; j <= degree-1; j++) {
            z.setKey(j, y.getKey(j+degree));
        }
        if (!y.isLeaf()) {
            for (int j = 1; j <= degree; j++) {
                z.setChild(j, y.getChild(j+degree));
            }
        }
        y.setNumKeys(degree-1);

        for (int j = x.getNumKeys()+1; j >= i+1; j--) {
            x.setChild(j+1, x.getChild(j));
        }
        x.setChild(i+1, z.getLocation());

        for (int j = x.getNumKeys(); j >= i; j--) {
            x.setKey(j+1, x.getKey(j));
        }
        x.setKey(i, y.getKey(degree));
        x.setNumKeys(x.getNumKeys()+1);

        cacheWrite(y);
        cacheWrite(z);
        cacheWrite(x);
    }

    /**
     * Inserts a node if it is nonfull. 
     */
    @Override
    public void insertNonFull(BTreeNode x, TreeObject newObj) throws IOException {
        int i = x.getNumKeys();
        if(x.isLeaf()){

            //Check for duplicate
            for (int j = 1; j <= x.getNumKeys(); j++) {
                if (x.getKey(j).getSubstring() == newObj.getSubstring()) {
                    x.getKey(j).increaseFrequency();
                    cacheWrite(x);
                    return;
                }
            }

            while(i >= 1 && newObj.getSubstring() < x.getKey(i).getSubstring()) {
                x.setKey(i+1, x.getKey(i));
                i--;
            }
            x.setKey(i+1, newObj);
            x.setNumKeys(x.getNumKeys()+1);

            cacheWrite(x);

        } else {
            while(i > 1 && newObj.getSubstring() < x.getKey(i).getSubstring()) {
                i--;
            }
            if (i >= 0 && x.getKey(i).getSubstring() == newObj.getSubstring()) {
                x.getKey(i).increaseFrequency();

                cacheWrite(x);

                return;
            }
            if (newObj.getSubstring() > x.getKey(i).getSubstring()) {
                i++;
            }

            BTreeNode node;
            node = cacheRead(x.getChild(i));

            if(node.getNumKeys() == 2*degree-1) {
                splitChild(x, i, node);
                if(newObj.getSubstring() > x.getKey(i).getSubstring()) {
                    i++;
                } else if (newObj.getSubstring() == x.getKey(i).getSubstring()) {
                    x.getKey(i).increaseFrequency();
                    cacheWrite(x);
                    return;
                }
                node = cacheRead(x.getChild(i));
            }
            insertNonFull(node, newObj);
        }
    }

    /**
     * Searches the BTree for a node.
     */
    public TreeObject searchTree(BTreeNode x, TreeObject obj) throws IOException { //returns location of obj
        //obj is the key to compare (from query)
        int i = 1;
        //recorrer todas las keys del nodo
        while (i <= x.getNumKeys() && obj.getSubstring() > x.getKey(i).getSubstring()) {
            i++;
        }
        //si son iguales devuelve i
        if (i <= x.getNumKeys() && obj.getSubstring() == x.getKey(i).getSubstring()) {
            if (usingCache) {
                cache.addNode(x);
            }
            return x.getKey(i);
        } else if (x.isLeaf()) {
            return new TreeObject(obj.getSubstring(), 0);
        } else {
            return searchTree(cacheRead(x.getChild(i)), obj);
        }
    }

    /**
     * Reads an address from the disk.
     * @param address from disk
     * @return the BTreeNode at the address
     * @throws IOException if file is invalid.
     */
    public BTreeNode diskRead(long address) throws IOException {
        BTreeNode returnVal;
        raf.seek(address);
        buff.clear();
        raf.read(buff.array());
        returnVal = new BTreeNode(buff, degree, address);

        return returnVal;
    }

    /**
     * Writes a node to the disk.
     */
    public void diskWrite(BTreeNode x) throws IOException {
        raf.seek(x.getLocation());
        buff.clear();

        buff.putInt(x.getNumKeys());
        for (int i = 1; i <= x.getNumKeys(); i++) {
            buff.putLong(x.getKey(i).getSubstring());
            buff.putInt(x.getKey(i).getFrequency());
        }

        if (x.isLeaf()) {
            buff.put((byte)1);
        } else {
            buff.put((byte)0);
        }

        for (int i = 1; i <= x.getNumKeys()+1; i++) {
            buff.putLong(x.getChild(i));
        }
        raf.write(buff.array());
    }

    public BTreeNode cacheRead(long address) throws IOException {
        if (usingCache) {
            BTreeNode returnVal = cache.getNode(address);
            if (returnVal == null) {
                returnVal = diskRead(address);
                cacheWrite(returnVal);
            }
            return returnVal;
        } else {
            return diskRead(address);
        }
    }

    public void cacheWrite(BTreeNode node) throws IOException {
        if (usingCache) {
            if (cache.isFull()) {
                diskWrite(cache.removeLast()); {
                }
            }
            cache.addNode(node);
        } else {
            diskWrite(node);
        }
    }

    /**
     * Gets a node at a specific key.
     * @param i the key
     * @return the BTreeNode in question
     * @throws IOException if file is invalid.
     */
    public BTreeNode getNodeAt(int i) throws IOException {
        if (i < 1) {
            return null;
        }
        Queue<BTreeNode> q = new LinkedList<>();

        int j = 0;
        q.add(root);

        BTreeNode current = null;
        while (j < i) {
            current = q.remove();
            j++;
            if (!current.isLeaf()) {
                for (int k = 1; k <= current.getNumKeys()+1; k++) {
                    BTreeNode child = cacheRead(current.getChild(k));
                    q.add(child);
                }
            }
        }

        return current;
    }

    /**
     * Creates a dump file of all the occurring patterns in a BTree 
     * alongside the frequency of that pattern in the BTree.
     */
    public void dump(String fileName, int seqL) throws IOException {
        FileWriter write = new FileWriter(fileName);
        dumpTree(root, write, seqL);
        write.close();
    }

    /**
     * Finds the frequency of patterns for use in the dump file.
     * @param node
     * @param dumpWriter
     * @param seqL
     * @throws IOException
     */
    public void dumpTree(BTreeNode node, FileWriter dumpWriter, int seqL) throws IOException { //In Order Traversal, except writes to dump file
        if (node.isLeaf()) {
            for (int i = 1; i <= node.getNumKeys(); i++) {
                dumpWriter.write(longToDNAString(node.getKey(i).getSubstring(), seqL) + " " + node.getKey(i).getFrequency() + "\n");
            }
        } else {
            for (int j = 1; j <= node.getNumKeys(); j++) {
                dumpTree(cacheRead(node.getChild(j)), dumpWriter, seqL);
                dumpWriter.write(longToDNAString(node.getKey(j).getSubstring(), seqL) + " " + node.getKey(j).getFrequency() + "\n");
            }
            BTreeNode n = cacheRead(node.getChild(node.getNumKeys()+1));
            dumpTree(n, dumpWriter, seqL);
        }
    }

    public LinkedList<TreeObject> inOrderTraverse(BTreeNode root, int seql) throws IOException {
        LinkedList<TreeObject> returnVal = new LinkedList<>();
        return traverse(returnVal, root, seql);
    }
    public LinkedList<TreeObject> traverse(LinkedList<TreeObject> list, BTreeNode node, int seqL) throws IOException {
        if (node.isLeaf()) {
            for (int i = 1; i <= node.getNumKeys(); i++) {
                list.add(node.getKey(i));
            }
        } else {
            for (int j = 1; j <= node.getNumKeys(); j++) {
                traverse(list, cacheRead(node.getChild(j)), seqL);
                list.add(node.getKey(j));
            }
            BTreeNode n = cacheRead(node.getChild(node.getNumKeys()+1));
            traverse(list, n, seqL);
        }
        return list;
    }
    /**
     * Gets the root of a BTree.
     * @return
     * @throws IOException
     */
    public BTreeNode getRoot() throws IOException {
        return root;
    }

    /**
     * Writes a node from the cache.
     * @throws IOException
     */
    public void writeFromCache() throws IOException {
        if (cache != null) {
            for (int i = 0; i < cache.size(); i++) {
                diskWrite(cache.removeLast());
            }
        }
    }

    /**
     * Finalizes use of the Random Access File.
     * @throws IOException
     */
    public void finishUp() throws IOException {
        raf.seek(0);
        raf.writeInt(degree);
        raf.writeLong(root.getLocation());
        raf.close();
    }

    /*
     * @return the RandomAccessFile for testing purposes.
     */
    public RandomAccessFile getRAF() {
        return this.raf;
    }

	public boolean isUsingCache() {
		return usingCache;
	}

}
