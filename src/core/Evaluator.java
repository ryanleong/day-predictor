package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import core.Predictor.classifierType;

import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Evaluator {

	Instances testData = null;
	double[] naiveBayesPredictions;
	double[] decisionTreePredictions;
	double[] supportVectorMachinePredictions;

	// Constructor
	Evaluator() {

		try {
			// create evaluator
			Predictor.eval = new Evaluation(Predictor.trainingData);
			
		} catch (Exception e) {
			System.out.println("Unable to create evaluator.");
			System.exit(1);
		}
		
	}
	
	void generatePredictions() {
		try {
			
			if (Predictor.multipleClassifierComparision) {
				// Get NaiveBayes predictions
				naiveBayesPredictions = Predictor.eval.evaluateModel(Predictor.naiveBayesClassifier, testData);
				
				// Get Decision Tree predictions
				decisionTreePredictions = Predictor.eval.evaluateModel(Predictor.decisionTreeClassifier, testData);
				
				// Get Support Vector Machine predictions
				supportVectorMachinePredictions = Predictor.eval.evaluateModel(Predictor.supportVectorMachineClassifier, testData);
			}
			
			else {
				// Get predictions
				if (Predictor.cType == classifierType.DECISION_TREE) {
					// Get Decision Tree predictions
					decisionTreePredictions = Predictor.eval.evaluateModel(Predictor.decisionTreeClassifier, testData);
				}
				else if (Predictor.cType == classifierType.SUPPORT_VECTOR_MACHINE) {
					// Get Support Vector Machine predictions
					supportVectorMachinePredictions = Predictor.eval.evaluateModel(Predictor.supportVectorMachineClassifier, testData);
				}
				else {
					// Get NaiveBayes predictions
					naiveBayesPredictions = Predictor.eval.evaluateModel(Predictor.naiveBayesClassifier, testData);
				}
			}
			

			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void writePredictionsToFile(String outputLocation) {

		System.out.print("Writing predictions to file... ");
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			
			File file = new File(outputLocation);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			
			// write to file
			for (int i = 0; i < testData.numInstances(); i++) {
				String outputString = "";
				
				for (int j = 0; j < testData.instance(i).numAttributes(); j++) {
					
					if (testData.instance(i).attribute(j).name().equals("F1") ||
							testData.instance(i).attribute(j).name().equals("F2") ||
							testData.instance(i).attribute(j).name().equals("F3")) {
						
						// set year, month and date
						outputString = outputString + (int)testData.instance(i).value(j) + ",";
					}
					else if (j == 0) {
						// set city
						outputString = outputString + testData.instance(i).stringValue(j) + ",";
					}
					else if (j == (testData.instance(i).numAttributes() - 1)) {
						// set prediction
						outputString = outputString + choosePredictions(i) + "\n";
					}
					else {
						//set everything else
						if (testData.instance(i).isMissing(j)) {
							outputString = outputString + "?,";
						}
						else {
							
							if (testData.instance(i).attribute(j).isNominal()){
								outputString = outputString + testData.instance(i).stringValue(j) + ",";
							}
							else {
								outputString = outputString + testData.instance(i).value(j) + ",";
							}
							
						}
						
					}
				}
				
				bw.write(outputString);
			}
			
			
			bw.close();
 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}	
		}
		
		System.out.print("Done!\n");
	}
	
	String dayNoToString(int dayNo) {
		String day = "";
		
		switch (dayNo) {
		case 0:
			day = "Mon";
			break;
		case 1:
			day = "Tue";
			break;
		case 2:
			day = "Wed";
			break;
		case 3:
			day = "Thu";
			break;
		case 4:
			day = "Fri";
			break;
		case 5:
			day = "Sat";
			break;
		case 6:
			day = "Sun";
			break;
		default:
			break;
		}
		
		return day;
	}
	
	private String choosePredictions(int index) {
		
		// Use the majority of the three
		if (Predictor.multipleClassifierComparision) {
			int[] predictions = new int[3];
			
			predictions[0] = (int) naiveBayesPredictions[index];
			predictions[1] = (int) decisionTreePredictions[index];
			predictions[2] = (int) supportVectorMachinePredictions[index];
			
			if (predictions[0] == predictions[1]) {
				return dayNoToString(predictions[0]);
			}
			else if (predictions[0] == predictions[2]) {
				return dayNoToString(predictions[2]);
			}
			else if (predictions[1] == predictions[2]) {
				return dayNoToString(predictions[1]);
			}
			else {
				return dayNoToString(predictions[0 + (int)(Math.random() * ((2 - 0) + 1))]);
			}
		}
		else {
			if (Predictor.cType == classifierType.DECISION_TREE) {
				return dayNoToString((int) decisionTreePredictions[index]);
			}
			else if (Predictor.cType == classifierType.SUPPORT_VECTOR_MACHINE) {
				return dayNoToString((int) supportVectorMachinePredictions[index]);
			}
			else {
				return dayNoToString((int) naiveBayesPredictions[index]);
			}
		}
	}
	
	
	// Evaluate entire training dataset against itself
	String evaluateTrainingData(boolean evaluateTestData) {

		try {

			if (evaluateTestData) {
				if (Predictor.cType == classifierType.DECISION_TREE) {
					Predictor.eval.crossValidateModel(Predictor.decisionTreeClassifier, testData, 10, new Random(1));
				}
				else if (Predictor.cType == classifierType.SUPPORT_VECTOR_MACHINE) {
					Predictor.eval.crossValidateModel(Predictor.supportVectorMachineClassifier, testData, 10, new Random(1));
				}
				else {
					// Default is NaiveBayes
					Predictor.eval.crossValidateModel(Predictor.naiveBayesClassifier, testData, 10, new Random(1));
				}
			}
			else {
				if (Predictor.cType == classifierType.DECISION_TREE) {
					Predictor.eval.crossValidateModel(Predictor.decisionTreeClassifier, Predictor.trainingData, 10, new Random(1));
				}
				else if (Predictor.cType == classifierType.SUPPORT_VECTOR_MACHINE) {
					Predictor.eval.crossValidateModel(Predictor.supportVectorMachineClassifier, Predictor.trainingData, 10, new Random(1));
				}
				else {
					// Default is NaiveBayes
					Predictor.eval.crossValidateModel(Predictor.naiveBayesClassifier, Predictor.trainingData, 10, new Random(1));
				}
			}

			
		} catch (Exception e) {
			System.err.print("Could not run evaluator from weka.");
			System.exit(1);
		}

		return Predictor.eval.toSummaryString("\nResults\n=========\n", true);

	}

		
}