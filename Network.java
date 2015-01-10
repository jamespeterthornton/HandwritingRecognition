import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
	 * weights are between. The third dimension represents neurons from the first of
	 * those two layers, the second index which neuron from the second layer each is
	 * connected to. This reversed indexing simplifies the math involved - 
	 * specifically, it lends itself to matrix multiplication. 
	 */
	double[][][] weights;
	
	public Network(int[] argSizes) {
		//Initialize the network with a Gaussian distribution (mean 0, variance 1) 
		// of random biases and weights
		sizes = argSizes;
		numLayers = sizes.length;
		//Randomly initialize weights and biases
		this.randomInitialization();
	}
	
	public void randomInitialization() {
		int[] internal = Arrays.copyOfRange(sizes, 1, sizes.length);
		//biases
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
		//weights
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
	
	public void printBiases() {
		for (double[] size: biases) {
			System.out.println("Layer:");
			for (double bias : size) {
				System.out.println(bias);
			}
		}
	}
	
	public void printWeights() {
		for (int i = 0; i < numLayers - 1; i++) {
			for (int j = 0; j < sizes[i+1]; j++) {
				System.out.println ("New Neuron");
				for (int k = 0; k < sizes[i]; k++) {
					System.out.println(weights[i][j][k]);
				}
			}
		}
	}
	
	public void SGD(ArrayList<double[][]> trainingData, ArrayList<double[][]> testData, int epochs, 
			int miniBatchSize, double eta) {
		//Now, retrain on the data for each epoch
		for (int epo = 1; epo <= epochs; epo++) {
			//Print the evaluation messages for each epoch
			evaluate(testData, epo);
			//Randomly shuffle the data
			Collections.shuffle(trainingData);			
			//Split the data into k mini batches
			for(int k = 0; k < trainingData.size()/miniBatchSize; k++) {
				//Call update mini batch on each batch
				update_mini_batch(trainingData.subList(k*miniBatchSize, (k+1)*miniBatchSize), eta, miniBatchSize);
			}
		}
	}
	
	/**
	 * A class to hold the cost gradient for all of the biases and weights in the network.
	 * @author james
	 *
	 */
	
	private static class Nabla {
		double[][] nablaB;
		double[][][] nablaW;
		/**
		 * Initializes nablaB and nablaW
		 * @param layerCount the 
		 */
		public Nabla(Network net) {
			nablaB = Helper.copy2DArray(net.biases); 
			nablaW = Helper.copy3DArray(net.weights);
		}
		
		/**
		 * Enables class initialization without the layerCount parameter.
		 * In this case, nablaB and nablaW must be initialized elsewhere.
		 */
		public Nabla() {
		}
		public void add(Nabla nToAdd) {
			Helper.add2DDoubleArray(nToAdd.nablaB, nablaB);
			Helper.add3DDoubleArray(nToAdd.nablaW, nablaW);
		}
		public void print() {
			System.out.println("Print Bias Nablas: ");
			for (double[] size: nablaB) {
				System.out.println("Layer:");
				for (double bias : size) {
					System.out.println(bias);
				}
			}
			System.out.println("Print Weight Nablas: ");
			for (int i = 0; i < nablaW.length; i++) {
				for (int j = 0; j < nablaW[i].length; j++) {
					System.out.println ("New Neuron");
					for (int k = 0; k < nablaW[i][j].length; k++) {
						System.out.println(nablaW[i][j][k]);
					}
				}
			}	
		}
	}
	
	public void update_mini_batch(List<double[][]> miniBatch, double eta, int miniBatchSize) {
		//for each example in the minibatch, use backpropogation to compute the
		//cost gradient for biases and weights
		Nabla total = new Nabla(this);
		for (double[][] example : miniBatch) {
			//average these gradients
			total.add(backpropogate(example));
		}
		//total.print();
		//Update the overall network based on the average of those gradients
		for(int layer = 0; layer < biases.length; layer++) {
			for(int bias = 0; bias < biases[layer].length; bias++) {
				biases[layer][bias] -= total.nablaB[layer][bias]*eta/miniBatchSize;
			}
		}
		for(int layer = 0; layer < weights.length; layer++) {
			for(int neuron = 0; neuron < weights[layer].length; neuron++) {
				for(int weight = 0; weight < weights[layer][neuron].length; weight++) {
					//System.out.println("update weight from: " + weights[layer][neuron][weight]);
					weights[layer][neuron][weight] -= total.nablaW[layer][neuron][weight]*eta/miniBatchSize;	
					//System.out.println("to: " + weights[layer][neuron][weight]);
					//System.out.println("by a difference of: " + total.nablaW[layer][neuron][weight]*eta/miniBatchSize);
				}
			}
		}		
	}
	
	public Nabla backpropogate(double[][] example) {
		double[][] nablaB = Helper.copy2DArray(biases);
		double[][][] nablaW = Helper.copy3DArray(weights);
		//Compute the feedforward result while storing z vectors and activations
		FeedForwardResult feed = feedforward(Arrays.copyOf(example, 28));	
		//Backpropogation
		double[] cdVec = costDerivativeVec(example[28][0], feed.activations[feed.activations.length-1]);
		//calculate error for the last layer
		int last = nablaB.length-1;
		for(int i =0; i<nablaB[last].length; i++) {
			nablaB[last][i] = 
					cdVec[i]*sigmoidPrime(feed.zs[feed.zs.length-1][i]);
		}
		
		for(int j = 0; j < nablaW[last].length; j++) {
			for (int k = 0; k < nablaW[last][j].length; k++) {
				nablaW[last][j][k] = nablaB[last][j]*feed.activations[last][k];
			}
		}
		
		//use backpropogation to calculate nablaB for each layer before it,
		//and nablaW based off of that
		for(int i = 2; i<= nablaB.length;i++) {
			int layer = nablaB.length-i;
			// First create a new double array and store in it the dot product of delta(layer+1) 
			// and the transpose matrix of the weights.
			double[] dot = dot(transpose(weights[layer+1]), nablaB[layer+1]);
			for(int j=0; j<nablaB[layer].length; j++) {
				nablaB[layer][j] = sigmoidPrime(feed.zs[layer+1][j])*dot[j];
			}
			for(int j =0; j < nablaW[layer].length; j++) {
				for (int k = 0; k < nablaW[layer][j].length; k++) {
					nablaW[layer][j][k] = nablaB[layer][j]*feed.activations[layer][k];
				}
			}
		}		
		
		Nabla result = new Nabla(this);
		result.nablaB = nablaB;
		result.nablaW = nablaW;
		//return those two sets of error gradients
		return result;
	}
	/**
	 * Takes a 2D array as parameter and returns the transpose of that array
	 * as a 2D matrix.
	 * 
	 * @param array
	 * @return
	 */
	public static double[][] transpose(double[][] array) {
		double[][] result = new double[array[0].length][];
		for(int inner = 0; inner < array[0].length; inner++) {
			result[inner] = new double[array.length];
			for(int outer = 0; outer < array.length; outer++) {
				result[inner][outer] = array[outer][inner];
			}
		}
		return result;
	}
	
	public static double[] costDerivativeVec(double classification, double[] activation) {
		
		double[] goal = new double[activation.length];
		Arrays.fill(goal, 0);
		goal[(int)classification] = 1;
		
		double[] dCost = new double[goal.length];
		for(int i = 0; i < goal.length; i++) {
			dCost[i] = activation[i] - goal[i];
		}
		return dCost;
	}
		
	private static class FeedForwardResult {
		double[][] activations;
		double[][] zs;
		int classification;
	}
	
	/**
	 * Run the network on an image and return the result.
	 * 
	 * @param image the image for the network to be run on
	 * @return the network's result
	 */
	
	public FeedForwardResult feedforward(double[][] image) {
		//Set activation for the first layer
		double[][] activations = new double[numLayers][];
		double[][] zs = new double[numLayers][];
		double[] theseZs = new double[image.length*image[0].length];
		for (int i = 0; i < image.length; i++) {
			for(int j = 0; j < image[i].length; j++) {
				int k = i*image.length+j;
				theseZs[k] = image[i][j];
			}
		}
		zs[0] = theseZs;
		activations[0] = zs[0];
		double[] product;
		double sum;
		//Compute activation for each successive layer
		for (int i = 0; i < weights.length; i++) {
			//Compute the dot product of the last layer's weights and activations
			//and add it to the next layer's biases. Then, set this as the new activation. 
			product = dot(weights[i], activations[i]);
			zs[i+1] = new double[product.length];
			activations[i+1] = new double[product.length];
			for(int j = 0; j < product.length; j++) {
				sum = product[j] + biases[i][j];
				zs[i+1][j] = sum;
				activations[i+1][j]=sigmoid(sum);
			}
		}
		
		double max = 0;
		int classification = 0;
		for (int k = 0; k < activations[activations.length-1].length; k++) {
			if(activations[activations.length-1][k] > max) {
				max = activations[activations.length-1][k];
				classification = k;
			}
		}
		FeedForwardResult result = new FeedForwardResult();
		result.classification = classification;
		result.zs = zs;
		result.activations = activations;
		return result;
	}
	
	/**
	 * Compute the dot product of a two dimensional matrix and a one dimensional matrix.
	 * This will be used to multiply a matrix of weights by a matrix of the last layer's 
	 * activations to produce a matrix of the weighted inputs.
	 * 
	 * @param matrixA
	 * @param matrixB
	 * @return
	 */
	
	public static double[] dot(double[][] matrixA, double[] matrixB) {
		double[] result = new double[matrixA.length];
		for (int outer = 0; outer < matrixA.length ; outer++) {
			result[outer] = 0;
			for (int inner = 0; inner < matrixA[outer].length; inner++) {
				result[outer] += matrixA[outer][inner]*matrixB[inner];
			}
		}
		return result;
	}
	
	/** 
	 * Compute the sigmoid function for a given number.
	 * 
	 * @param x the number to be plugged into the sigmoid function
	 * @return the sigmoid function's result
	 */
	
	public static double sigmoid(double x) {
		return 1.0/(1.0+Math.exp(-x));
	}
	
	public static double sigmoidPrime(double x) {
		double s = sigmoid(x);
		return s*(1.0-s);
	}
	
	/**
	 * Run the network on the test data provided.
	 * Print the results.
	 * 
	 * @param testData the data to be evaluated for
	 * @param epoch an integer indicating the current epoch
	 */
	
	public void evaluate(ArrayList<double[][]> testData, int epoch) {		
		//Set up ints to hold the results
		double numCorrect = 0;
		int total = testData.size();
		//Iterate through each element of the testData
		DecimalFormat df = new DecimalFormat("#.00");
		for(double[][] data : testData) {	
			//Call feedforward to run the network on this data
			//Increment appropriate variables
			FeedForwardResult thisResult = feedforward(Arrays.copyOf(data, 28));
			if (thisResult.classification == data[28][0]) {
				numCorrect++;
			}
		}
		//Print the epoch number and overall accuracy (numCorrect / total)
		double accuracy = numCorrect/total;
		System.out.print("Num correct: " + numCorrect + " total: " + total);
		System.out.println("Epoch: " + epoch + " Accuracy: " + df.format(accuracy));
	}
	
	/**
	 * Reads in MNIST data... file names are hardcoded for my setup.
	 * Will fix that eventually, but not until after the network is working.
	 * It runs SGD on the retrieved data, which will train and evaluate 
	 * itself along the way.
	 * 
	 * @param args
	 * @throws IOException
	 */
	
	public static void main(String[] args) {		
		int[] sizes = new int[]{784, 15, 10};
		Network thisNet = new Network(sizes);
		String[] files = new String[2];
        files[0] = "train-labels-idx1-ubyte";
        files[1] = "train-images-idx3-ubyte";
        try{
			ArrayList<double[][]> data = MNISTReader.getMNIST(files);
			int dataSize = data.size();
			int split = (int)(0.7*dataSize);
			ArrayList<double[][]> train = new ArrayList<double[][]>(data.subList(0, split));
			ArrayList<double[][]> test = new ArrayList<double[][]>(data.subList(split, dataSize-1));		
			thisNet.SGD(train, test, 30, 10, 3.0);
		} catch (IOException e) {
            
            System.out.println("Exception! E: ");
            System.out.print(e);
		} 
	} 
}
