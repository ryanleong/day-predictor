package core;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Classifiers {
	
	// Feature vector
	//private FastVector fvWekaAttributes = null;

	Classifiers() {
		// Declare a nominal attribute along with its values
		FastVector fvNominalVal = new FastVector(3);
		fvNominalVal.addElement("Melbourne");
		fvNominalVal.addElement("Perth");
		fvNominalVal.addElement("Sydney");
		Attribute city = new Attribute("city", fvNominalVal);

		// Declare four numeric attributes
		Attribute year = new Attribute("year");
		Attribute month = new Attribute("month");
		Attribute date = new Attribute("date");
		Attribute rainfall = new Attribute("rainfall");
		Attribute maxTemp = new Attribute("maxTemp");
		Attribute minTemp = new Attribute("minTemp");

		// Declare a nominal attribute along with its values
		FastVector fvNominalVal2 = new FastVector(7);
		fvNominalVal2.addElement("Mon");
		fvNominalVal2.addElement("Tue");
		fvNominalVal2.addElement("Wed");
		fvNominalVal2.addElement("Thu");
		fvNominalVal2.addElement("Fri");
		fvNominalVal2.addElement("Sat");
		fvNominalVal2.addElement("Sun");
		Attribute day = new Attribute("day", fvNominalVal2);

		// Declare the feature vector
		Predictor.attributes = new FastVector(8);
		Predictor.attributes.addElement(city);
		Predictor.attributes.addElement(year);
		Predictor.attributes.addElement(month);
		Predictor.attributes.addElement(date);
		Predictor.attributes.addElement(rainfall);
		Predictor.attributes.addElement(maxTemp);
		Predictor.attributes.addElement(minTemp);
		Predictor.attributes.addElement(day);

		Predictor.trainingData = new Instances("Weather", Predictor.attributes, 10);

		if (Predictor.trainingData.classIndex() == -1)
			Predictor.trainingData.setClassIndex(Predictor.trainingData
					.numAttributes() - 1);
		
	}

	void createInstance(String dataString) {
		String[] data = dataString.split(",");
		Instance entry = new Instance(8);
		
		// set data year limit
		if ((Integer.parseInt(data[1])) < 1945) {
			return;
		}
		
		// check for missing data
		for (int i = 0; i < data.length; i++) {
			
			if (data[i].equals("?")) {
				return;
			}
		}
		
		// Insert data into instance
		entry.setValue((Attribute)Predictor.attributes.elementAt(0), data[0]);      
		entry.setValue((Attribute)Predictor.attributes.elementAt(1), Integer.parseInt(data[1]));      
		entry.setValue((Attribute)Predictor.attributes.elementAt(2), Integer.parseInt(data[2]));
		entry.setValue((Attribute)Predictor.attributes.elementAt(3), Integer.parseInt(data[3]));
		entry.setValue((Attribute)Predictor.attributes.elementAt(4), Double.parseDouble(data[4]));
		entry.setValue((Attribute)Predictor.attributes.elementAt(5), Double.parseDouble(data[5]));
		entry.setValue((Attribute)Predictor.attributes.elementAt(6), Double.parseDouble(data[6]));
		entry.setValue((Attribute)Predictor.attributes.elementAt(7), data[7]);

		// add to data set
		Predictor.trainingData.add(entry);
	}
	
	void completeTrainingClassifier() {
		try {
			
			// NaiveBayes object
			Predictor.classifier.buildClassifier(Predictor.trainingData);


		} catch (Exception e) {
			System.err.print("Unable to create classifier or evaluator.");
		}
	}
	
}
