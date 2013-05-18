package core;

import java.io.BufferedReader;
import java.io.FileReader;

import dataClassifiers.StackingClassifier;
import dataClassifiers.BaggingClassifier;
import dataClassifiers.DecisionTreeClassifier;
import dataClassifiers.NaiveBayesClassifier;

import weka.core.Instances;

public class Predictor {

	// Flag to predict for data with missing "day" value
	// I.E. test data set
	private static boolean evaluateAgainstSelf = false;

	// Classifier Type
	public static classifierType cType = classifierType.DECISION_TREE;

	// Location of training and test data
	private static String trainingInput = "datasets/melb.train.arff";
	private static String testInput = "datasets/melb.dev.arff";

	// ///////////////////////////////////////////////

	// Training data
	private static Instances trainingData;
	private static Instances testData;

	// Enum of classifier type
	public static enum classifierType {
		NAIVEBAYES, DECISION_TREE, STACKING, BAGGING
	}

	public static void main(String[] args) {

		// Build data sets
		buildTrainingDataSet();
		buildTestDataSet();
		
		// Build evaluator
		Evaluator eval = new Evaluator(trainingData);

		// Use specified classifier
		if (cType == classifierType.NAIVEBAYES) {

			System.out.println("Classifier used: NaiveBayes Classifer");

			NaiveBayesClassifier nb = new NaiveBayesClassifier(trainingData);
			
			if (evaluateAgainstSelf) {
				eval.doEvaluation(nb.getClassifier(), trainingData);
			}
			else {
				eval.doEvaluation(nb.getClassifier(), testData);
				eval.writePredictionsToFile(testInput + ".NaiveBayesOutput", testData);
			}
			
			System.out.println(eval.getEvaluationStats());
			
		} else if (cType == classifierType.DECISION_TREE) {
			
			System.out.println("Classifier used: Decision Trees");

			DecisionTreeClassifier dt = new DecisionTreeClassifier(trainingData);

			if (evaluateAgainstSelf) {
				eval.doEvaluation(dt.getClassifier(), trainingData);
			}
			else {
				eval.doEvaluation(dt.getClassifier(), testData);
				eval.writePredictionsToFile(testInput + ".DecisionTreeOutput", testData);
			}
			
			System.out.println(eval.getEvaluationStats());
			
			
			
		} else if (cType == classifierType.BAGGING) {
			System.out
					.println("Classifier used: Bagging (Used with Decision Trees)");

			BaggingClassifier bag = new BaggingClassifier(trainingData);

			if (evaluateAgainstSelf) {
				eval.doEvaluation(bag.getClassifier(), trainingData);
			}
			else {
				eval.doEvaluation(bag.getClassifier(), testData);
				eval.writePredictionsToFile(testInput + ".BaggingOutPut", testData);
			}
			
			System.out.println(eval.getEvaluationStats());
		
			
		} else {
			System.out.println("Classifier used: An aggregate of NaiveBayes and Decision Trees\n");

			classifierType[] cl = {classifierType.NAIVEBAYES, classifierType.DECISION_TREE};
			
			StackingClassifier stack = new StackingClassifier(trainingData, cl);
			
			if (evaluateAgainstSelf) {
				eval.doEvaluation(stack.getClassifier(), trainingData);
			}
			else {
				eval.doEvaluation(stack.getClassifier(), testData);
				eval.writePredictionsToFile(testInput + ".StackingOutPut", testData);
			}
			
			System.out.println(eval.getEvaluationStats());
		}

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

}
