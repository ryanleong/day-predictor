package core;

import java.io.BufferedReader;
import java.io.FileReader;

import core.Predictor.classifierType;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Classifiers {
	
	// Feature vector
	//private FastVector fvWekaAttributes = null;

	Classifiers(String trainingSource) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(trainingSource));
			Predictor.trainingData = new Instances(reader);
			
		} catch (Exception e) {
			System.err.print("Unable to build instances from training input.\nExiting Program.");
			System.exit(1);
		}
		

		if (Predictor.trainingData.classIndex() == -1)
			Predictor.trainingData.setClassIndex(Predictor.trainingData.numAttributes() - 1);
		
	}
	
	// To build classifiers
	void buildTrainingClassifier() {
		try {
			
			if (Predictor.cType == classifierType.NAIVEBAYES) {
				Predictor.classifier = new NaiveBayes();
			} else {
				Predictor.classifier = new J48();
			}
			
			// NaiveBayes object
			Predictor.classifier.buildClassifier(Predictor.trainingData);


		} catch (Exception e) {
			System.err.print("Unable to create classifier or evaluator.");
		}
	}
	
	// To add an instance manually
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
}
