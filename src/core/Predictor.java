package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.FastVector;
import weka.core.Instances;

public class Predictor {

	// Flag to predict for data with missing "day" value
	// I.E. test data set
	private boolean predictionsForTest = true;

	// Training data
	static Instances trainingData;

	// Attributes
	static FastVector attributes = null;

	// Classifier
	static Classifier classifier = null;

	// Classifier Type
	static classifierType cType = classifierType.NAIVEBAYES;

	// Evaluator
	static Evaluation eval = null;

	// Location of training and test data
	private static String trainingInput = "melb.train";
	private static String testInput = "melb.test";

	public static enum classifierType {
		NAIVEBAYES, DECISION_TREE
	}

	public static void main(String[] args) {

		doClassification();
		System.out.println(trainingData.toSummaryString());

		doEvaluation();

		// Output to file
	}

	private static void doClassification() {
		Classifiers trainingClassifier = new Classifiers(trainingInput);

		trainingClassifier.buildTrainingClassifier();
	}

	private static void doEvaluation() {
		Evaluator evaluator = new Evaluator();
		
		// TODO put attributes from training data into test instance
//		evaluator.doPredictionForTest(testInput);
//		
//		System.exit(0);
		
		
		
		//System.out.println(currInst.attribute(3).name());
		
		///////////////////////////////////////////////
		// Through weka read
		

		evaluator.wekaRead(testInput);
		

		System.out.println("\n============================");
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

	private static void toFile() {

		BufferedReader br = null;
		 
		try {
 
			String currentLine;
			boolean startReading = false;
			
			br = new BufferedReader(new FileReader(testInput));
 
			while ((currentLine = br.readLine()) != null) {
				
				if (!startReading) {
					if (currentLine.equals("@data")) {
						startReading = true;
					}
					
					String[] lineParts = currentLine.split(",");
					lineParts[lineParts.length - 1] = "";
					
				}
				
				
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 

	}

}
