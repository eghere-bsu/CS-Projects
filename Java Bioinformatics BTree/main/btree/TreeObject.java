package cs321.btree;

/*
 * The TreeObject class represents a key that is stored in a BTreeNode.
 * This is the smallest component in the BTree; these files make up
 * the largest part of the data structure. For the Gene Bank, DNA 
 * substrings will be stored in TreeObjects > stored in BTreeNodes > in 
 * the BTree data structure.
 */
public class TreeObject{

	private long substring;
	private int frequency;

	public TreeObject(long substring, int frequency) {
		this.substring = substring;
		this.frequency = frequency;
	}
	
	public TreeObject(long substring) {
		this.substring = substring;
		this.frequency = 0;
	}

	public long getSubstring() { return this.substring; }

	public void setSubstring(long newVal) { this.substring = newVal; }

	public int getFrequency() { return this.frequency; }

	public void increaseFrequency() { this.frequency++; }

	public int compareTo(TreeObject obj) {
		if (substring < obj.substring) {
			return -1;
		}
		if (substring > obj.substring) {
			return 1;
		}
		return 0;
	}

	public String toString() {
		return this.substring + " " + this.frequency;
	}

}
