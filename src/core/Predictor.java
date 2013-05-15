package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.sun.tools.doclets.internal.toolkit.resources.doclets;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Predictor {
	
	// Flag to predict for data with missing "day" value
	// I.E. test data set
	private boolean predictionsForTest = true;
	
	// Training data
	static Instances trainingData;
	
	// Attributes
	static FastVector attributes = null;
	
	// Naive based classifier
	static NaiveBayes classifier = new NaiveBayes();
	//static J48 classifier = new J48();
	
	// Evaluator
	static Evaluation eval = null; 
	
	// Location of training and test data
	private static String trainingInput = "melb.train";
	private static String testInput = "melb.test";

	public static void main(String[] args) {

		
		doClassification();
		System.out.println(trainingData.toSummaryString());
		

		doEvaluation();
		
		// Output to file
	}
	
	private static void doClassification() {
		Classifiers trainingClassifier = new Classifiers();
		
		// Read from training data
		BufferedReader br = null;
		
		try {
			String currentLine;
			br = new BufferedReader(new FileReader(trainingInput));

			// read line by line
			while ((currentLine = br.readLine()) != null) {
				
				// crate instance from string
				trainingClassifier.createInstance(currentLine);
			}
			
			// Classify and learn from the training data
			trainingClassifier.completeTrainingClassifier();
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void doEvaluation() {
		// Read from test data
		BufferedReader br = null;
		
		// evaluate data
		Evaluator evaluator = new Evaluator();
		
		try {
			String currentLine;
			br = new BufferedReader(new FileReader(testInput));

			// read line by line
			while ((currentLine = br.readLine()) != null) {
				
				// add test data to instances
				evaluator.addToTestData(currentLine);

			}
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		System.out.println(evaluator.getEvaluation());
		
		
		// Confusion matrix
		double[][] x = eval.confusionMatrix();
		System.out.println("Mon\tTue\tWed\tThu\tFri\tSat\tSun");
		
		
		for (int i = 0; i < x.length; i++) {
			
			for (int j = 0; j < x[i].length; j++) {
				System.out.print(x[i][j] + "\t");
			}
			
			System.out.println();
		}
		
	}
	
	
}
