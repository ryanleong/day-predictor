package core;

import java.io.BufferedReader;
import java.io.FileReader;

import dataClassifiers.RandomForestClassifier;
import dataClassifiers.StackingClassifier;
import dataClassifiers.BaggingClassifier;
import dataClassifiers.DecisionTreeClassifier;
import dataClassifiers.NaiveBayesClassifier;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class Predictor {

	// Enum of classifier type
	public static enum classifierType {
		NAIVEBAYES, DECISION_TREE, BAGGING, STACKING, RANDOM_FOREST
	}

	// Flag to predict for data with missing "day" value
	// I.E. test data set
	private static boolean evaluateAgainstSelf = false;

	// Classifier Type
	public static classifierType cType = classifierType.RANDOM_FOREST;

	/////////////////////////////////////////////////

	// Location of training and test data
	private static String trainingInput = "datasets/bris.train.arff";
	private static String testInput = "datasets/bris.dev.arff";
	
	// Training data
	private static Instances trainingData;
	private static Instances testData;

	
public static void main(String[] args) {
		
		if (args.length != 0) {
			handleArguments(args);
		}

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
			
			
			
		} else if (cType == classifierType.BAGGING) {
			System.out.println("Classifier used: Bagging (Used with Decision Trees)");

			BaggingClassifier bag = new BaggingClassifier(trainingData);

			if (evaluateAgainstSelf) {
				eval.doEvaluation(bag.getClassifier(), trainingData);
			}
			else {
				eval.doEvaluation(bag.getClassifier(), testData);
				eval.writePredictionsToFile(testInput + ".BaggingOutPut", testData);
			}
			
			
		} else if(cType == classifierType.STACKING) {
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
			
		} else {
			
			System.out.println("Classifier used: Random Forest\n");
			
			RandomForestClassifier rf = new RandomForestClassifier(trainingData);
			
			System.out.println(rf.getClassifier().toString());
			
			if (evaluateAgainstSelf) {
				eval.doEvaluation(rf.getClassifier(), trainingData);
			}
			else {
				eval.doEvaluation(rf.getClassifier(), testData);
				eval.writePredictionsToFile(testInput + ".RandomForestOutPut", testData);
			}
		}

		System.out.println(eval.getEvaluationStats());
		System.out.println(eval.confusionMatrixToString());
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
			System.err.print("Unable to build instances from training input.\n" +
					"Exiting Program.");
			System.exit(1);
		}

		System.out.println("Done!\n");
	}

	private static void buildTrainingDataSet() {
		System.out.print("Reading in training data... ");

		try {

			// Build training data set
			BufferedReader reader = new BufferedReader(new FileReader(trainingInput));
			trainingData = new Instances(reader);

			// // combine dev and training instances
			// reader = new BufferedReader(new FileReader("melb.dev"));
			// Instances temp = new Instances(reader);
			//
			// for (int i = 0; i < temp.numInstances(); i++) {
			// Predictor.trainingData.add(temp.instance(i));
			// }
			//
			// temp = null;
			
			
			// Delete instances with missing data
			for (int i = 1; i < trainingData.numAttributes(); i++) {
				trainingData.deleteWithMissing(i);
			}
			

			if (Predictor.trainingData.classIndex() == -1)
				Predictor.trainingData.setClassIndex(Predictor.trainingData
						.numAttributes() - 1);

		} catch (Exception e) {
			System.err.print("Unable to build instances from training input.\n" +
							"Exiting Program.");
			System.exit(1);
		}

		System.out.println("Done!\n");
	}

	private static void handleArguments(String[] args) {
		if (args.length != 0) {
			try {
				
				// if -h flag is used
				if (args[0].equals("-h")) {
					
					System.out.println("usage: \n/usr/java1.6/bin/java -Xmx128m -cp bin:. " + 
							"core.Predictor [-t] [-e] [-c classifier]\n" +
							"-e\t\t: Path of test data to be evaluated\n" +
							"-t\t\t: Path of training data\n" +
							"-c classifier\t: An integer that represents the use of 1 of the 4 classifiers\n" +
							"\t\t\t1 - NaiveBayes\n" +
							"\t\t\t2 - Decision Trees\n" +
							"\t\t\t3 - Bagging\n" +
							"\t\t\t4 - Stacking");

					System.exit(0);
				}

				for (int i = 0; i < args.length; i++) {
					
//					// if -t flag is used
//					if (args[i].equals("-t")) {
//						technique = Integer.parseInt(args[i + 1]);
//
//						if (technique < 1 || technique > 4) {
//							System.out
//									.println("Choose between algorithms 1, 2, 3 or 4.");
//							System.exit(0);
//						}
//					} 
//					// if -d flag is used
//					else if (args[i].equals("-d")) {
//						dictFileName = args[i + 1];
//
//						File f = new File(dictFileName);
//
//						if (!f.exists()) {
//							System.out.println("Dictionary not found.");
//							System.exit(0);
//						}
//					} 
//					// if -m flag is used
//					else if (args[i].equals("-m")) {
//						
//						evaluationOutput = false;
//					} 
//					// if -a flag is used
//					else if (args[i].equals("-a")) {
//						
//						useAbbreviationDictionary = true;
//					}
//					// if -f flag is used
//					else if (args[i].equals("-f")) {
//						
//						inputFile = args[i + 1];
//						
//						File f = new File(inputFile);
//
//						if (!f.exists()) {
//							System.out.println("Input file not found.");
//							System.exit(0);
//						}
//					}
				}

			} catch (Exception e) {
				System.out.println("Invalid arguments\nRunning with default settings");
			}
		}
	}

	
}
