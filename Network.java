package handwritingrecognition;

import java.util.Arrays;
import java.util.Random;

public class Network {

	int numLayers = 0;
	
	int[] sizes;
	
	/** 
	 * Biases is a two-dimensional array, with the first dimension indicating
	 * the layer we are referring to and the second the neuron within that layer 
	 */
	
	double[][] biases;
	
	/** 
	 * Weights has three dimensions. The first indicates which two layers these 
	 * weights are between. The third index represents a neuron from the first of
	 * those two layers, the second index which neuron from the second layer it is
	 * connected to (this reversed indexing simplifies the math involved). 
	 */
	double[][][] weights;
	
	public Network(int[] argSizes) {
		
		//Initialize the network with a Gaussian distribution (mean 0, variance 1) 
		// of random biases and weights
		sizes = argSizes;
		numLayers = sizes.length;
		int[] internal = Arrays.copyOfRange(sizes, 1, sizes.length);
		
		//Randomly initialize biases
		
		biases = new double[numLayers - 1][];
		weights = new double[numLayers - 1][][];
		int biasIndex = 0;
		Random rand = new Random();
		for(int size : internal) {
			biases[biasIndex] = new double[size];
			for (int i = 0; i < size; i++) {
				biases[biasIndex][i] = rand.nextGaussian();
			}
			biasIndex++;
		}
		
		//Randomly initialize weights
		
		for (int i = 0; i < numLayers - 1; i++) {
			weights[i] = new double[sizes[i+1]][];
			for(int j = 0; j < sizes[i+1]; j++) {
				weights[i][j] = new double[sizes[i]];
				for (int k = 0; k < sizes[i]; k++) {
					weights[i][j][k] = rand.nextGaussian();
				}
			}
		}	
	}
	
	public static void printBiases(Network net) {
		for (double[] size: net.biases) {
			System.out.println("Layer:");
			for (double bias : size) {
				System.out.println(bias);
			}
		}
	}
	
	public static void printWeights(Network net) {
		//Print weights between the second and third layers and from there on
		for (int i = 1; i < net.numLayers - 1; i++) {
			for (int j = 0; j < net.sizes[i+1]; j++) {
				System.out.println ("New Neuron");
				for (int k = 0; k < net.sizes[i]; k++) {
					System.out.println(net.weights[i][j][k]);
				}
			}
		}
	}
	
	public static void SGD(int[][][] trainingData, int epochs, 
			int miniBatchSize, double eta, int[][][]...test_data) {
		
		
		
		
		
	}
	
	public static void update_mini_batch(int[][][] miniBatch, double eta) {
		
		
		
	
	}
	
	//Functions to add : backProp, evaluate, costDerivative, sigmoid, sigmoidPrime
	
	public static void main(String[] args) {
		int[] sizes = new int[]{784, 15, 10};
		Network thisNet = new Network(sizes);
		printWeights(thisNet);
	}
}
