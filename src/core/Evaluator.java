package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Evaluator {

	private Instance test = new Instance(8);
	Instances testData = null ;
	
	// Constructor
	Evaluator(String testSource) {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(testSource));
			testData = new Instances(reader);
			
			if (testData.classIndex() == -1)
				testData.setClassIndex(testData.numAttributes() - 1);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.err.print("Unable to build instances from training input.\nExiting Program.");
			System.exit(1);
		}

		
		
		try {
			// Run evaluator
			Predictor.eval = new Evaluation(Predictor.trainingData);
		} catch (Exception e) {
			System.out.println("Unable to create evaluator.");
			System.exit(1);
		}
		
	}
	
	
	void addToTestData(String dataString) {
		
		String[] data = dataString.split(",");
		test = new Instance(8);
		
		// Insert city data into instance
		if (data[0].equals("?")) {
			test.setValue((Attribute)Predictor.attributes.elementAt(0), Instance.missingValue());
		}
		else {
			test.setValue((Attribute)Predictor.attributes.elementAt(0), data[0]);  
		}
		
		// Insert year data into instance		
		if (data[1].equals("?")) {
			test.setValue((Attribute)Predictor.attributes.elementAt(1), Instance.missingValue());
		}
		else {
			test.setValue((Attribute)Predictor.attributes.elementAt(1), Integer.parseInt(data[1]));
		}
		
		// Insert month data into instance
		if (data[2].equals("?")) {
			test.setValue((Attribute)Predictor.attributes.elementAt(2), Instance.missingValue());
		}
		else {
			test.setValue((Attribute)Predictor.attributes.elementAt(2), Integer.parseInt(data[2]));
		}
		    
		// Insert date data into instance
		if (data[3].equals("?")) {
			test.setValue((Attribute)Predictor.attributes.elementAt(3), Instance.missingValue());
		}
		else {
			test.setValue((Attribute)Predictor.attributes.elementAt(3), Integer.parseInt(data[3]));
		}
		
		// Insert rainfall data into instance
		if (data[4].equals("?")) {
			test.setValue((Attribute)Predictor.attributes.elementAt(4), Instance.missingValue());
		}
		else {
			test.setValue((Attribute)Predictor.attributes.elementAt(4), Double.parseDouble(data[4]));
		}
		
		// Insert maxTemp data into instance
		if (data[5].equals("?")) {
			test.setValue((Attribute)Predictor.attributes.elementAt(5), Instance.missingValue());
		}
		else {
			test.setValue((Attribute)Predictor.attributes.elementAt(5), Double.parseDouble(data[5]));
		}
		
		// Insert minTemp data into instance
		if (data[6].equals("?")) {
			test.setValue((Attribute)Predictor.attributes.elementAt(6), Instance.missingValue());
		}
		else {
			test.setValue((Attribute)Predictor.attributes.elementAt(6), Double.parseDouble(data[6]));
		}
		
		// Insert day data into instance
		test.setValue((Attribute)Predictor.attributes.elementAt(7), data[7]);

		testData.add(test);
		

//		// Specify that the instance belong to the training set
//		// in order to inherit from the set description
//		test.setDataset(Predictor.trainingData);
	}

	String getEvaluation() {
		
		try {	
			// Compare with test data
			//Predictor.eval.evaluateModel(Predictor.classifier, testData);
			
			Predictor.eval.crossValidateModel(Predictor.classifier, Predictor.trainingData, 10, new Random(1));
			
			
			
		} catch (Exception e) {
			System.err.print("Could not run evaluator from weka.");
		}
		
		return Predictor.eval.toSummaryString("\nResults\n======\n", true);
		
	}
	
	double[] getFDistribution() {
		double[] distributions = null;

		try {
			// get fDistribution
			distributions = Predictor.classifier.distributionForInstance(test);
			
		} catch (Exception e) {
			System.err.println("Unable to get distribution.");
			return null;
		}
		
		return distributions;

	}
	
}
