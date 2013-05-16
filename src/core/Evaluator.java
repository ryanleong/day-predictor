package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Evaluator {

	private Instance test = new Instance(8);
	Instances testData = null;

	// Constructor
	Evaluator() {

		try {
			// Run evaluator
			Predictor.eval = new Evaluation(Predictor.trainingData);
		} catch (Exception e) {
			System.out.println("Unable to create evaluator.");
			System.exit(1);
		}
	}

	void wekaRead(String testSource) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					testSource));
			testData = new Instances(reader);

			if (testData.classIndex() == -1)
				testData.setClassIndex(testData.numAttributes() - 1);

		} catch (Exception e) {
			// TODO: handle exception
			System.err.print("Unable to build instances from training input.\nExiting Program.");
			System.exit(1);
		}

	}

	void doPredictionForTest(String testDataInput) {
		// Index of instance
		int indexOfInstance = 0;
		
		// Buffer to read from file
		BufferedReader br = null;
		
		// Buffer to write to file
		BufferedWriter bw = null;
		
		// Get an instance from the training data
		Instance tempTrainingInstance = Predictor.trainingData.instance(0);

		// New attributes object to be put into test data
		FastVector testAttributes = new FastVector(
				Predictor.trainingData.numAttributes());

		// Place attributes from training instance to Attribute objects
		for (int i = 0; i < Predictor.trainingData.numAttributes(); i++) {
			testAttributes.addElement(tempTrainingInstance.attribute(i));
		}
		
		
		// Create an empty training set
		testData = new Instances("Weather Test", testAttributes, 10);

		// Set class index
		if (testData.classIndex() == -1)
			testData.setClassIndex(testData.numAttributes() - 1);


		try {

			String currentLine;

			br = new BufferedReader(new FileReader(testDataInput));

			// Location of file to write to
			File file = new File(testDataInput + ".output");
			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// File writer
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			
			
			while ((currentLine = br.readLine()) != null) {
				// Split String into array
				String[] testInstanceData = currentLine.split(",");
				
				// Create the instance
				Instance dataInstance = new Instance(Predictor.trainingData.numAttributes());

				// place data in instance
				for (int i = 0; i < testInstanceData.length; i++) {
					
					if(tempTrainingInstance.attribute(i).isNominal()) {
						if (testInstanceData[i].equals("?")) {
							dataInstance.setValue((Attribute)testAttributes.elementAt(i), Instance.missingValue());
						}
						else {
							dataInstance.setValue((Attribute)testAttributes.elementAt(i), testInstanceData[i]);
						}
						 
					}
					else if (tempTrainingInstance.attribute(i).isNumeric()) {
						
						// check if missing value
						if (testInstanceData[i].equals("?")) {
							dataInstance.setValue((Attribute)testAttributes.elementAt(i), Instance.missingValue());
						}
						else {
							dataInstance.setValue((Attribute)testAttributes.elementAt(i), Double.parseDouble(testInstanceData[i]));
						}
					}
					
				}
				
				
				// add the instance
				testData.add(dataInstance);
				
				//System.out.println(testData.instance(0).toString());
				
				// Make prediction
				String day = predictClassForInstance(indexOfInstance);
			
				// Combine instance data parts into string
				String out = "";

				for (int i = 0; i < testInstanceData.length - 1; i++) {
					out = out + testInstanceData[i] + ",";
				}
				
				out = out + day +"\n";
				
				// Write to file
				bw.write(out);
				
				// Increament index
				indexOfInstance++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if(bw != null)
					bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
			
		}

	}
	
	String predictClassForInstance(int indexOfInstance) {
		String day = "";

		try {
			
			int dayNo = (int) Predictor.eval.evaluateModelOnce(Predictor.classifier, testData.instance(indexOfInstance));
			
			System.out.print(dayNo + ", ");
			
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

		} catch (Exception e) {
			e.printStackTrace();
			System.err.print("Could not evaluate instance.\nExiting program.");
			System.exit(1);
		}

		return day;
	}
	
	// Evaluate entire data set
	String getEvaluation() {

		try {
			// Compare with test data
			// Predictor.eval.evaluateModel(Predictor.classifier, testData);

			Predictor.eval.crossValidateModel(Predictor.classifier, testData,
					10, new Random(1));

		} catch (Exception e) {
			System.err.print("Could not run evaluator from weka.");
			System.exit(1);
		}

		return Predictor.eval.toSummaryString("\nResults\n=========\n", true);

	}

}
