package cs321.create;

import org.junit.Test;

import static org.junit.Assert.*;

public class SequenceUtilsTest
{
	@Test
	public void longToDNAStringTest() throws Exception
	{
		String result, expected;

		result = SequenceUtils.longToDNAString(0, 2);
		expected = "aa";
		assertEquals(result, expected);

		result = SequenceUtils.longToDNAString(0, 4);
		expected = "aaaa";
		assertEquals(result, expected);

		result = SequenceUtils.longToDNAString(1, 2);
		expected = "ac";
		assertEquals(result, expected);

		result = SequenceUtils.longToDNAString(1, 1);
		expected = "c";
		assertEquals(result, expected);

		result = SequenceUtils.longToDNAString(228, 4);
		expected = "tgca";
		assertEquals(result, expected);

		result = SequenceUtils.longToDNAString(27, 4);
		expected = "acgt";
		assertEquals(result, expected);
		
		result = SequenceUtils.longToDNAString(27, 3);
		expected = "cgt";
		assertEquals(result, expected);
	}

	@Test
	public void DNAStringToLongTest() throws Exception
	{
		long result, expected;	
		
		result = SequenceUtils.DNAStringToLong("AAA");
		expected = 0;
		assertEquals(result, expected);
		
		result = SequenceUtils.DNAStringToLong("A");
		expected = 0;
		assertEquals(result, expected);

		result = SequenceUtils.DNAStringToLong("ACGT");
		expected = 27;
		assertEquals(result, expected);

		result = SequenceUtils.DNAStringToLong("TGCA");
		expected = 228;
		assertEquals(result, expected);
		
		result = SequenceUtils.DNAStringToLong("gtc");
		expected = 45;
		assertEquals(result, expected);
		
		result = SequenceUtils.DNAStringToLong("ag");
		expected = 2;
		assertEquals(result, expected);
	}

	@Test
	public void getComplementTest() throws Exception
	{
		long result, expected;
		
		result = SequenceUtils.getComplement(0, 2);
		expected = 15;
		assertEquals(result, expected);

		result = SequenceUtils.getComplement(0, 4);
		expected = 255;
		assertEquals(result, expected);
		
		result = SequenceUtils.getComplement(1, 1);
		expected = 2;
		assertEquals(result, expected);

		result = SequenceUtils.getComplement(1, 2);
		expected = 14;
		assertEquals(result, expected);

		result = SequenceUtils.getComplement(27, 3);
		expected = 36;
		assertEquals(result, expected);
		
		result = SequenceUtils.getComplement(27, 4);
		expected = 228;
		assertEquals(result, expected);
		
		result = SequenceUtils.getComplement(228, 4);
		expected = 27;
		assertEquals(result, expected);
		
	}

}
