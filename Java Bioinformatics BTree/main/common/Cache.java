package cs321.common;

import cs321.btree.BTreeNode;

import java.util.LinkedList;

/**
 * This class represents the cache that may be used in both 
 * GeneBankCreateBTree and GeneBankSearchBTree.
 */
public class Cache {
    private LinkedList<BTreeNode> cache;
    int limit;

    /**
     * Constructor method for the cache.
     */
    public Cache(int maxSize) {
        cache = new LinkedList<>();
        this.limit = maxSize;
    }

    /**
     * Gets the node from the cache.
     * @param address
     * @return
     */
    public BTreeNode getNode(long address) {
        BTreeNode target = null;
        for (BTreeNode node : cache) {
            if (node != null && node.getLocation() == address) {
                target = node;
                break;
            }
        }
        cache.remove(target);
        cache.addFirst(target);

        return target;
    }

    /**
     * Adds a node to the cache.
     * @param element
     */
    public void addNode(BTreeNode element) {
        if (cache.contains(element)) {
            cache.remove(element);
        }

        cache.addFirst(element);
    }

    /** 
     * Removes first occurrence of given object from linked list. 
     */ 
    public BTreeNode removeLast() { return cache.removeLast(); }

    /**
     * @return the size of the cache.
     */
    public int size() { return cache.size(); }

    /**
     * @return true if the cache is full.
     */
    public boolean isFull() {
        return size() == limit;
    }

    /**
     * Removes all elements from the cache.
     */
    public void clearCache() {
        cache.clear();
    }

    public void remove(BTreeNode node) {
        cache.remove(node);
    }
}
