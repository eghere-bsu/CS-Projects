package cs321.btree;

import java.io.IOException;

public interface BTreeInterface {
    /**
     *  Insert new element into BTree using key
     *
     * @param newObj TreeObject to be inserted into BTree
     * 
     * @return int 0 if insertion is succesfull, -1 otherwise
     */
    public void insert(TreeObject newObj) throws IOException;

    /**
     * Takes as input a nonfull internal node x and an index i such that x.c_i
     * is a full child of x. The procedure then splits this child in two and adjusts x so that
     * it has an additional child.
     *
     * @param x nonfull internal node
     * @param i index
     * @param y node to split
     */
    public void splitChild(BTreeNode x, int i, BTreeNode y) throws IOException;

    /**
     * If x is not a leaf node, then insert key into the appropriate leaf node in the
     * subtree rooted at internal node x. Otherwise, insert key into node x
     *
     * @param x node to use as root for insertion
     * @param newObj TreeObject to be inserted into BTree
     */
    public void insertNonFull(BTreeNode x, TreeObject newObj) throws IOException;

}
