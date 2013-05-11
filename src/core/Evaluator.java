package core;

import weka.core.Attribute;
import weka.core.Instance;

public class Evaluator {

	private Instance test = new Instance(8);
	
	// Constructor
	Evaluator(String dataString) {
		
		String[] data = dataString.split(",");
		
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
		test.setValue((Attribute)Predictor.attributes.elementAt(7), Instance.missingValue());

		// Specify that the instance belong to the training set
		// in order to inherit from the set description
		test.setDataset(Predictor.trainingData);
	}

	double[] getFDistribution() {
		double[] distributions = null;

		try {
			// get fDistribution
			distributions = Predictor.naiveBayesClassifier
					.distributionForInstance(test);
		} catch (Exception e) {
			System.err.println("Unable to get distribution.");
			return null;
		}
		return distributions;

	}
	
	String getProbablyDay() {
		
		double[] distributions = null;
		String day = "";

		try {
			// get fDistribution
			distributions = Predictor.naiveBayesClassifier
					.distributionForInstance(test);
			
			int probableDay = 0;
			
			for (int i = 1; i < distributions.length; i++) {
				if(distributions[i] > distributions[i-1]) {
					probableDay = i;
				}
			}
			
			switch (probableDay) {
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
			System.err.println("Unable to get distribution.");
			return null;
		}
		
		return day;
	}
}
