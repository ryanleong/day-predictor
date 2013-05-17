package core;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;

public class Predictor {

	// Flag to predict for data with missing "day" value
	// I.E. test data set
	private static boolean predictionsForTest = false;
	
	// Flag to enable use of comparison between different classifiers 
	static boolean multipleClassifierComparision = false;
	
	// Classifier Type
	static classifierType cType = classifierType.NAIVEBAYES;
	
	// Location of training and test data
	private static String trainingInput = "datasets/melb.train.arff";
	private static String testInput = "datasets/melb.test.arff";

	// Flag to enable use of bagging and decision trees
	static boolean enableBagging = true;
	
	/////////////////////////////////////////////////
	
	// Classifier
	static Classifier naiveBayesClassifier = null;
	static Classifier decisionTreeClassifier = null;
	static Classifier supportVectorMachineClassifier = null;
	//static Bagging bag = null;

	// Training data
	static Instances trainingData;
	static Instances testData;
	
	// Evaluator
	static Evaluation eval = null;

	// Enum of classifier type
	public static enum classifierType {
		NAIVEBAYES, DECISION_TREE, SUPPORT_VECTOR_MACHINE
	}

	public static void main(String[] args) {

		buildTrainingDataSet();
		buildTestDataSet();
		
		// Create classifier
		Classifiers trainingClassifier = new Classifiers();
		
		System.out.println(trainingData.toSummaryString());

		
		runEvaluation();

	}
	
	private static void buildTrainingDataSet() {
		System.out.print("Reading in training data... ");
		
		try {
			
			// Build training data set
			BufferedReader reader = new BufferedReader(new FileReader(trainingInput));
			Predictor.trainingData = new Instances(reader);
			
//			// combine dev and training instances
//			reader = new BufferedReader(new FileReader("melb.dev"));
//			Instances temp = new Instances(reader);
//			
//			for (int i = 0; i < temp.numInstances(); i++) {
//				Predictor.trainingData.add(temp.instance(i));
//			}
//			
//			temp = null;
			
			if (Predictor.trainingData.classIndex() == -1)
				Predictor.trainingData.setClassIndex(Predictor.trainingData.numAttributes() - 1);
			
		} catch (Exception e) {
			System.err.print("Unable to build instances from training input.\nExiting Program.");
			System.exit(1);
		}

		System.out.println("Done!\n");
	}
	
	private static void buildTestDataSet() {
		System.out.print("Reading in test data... ");
		
		try {
			
			// read from test data source
			BufferedReader reader = new BufferedReader(new FileReader(testInput));
			testData = new Instances(reader);

			if (testData.classIndex() == -1)
				testData.setClassIndex(testData.numAttributes() - 1);

			
		} catch (Exception e) {
			System.err.print("Unable to build instances from training input.\nExiting Program.");
			System.exit(1);
		}
		
		System.out.println("Done!\n");
	}


	private static void runEvaluation() {
		Evaluator evaluator = new Evaluator();
		
		if (predictionsForTest) {
			//evaluator.createTestInstances(testInput);
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
