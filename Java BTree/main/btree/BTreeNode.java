package cs321.btree;

//import cs321.create.SequenceUtils;

import java.nio.ByteBuffer;

/**
 * This class represents a Node in the BTree. A node can have multiple 
 * TreeObjects inside of it and are stored in the BTree in nondecreasing
 * order. The BTree will be comprised of many BTreeNodes.
 */
public class BTreeNode{

    private int t; //degree
    public TreeObject[] keys;
    private long[] children;
    private int numKeys;
    private boolean isLeaf;
    private long location; //location of this node in file

    public BTreeNode(int t) {
        this.t = t;
        this.keys = new TreeObject[(2*t)-1+1];
        this.children = new long[(2*t)+1];
        this.numKeys = 0;
    }

    public BTreeNode(ByteBuffer buff, int deg, long address) {

        this.t = deg;
        this.keys = new TreeObject[(2*t)-1+1];
        this.children = new long[(2*t)+1];

        if (address == 0) { return; }
//        TreeObject tmp;
        this.setNumKeys(buff.getInt());

        for (int i = 1; i <= numKeys; i++) {
            this.setKey(i, new TreeObject(buff.getLong(), buff.getInt()));
        }

        byte flag = buff.get();
        boolean leaf = false;
        if (flag == 1) {
            leaf = true;
        }

        for (int i = 1; i <= numKeys+1; i++) {
            this.setChild(i, buff.getLong());
        }

        this.isLeaf = leaf;
        this.location = address;
    }

    public boolean isLeaf() { return this.isLeaf; }

    public void setIsLeaf(boolean isLeaf) { this.isLeaf = isLeaf; }

    public TreeObject getKey(int i) { return keys[i]; }

    public void setKey(int i, TreeObject newKey) { keys[i] = newKey; }

    public long getLocation() { return this.location; }

    public void setLocation(long newVal) { this.location = newVal; }

    public long getChild(int i) { return children[i]; }

    public void setChild(int i, long newVal) { children[i] = newVal; }

    public int getNumKeys() { return this.numKeys; }

    public boolean isFull() { return this.numKeys == keys.length; }

	public void setNumKeys(int nkeys) {
		this.numKeys = nkeys;
	}

    public String toString() {
//        SequenceUtils convert = new SequenceUtils();
        String returnVal = "";
        for (int i = 1; i <= numKeys; i++) {
            returnVal += keys[i].getSubstring();
            if (i != numKeys) {
                returnVal += " ";
            }
        }
        return returnVal;
    }
}
