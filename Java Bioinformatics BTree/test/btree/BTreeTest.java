package cs321.btree;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import cs321.common.Cache;

/**
 * The tester class for BTree.java.
 */
public class BTreeTest {
	// HINT:
	// instead of checking all intermediate states of constructing a tree
	// you can check the final state of the tree and
	// assert that the constructed tree has the expected number of nodes and
	// assert that some (or all) of the nodes have the expected values
	
	@Test
	public void btreeDegree4Test() {
		// //TODO instantiate and populate a bTree object
		// int expectedNumberOfNodes = TBD;
		//
		// // it is expected that these nodes values will appear in the tree when
		// // using a level traversal (i.e., root, then level 1 from left to right, then
		// // level 2 from left to right, etc.)
		// String[] expectedNodesContent = new String[]{
		// "TBD, TBD", //root content
		// "TBD", //first child of root content
		// "TBD, TBD, TBD", //second child of root content
		// };
		//
		// assertEquals(expectedNumberOfNodes, bTree.getNumberOfNodes());
		// for (int indexNode = 0; indexNode < expectedNumberOfNodes; indexNode++)
		// {
		// // root has indexNode=0,
		// // first child of root has indexNode=1,
		// // second child of root has indexNode=2, and so on.
		// assertEquals(expectedNodesContent[indexNode],
		// bTree.getArrayOfNodeContentsForNodeIndex(indexNode).toString());
		// }

	}

	@Test
	public void fillRootTest() throws Exception {
		BTree test = new BTree(2, "fillRootTest", 0);
		TreeObject a = new TreeObject(2, 0);
		TreeObject b = new TreeObject(1, 0);
		TreeObject c = new TreeObject(3, 0);
		test.insert(a);
		test.insert(b);
		test.insert(c);

		System.out.println(test.getRoot().toString());
		assert (test.getRoot().toString().equals("1 2 3"));
		test.finishUp();
	}

	@Test
	public void splitRootTest() throws Exception {
		BTree test = new BTree(2, "splitRootTest", 0);
		TreeObject a = new TreeObject(2, 0);
		TreeObject b = new TreeObject(1, 0);
		TreeObject c = new TreeObject(3, 0);
		TreeObject d = new TreeObject(4, 0);
		test.insert(a);
		test.insert(b);
		test.insert(c);
		test.insert(d);

		assert (test.getRoot().toString().equals("2"));
		assert (test.getNodeAt(2).toString().equals("1"));
		assert (test.getNodeAt(3).toString().equals("3 4"));
		test.finishUp();
	}

	@Test
	public void splitLeafTest() throws Exception {
		BTree test = new BTree(2, "splitLeafTest", 0);
		TreeObject a = new TreeObject(2, 0);
		TreeObject b = new TreeObject(1, 0);
		TreeObject c = new TreeObject(3, 0);
		TreeObject d = new TreeObject(4, 0);
		TreeObject e = new TreeObject(5, 0);
		TreeObject f = new TreeObject(6, 0);
		test.insert(a);
		test.insert(b);
		test.insert(c);
		test.insert(d);
		test.insert(e);
		test.insert(f);

		assert (test.getRoot().toString().equals("2 4"));
		assert (test.getNodeAt(2).toString().equals("1"));
		assert (test.getNodeAt(3).toString().equals("3"));
		assert (test.getNodeAt(4).toString().equals("5 6"));
		test.finishUp();
	}

	@Test
	public void secSplitLeafTest() throws Exception {
		Cache cache = new Cache(4);
		BTree test = new BTree(2, "splitLeafTest", 0);
		TreeObject a = new TreeObject(2, 0);
		TreeObject b = new TreeObject(1, 0);
		TreeObject c = new TreeObject(3, 0);
		TreeObject d = new TreeObject(4, 0);
		TreeObject e = new TreeObject(5, 0);
		TreeObject f = new TreeObject(6, 0);
		TreeObject g = new TreeObject(7, 0);
		TreeObject h = new TreeObject(8, 0);
		test.insert(a);
		test.insert(b);
		test.insert(c);
		test.insert(d);
		test.insert(e);
		test.insert(f);
		test.insert(g);
		test.insert(h);

		assert (test.getRoot().toString().equals("2 4 6"));
		assert (test.getNodeAt(2).toString().equals("1"));
		assert (test.getNodeAt(3).toString().equals("3"));
		assert (test.getNodeAt(4).toString().equals("5"));
		assert (test.getNodeAt(5).toString().equals("7 8"));
		test.finishUp();
	}

}
