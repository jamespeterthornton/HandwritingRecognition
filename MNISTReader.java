package handwritingrecognition;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class implements a reader for the MNIST dataset of handwritten digits. The dataset is found
 * at http://yann.lecun.com/exdb/mnist/.
 * 
 * @author Gabe Johnson <johnsogg@cmu.edu>
 */
public class MNISTReader {
	
	
  /**
   * @param args
   *          args[0]: label file; args[1]: data file.
   * @throws IOException
   */
	
  public static void main(String[] args) throws IOException {
	
  }
  
  /**
   * @param args
   *          args[0]: label file; args[1]: data file.
   * @throws IOException
   */
  
  public static ArrayList<double[][]> getMNIST(String[] args) throws IOException {
	  
	    System.out.println("Retrieving MNIST data...");
	    
	    DataInputStream labels = new DataInputStream(new FileInputStream("src/handwritingrecognition/train-labels-idx1-ubyte"));
	    DataInputStream images = new DataInputStream(new FileInputStream("src/handwritingrecognition/train-images-idx3-ubyte"));
	  
	    int magicNumber = labels.readInt();
	    if (magicNumber != 2049) {
	      System.err.println("Label file has wrong magic number: " + magicNumber + " (should be 2049)");
	      System.exit(0);
	    }
	    magicNumber = images.readInt();
	    if (magicNumber != 2051) {
	      System.err.println("Image file has wrong magic number: " + magicNumber + " (should be 2051)");
	      System.exit(0);
	    }
	    
	    int numLabels = labels.readInt();
	    int numImages = images.readInt();
	    int numRows = images.readInt();
	    int numCols = images.readInt();
	    if (numLabels != numImages) {
	      System.err.println("Image file and label file do not contain the same number of entries.");
	      System.err.println("  Label file contains: " + numLabels);
	      System.err.println("  Image file contains: " + numImages);
	      System.exit(0);
	    }

	    long start = System.currentTimeMillis();
	    int numLabelsRead = 0;
	    int numImagesRead = 0;
	    
	    //This is a bit cheap, but I'm doing this quickly atm... 
	    // here I hardcode the size of the result variable
	    // so it matches what we know to be the desired size.
	    
	    ArrayList<double[][]> result = new ArrayList<double[][]>();
	    
	    while (labels.available() > 0 && numLabelsRead < numLabels) {
	    
	      byte label = labels.readByte();
	      numLabelsRead++;
	      double[][] image = new double[numCols+1][numRows];
	      for (int colIdx = 0; colIdx < numCols; colIdx++) {
	        for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
	          image[colIdx][rowIdx] = images.readUnsignedByte()/256.0;
	        }
	      }
	      numImagesRead++;
	      
	      double[] labelArray = new double[1];
	      labelArray[0] = label;
	      image[numCols] = labelArray;
	      
	      result.add(image);
	      
	      // At this point, 'label' and 'image' agree and you can do whatever you like with them.

	      if (numLabelsRead % 10 == 0) {
	          //System.out.print(".");
	      }
	      if ((numLabelsRead % 800) == 0) {
	    	//helper.print2DIntArray(image);
	    	//System.out.println(label);
	        //System.out.print(" " + numLabelsRead + " / " + numLabels);
	        long end = System.currentTimeMillis();
	        long elapsed = end - start;
	        long minutes = elapsed / (1000 * 60);
	        long seconds = (elapsed / 1000) - (minutes * 60);
	        //System.out.println("  " + minutes + " m " + seconds + " s ");
	      }
	    }
	    //System.out.println();
	    long end = System.currentTimeMillis();
	    long elapsed = end - start;
	    long minutes = elapsed / (1000 * 60);
	    long seconds = (elapsed / 1000) - (minutes * 60);
	    //System.out.println("Read " + numLabelsRead + " samples in " + minutes + " m " + seconds + " s ");
	    labels.close();
	    images.close();
	    return result;
  }
}