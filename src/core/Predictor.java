package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.FastVector;
import weka.core.Instances;

public class Predictor {
	
	// Training data
	static Instances trainingData;
	
	// Attributes
	static FastVector attributes = null;
	
	// Naive based classifier
	static NaiveBayes naiveBayesClassifier = new NaiveBayes();
	
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
		Classifier trainingClassifier = new Classifier();
		
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
		
		try {
			String currentLine;
			br = new BufferedReader(new FileReader(testInput));

			// read line by line
			while ((currentLine = br.readLine()) != null) {
				
				// evaluate data
				Evaluator evaluator = new Evaluator(currentLine);
				
				//double[] evaluatedData = evaluator.getFDistribution();
				
				
				System.out.println(evaluator.getProbablyDay());
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
		
	}
	
}
