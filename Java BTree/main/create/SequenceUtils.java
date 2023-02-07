package cs321.create;

/**
 * Utility methods dealing with DNA sequences and its compact representation as long variables.
 */

public class SequenceUtils
{
	public static long DNAStringToLong(String seq)  {
		long key = 0;
		seq = seq.toLowerCase();

		for (int i = 0; i < seq.length(); i++) {
			if (seq.charAt(i) == 'a') { // 00
				if (i == 0) {
					key = 0;
				} else {
					key = key << 2;
					key = key | 0;
				}
			} if (seq.charAt(i) == 'c') { // 01
				if(i == 0) {
					key = 1	;
				} else {
					key = key << 2;
					key = key | 1;
				}
			} if (seq.charAt(i) == 'g') { // 10
				if(i == 0) {
					key = 2;
				} else {
					key = key << 2;
					key = key | 2;
				}
			} if (seq.charAt(i) == 't') { // 11
				if(i == 0) {
					key = 3;
				} else {
					key = key << 2;
					key = key | 3;
				}
			}
		} return key;
	}

	public static String longToDNAString(long sequence, int seqLength) {
		String DNAString = Long.toBinaryString(sequence);
		String returnVal = "";
		int dif = (seqLength*2)-DNAString.length();
		for(int i=0;i<dif; i++) {
			DNAString = "0" + DNAString;
		}

		for (int i = 0; i <= DNAString.length()-2; i+=2) {
			switch (DNAString.substring(i, i+2)) {
				case "00":
					returnVal += "a";
					break;
				case "01":
					returnVal += "c";
					break;
				case "10":
					returnVal += "g";
					break;
				case "11":
					returnVal += "t";
					break;
			}
		}
		return returnVal;
	}

	public static long getComplement(long sequence, int seqLength) {
		String DNAString = longToDNAString(sequence, seqLength);
		char[] charArrayDNA = DNAString.toLowerCase().toCharArray();
		DNAString="";
		for(int i=0; i<charArrayDNA.length;i++) {
			switch (charArrayDNA[i]) {
				case 'a':
					DNAString = DNAString.concat("t");
					break;
				case 'c':
					DNAString = DNAString.concat("g");
					break;
				case 'g':
					DNAString = DNAString.concat("c");
					break;
				case 't':
					DNAString = DNAString.concat("a");
					break;
				default:
					break;
			}
		}
		String returnVal = "";
		for (int i = 0; i < DNAString.length(); i++) {
			if (DNAString.charAt(i) == 'a') {
				returnVal += 'a';
			}
			if (DNAString.charAt(i) == 'c') {
				returnVal += 'a';
			}
			if (DNAString.charAt(i) == 'a') {
				returnVal += 'a';
			}
			if (DNAString.charAt(i) == 'a') {
				returnVal += 'a';
			}

		}
		return DNAStringToLong(DNAString);
	}

	public static String getComplementString(String sequence) {
		char[] charArrayDNA = sequence.toLowerCase().toCharArray();
		String DNAString="";
		for(int i=0; i<charArrayDNA.length;i++) {
			switch (charArrayDNA[i]) {
				case 'a':
					DNAString = DNAString.concat("t");
					break;
				case 'c':
					DNAString = DNAString.concat("g");
					break;
				case 'g':
					DNAString = DNAString.concat("c");
					break;
				case 't':
					DNAString = DNAString.concat("a");
					break;
				default:
					break;
			}
		}
		return DNAString;
	}
}