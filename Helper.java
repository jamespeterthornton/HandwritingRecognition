package handwritingrecognition;

import java.util.Arrays;

public class Helper {
	
	public static void print2DIntArray(int[][] arrayToPrint) {
		
		for (int[] outerArray : arrayToPrint) {
			for(int innerValue : outerArray) {
				System.out.print(innerValue + " ");
			}
			System.out.println();
		}
	}
	
	public static void print2DDoubleArray(double[][] arrayToPrint) {
		
		System.out.println("Print 2D Double Array.");
		
		for (double[] outerArray : arrayToPrint) {
			for(double innerValue : outerArray) {
				System.out.print(innerValue + " ");
			}
			System.out.println();
		}
	}
	
	public static void print3DDoubleArray(double[][][] arrayToPrint) {
		
		System.out.println("3D Double Array: ");
		
		for (double[][] outerArray : arrayToPrint) {
			for(double[] innerArray : outerArray) {
				for(double value : innerArray) {
					System.out.print(value + " ");	
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public static void printDoubleArray(double[] arrayToPrint) {
		for(double innerValue : arrayToPrint) {
			System.out.print(innerValue + " ");
		}
	}
	
	/**
	 * Adds array1 to array2 and stores the result in array2.
	 * @param array1
	 * @param array2
	 * @return
	 */
	
	public static void add2DDoubleArray(double[][] array1, double[][] array2) {
		for(int i =0; i < array1.length; i++) {
			for(int j=0; j<array1[i].length; j++) {
				array2[i][j] = array1[i][j] + array2[i][j];
			}
		}
	}
	
	public static void add3DDoubleArray(double[][][] array1, double[][][] array2) {
		for(int i =0; i < array1.length; i++) {
			for(int j=0; j<array1[i].length; j++) {
				for (int k=0; k<array1[i][j].length; k++){
					array2[i][j][k] += array1[i][j][k];	
				}
			}
		}
	}
	
	public static double[][][] copy3DArray (double[][][] arrayToCopy) {

		double[][][] copy = new double[arrayToCopy.length][][]; //Arrays.copyOf(net.weights, net.weights.length);
		for(int i=0; i< arrayToCopy.length; i++) {
			copy[i] = new double[arrayToCopy[i].length][];
			for(int j = 0; j<arrayToCopy[i].length; j++) {
				copy[i][j] = Arrays.copyOf(arrayToCopy[i][j], arrayToCopy[i][j].length);
				Arrays.fill(copy[i][j], 0);
			}
		}
		return copy;
	}
	
	public static double[][] copy2DArray (double[][] arrayToCopy) {
		double[][] copy = new double[arrayToCopy.length][];
		for(int i =0; i<arrayToCopy.length; i++){
			copy[i] = Arrays.copyOf(arrayToCopy[i], arrayToCopy[i].length);
			Arrays.fill(copy[i], 0);
		}
		return copy;
		
	}
	
	/**
	 * Divides every element in arrayToMod by divisor.
	 * @param arrayToMod
	 * @param divisor
	 */
	
	public static void divide2DDoubleArray(double[][] arrayToMod, double divisor) {
		for(int i =0; i < arrayToMod.length; i++) {
			for(int j=0; j<arrayToMod[i].length; j++) {
				arrayToMod[i][j] = arrayToMod[i][j]/divisor;
			}
		}
		
	}
}
