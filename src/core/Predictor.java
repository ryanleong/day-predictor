package core;

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
	static classifierType cType = classifierType.DECISION_TREE;
	
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
		
		// evaluate data
		Evaluator evaluator = new Evaluator(testInput);

		
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
