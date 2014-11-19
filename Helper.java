package handwritingrecognition;

public class Helper {

	void print2DIntArray(int[][] arrayToPrint) {
		
		for (int[] outerArray : arrayToPrint) {
			for(int innerValue : outerArray) {
				System.out.print(innerValue);
			}
			System.out.println();
		}
	}
}
