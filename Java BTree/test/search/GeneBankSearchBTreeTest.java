package cs321.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import cs321.btree.BTree;
import cs321.btree.TreeObject;
import cs321.create.SequenceUtils;

public class GeneBankSearchBTreeTest {
//	private static String[] args;
//	private GeneBankSearchBTreeArguments expectedConfiguration;
//	private GeneBankSearchBTreeArguments actualConfiguration;
	
////	@Test (expected = Exception.class)
//	
//	@Rule
//	public ExpectedException thrown = ExpectedException.none();	
//	
//	@Test
//	public void parse5CorrectArgumentsTest() throws Exception {
//		args = new String[5];
//		args[0] = "--cache=2";
//		args[1] = "--degree=-10";
//		args[2] = "--btreefile=test0.gbk.btree.data.5.0";
//		args[3] = "--length=5";
//		args[4] = "--queryfile=results/query-results/query5";
//		
////		GeneBankSearchBTreeArguments gba = GeneBankSearchBTree.parseArguments(args);
////		parseArgumentsAndHandleExceptions(args);
//		GeneBankSearchBTree.parseArguments(args);
//		
////		thrown.expect(Exception.class);
////		thrown.expectMessage("Invalid degree value");
//		
////		thrown.expect(Exception.class);
////		thrown.expectMessage("Invalid degree value");
//
////		expectedConfiguration = new GeneBankSearchBTreeArguments(false, 0, "test0.gbk.btree.data.5.0", 5,
////				"results/query-results/query5");
////		GeneBankSearchBTree test = new GeneBankSearchBTree();
////		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
////		actualConfiguration = test.parseArgumentsAndHandleExceptions(args);
//	}

	/*
	 * Method testing frequencies resulting from searching both, DNAString and its complement, 
	 * been equal to the ones in results/query-results/...
	 * */

	@Test
	public void searchTest() throws Exception {
		String btfiletest = "results/query-results/query2-test0.gbk.out";
		int subsequenceLength = 2;
		BTree bt = new BTree("test0.gbk.btree.data.2.2", 0);
		BufferedReader bf;
		ArrayList<Integer> atotal = new ArrayList<Integer>();
		ArrayList<Integer> afreqs = new ArrayList<Integer>();
		String[] split;
		try {
			bf = new BufferedReader(new FileReader(btfiletest));
			String line;
			while((line=bf.readLine())!=null) {
				line = line.toLowerCase();
				split = line.split(" ");
				TreeObject temp = new TreeObject(SequenceUtils.DNAStringToLong(line));
				TreeObject temp2 = new TreeObject(SequenceUtils.getComplement(SequenceUtils.DNAStringToLong(line), subsequenceLength));
				int total = bt.searchTree(bt.getRoot(), temp).getFrequency() + bt.searchTree(bt.getRoot(), temp2).getFrequency();
				atotal.add(total);
				afreqs.add(Integer.parseInt(split[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for(int i=0;i<atotal.size();i++) {
			assertEquals(atotal.get(i), afreqs.get(i));
		}
	}

	@Test
	public void searchCacheTest() throws Exception {
		
		BTree bt = new BTree("test0.gbk.btree.data.2.2", 101);
		assertTrue(bt.isUsingCache());

	}

}
