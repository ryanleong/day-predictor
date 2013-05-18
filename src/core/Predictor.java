package core;

import java.io.BufferedReader;
import java.io.FileReader;

import dataClassifiers.AggregateClassifier;
import dataClassifiers.BaggingClassifier;
import dataClassifiers.DecisionTreeClassifier;
import dataClassifiers.NaiveBayesClassifier;

import weka.core.Instances;

public class Predictor {

	// Flag to predict for data with missing "day" value
	// I.E. test data set
	private static boolean predictionsForTest = true;

	// Classifier Type
	public static classifierType cType = classifierType.NAIVEBAYES;

	// Location of training and test data
	private static String trainingInput = "datasets/melb.train.arff";
	private static String testInput = "datasets/melb.dev.arff";

	// ///////////////////////////////////////////////

	// Training data
	private static Instances trainingData;
	private static Instances testData;

	// Enum of classifier type
	public static enum classifierType {
		NAIVEBAYES, DECISION_TREE, SUPPORT_VECTOR_MACHINE, VOTE, BAGGING
	}

	public static void main(String[] args) {

		// printClassifierType();

		// Build data sets
		buildTrainingDataSet();
		buildTestDataSet();

		// Use specified classifier
		if (cType == classifierType.NAIVEBAYES) {

			System.out.println("Classifier used: NaiveBayes Classifer");

			NaiveBayesClassifier nb = new NaiveBayesClassifier(trainingData);
			// System.out.println(nb.getClassifier().toString());
			
			Evaluator eval = new Evaluator(trainingData);
			
			eval.doEvaluation(nb.getClassifier(), testData);
			//System.out.println(testData.numInstances() + "\t" + eval.getPredictions(nb.getClassifier(), testData).length);
			
			eval.writePredictionsToFile(testInput + ".out", trainingData);
			
			
		} else if (cType == classifierType.DECISION_TREE) {
			System.out.println("Classifier used: Decision Trees");

			DecisionTreeClassifier dt = new DecisionTreeClassifier(trainingData);
			// System.out.println(dt.getClassifier().toString());
			
			
			
		} else if (cType == classifierType.BAGGING) {
			System.out
					.println("Classifier used: Bagging (Used with Decision Trees)");

			BaggingClassifier bag = new BaggingClassifier(trainingData);
			//System.out.println(bag.getClassifier().toString());
		
			
			
		} else {
			System.out
					.println("Classifier used: An aggregate of NaiveBayes and Decision Trees\n"
							+ "");

			classifierType[] cl = {classifierType.NAIVEBAYES, classifierType.DECISION_TREE};
			
			AggregateClassifier ac = new AggregateClassifier(trainingData, cl);
		}

		System.exit(0);
		// ////////////////////////////////

		// Create classifier
		// Classifiers trainingClassifier = new Classifiers();
		//
		// System.out.println(trainingData.toSummaryString() + "\n");
		//
		// System.out.println(testData.toSummaryString() + "\n");
		//
		// runEvaluation();

	}

	private static void buildTrainingDataSet() {
		System.out.print("Reading in training data... ");

		try {

			// Build training data set
			BufferedReader reader = new BufferedReader(new FileReader(
					trainingInput));
			Predictor.trainingData = new Instances(reader);

			// // combine dev and training instances
			// reader = new BufferedReader(new FileReader("melb.dev"));
			// Instances temp = new Instances(reader);
			//
			// for (int i = 0; i < temp.numInstances(); i++) {
			// Predictor.trainingData.add(temp.instance(i));
			// }
			//
			// temp = null;

			if (Predictor.trainingData.classIndex() == -1)
				Predictor.trainingData.setClassIndex(Predictor.trainingData
						.numAttributes() - 1);

		} catch (Exception e) {
			System.err
					.print("Unable to build instances from training input.\nExiting Program.");
			System.exit(1);
		}

		System.out.println("Done!\n");
	}

	private static void buildTestDataSet() {
		System.out.print("Reading in test data... ");

		try {

			// read from test data source
			BufferedReader reader = new BufferedReader(
					new FileReader(testInput));
			testData = new Instances(reader);

			if (testData.classIndex() == -1)
				testData.setClassIndex(testData.numAttributes() - 1);

		} catch (Exception e) {
			System.err
					.print("Unable to build instances from training input.\nExiting Program.");
			System.exit(1);
		}

		System.out.println("Done!\n");
	}

	private static void runEvaluation() {

		if (predictionsForTest) {

		} else {

			// System.out.println(evaluator.evaluateTrainingData(false));

			// Confusion matrix
			// double[][] x = eval.confusionMatrix();
			// System.out.println("Mon\tTue\tWed\tThu\tFri\tSat\tSun");
			//
			// for (int i = 0; i < x.length; i++) {
			//
			// for (int j = 0; j < x[i].length; j++) {
			// System.out.print(x[i][j] + "\t");
			// }
			//
			// System.out.println();
			// }
		}

	}

	private static void printClassifierType() {

		if (cType == classifierType.NAIVEBAYES) {
			System.out.println("Classifier used: NaiveBayes Classifer");
		} else if (cType == classifierType.DECISION_TREE) {
			System.out.println("Classifier used: Decision Trees");
		} else if (cType == classifierType.BAGGING) {
			System.out
					.println("Classifier used: Bagging (Used with Decision Trees)");
		} else {
			System.out
					.println("Classifier used: An aggregate of NaiveBayes and Decision Trees\n"
							+ "");
		}
	}
}
