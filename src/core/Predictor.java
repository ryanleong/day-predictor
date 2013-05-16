package core;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Predictor {

	// Flag to predict for data with missing "day" value
	// I.E. test data set
	private static boolean predictionsForTest = true;
	
	// Flag to enable use of comparison between different classifiers 
	static boolean multipleClassifierComparision = true;
	
	// Location of training and test data
	private static String trainingInput = "datasets/bris.train";
	private static String testInput = "datasets/bris.test";

	// Classifier
	static Classifier naiveBayesClassifier = null;
	static Classifier decisionTreeClassifier = null;
	static Classifier supportVectorMachineClassifier = null;

	// Classifier Type
	static classifierType cType = classifierType.NAIVEBAYES;

	// Training data
	static Instances trainingData;
	
	// Evaluator
	static Evaluation eval = null;

	// Enum of classifier type
	public static enum classifierType {
		NAIVEBAYES, DECISION_TREE, SUPPORT_VECTOR_MACHINE
	}

	public static void main(String[] args) {

		doClassification();
		System.out.println(trainingData.toSummaryString());

		doEvaluation();

	}

	private static void doClassification() {
		Classifiers trainingClassifier = new Classifiers(trainingInput);

		trainingClassifier.buildTrainingClassifier();
	}

	private static void doEvaluation() {
		Evaluator evaluator = new Evaluator();
		
		if (predictionsForTest) {
			evaluator.createTestInstances(testInput);
			evaluator.generatePredictions();
			evaluator.writePredictionsToFile(testInput + ".output");
		}
		else {
			
			System.out.println(evaluator.evaluateTrainingData(false));

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
	
}
