package core;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Classifier {

	// Naive based classifier
	static NaiveBayes naiveBayesClassifier = new NaiveBayes();
	
	// Feature vector
	private FastVector fvWekaAttributes = null;

	Classifier() {
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
		fvWekaAttributes = new FastVector(8);
		fvWekaAttributes.addElement(city);
		fvWekaAttributes.addElement(year);
		fvWekaAttributes.addElement(month);
		fvWekaAttributes.addElement(date);
		fvWekaAttributes.addElement(rainfall);
		fvWekaAttributes.addElement(maxTemp);
		fvWekaAttributes.addElement(minTemp);
		fvWekaAttributes.addElement(day);

		Predictor.trainingData = new Instances("Weather", fvWekaAttributes, 10);

		if (Predictor.trainingData.classIndex() == -1)
			Predictor.trainingData.setClassIndex(Predictor.trainingData
					.numAttributes() - 1);
		
	}

	void createInstance(String dataString) {
		String[] data = dataString.split(",");
		Instance entry = new Instance(8);
		
		if ((Integer.parseInt(data[1])) < 1945) {
			return;
		}
		
		for (int i = 0; i < data.length; i++) {
			
			if (data[i].equals("?")) {
				return;
			}
		}
		
		
		entry.setValue((Attribute)fvWekaAttributes.elementAt(0), data[0]);      
		entry.setValue((Attribute)fvWekaAttributes.elementAt(1), Integer.parseInt(data[1]));      
		entry.setValue((Attribute)fvWekaAttributes.elementAt(2), Integer.parseInt(data[2]));
		entry.setValue((Attribute)fvWekaAttributes.elementAt(3), Integer.parseInt(data[3]));
		entry.setValue((Attribute)fvWekaAttributes.elementAt(4), Double.parseDouble(data[4]));
		entry.setValue((Attribute)fvWekaAttributes.elementAt(5), Double.parseDouble(data[5]));
		entry.setValue((Attribute)fvWekaAttributes.elementAt(6), Double.parseDouble(data[6]));
		entry.setValue((Attribute)fvWekaAttributes.elementAt(7), data[7]);

		// add the instance
		Predictor.trainingData.add(entry);
	}
	
	void completeTrainingClassifier() {
		try {
			// NaiveBayes object
			NaiveBayes nb = new NaiveBayes();
			nb.buildClassifier(Predictor.trainingData);

//			Evaluation eval = new Evaluation(Predictor.trainingData);
//			eval.crossValidateModel(nb, Predictor.trainingData, 2, new Random(1));
		} catch (Exception e) {
			// TODO: handle exception
			System.err.print("Unable to create classifier or evaluator.");
		}
		

	}
}
