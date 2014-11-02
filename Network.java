package handwritingrecognition;

import java.util.Arrays;
import java.util.Random;

public class Network {

	int numLayers = 0;
	double[][] biases;
	
	public Network(int[] sizes) {
		
		//Initialize the network with a Gaussian distribution (mean 0, variance 1) 
		// of random biases and weights
		
		numLayers = sizes.length;
		int[] internal = Arrays.copyOfRange(sizes, 1, sizes.length);
		
		//Initialize biases
		
		biases = new double[internal.length][];
		int biasIndex = 0;
		Random rand = new Random();
		for(int size : internal) {
			biases[biasIndex] = new double[size];
			for (int i = 0; i < size; i++) {
				biases[biasIndex][i] = rand.nextGaussian();
			}
			biasIndex++;
		}
		
		//Initialize weights (TO-DO)
		
	}
	
	public void printBiases(Network net) {
		for (double[] size: net.biases) {
			System.out.println("Layer:");
			for (double bias : size) {
				System.out.println(bias);
			}
		}
	}
	
	public static void main(String[] args) {
		int[] sizes = new int[]{784, 15, 10};
		Network thisNet = new Network(sizes);
	}
}
