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
	

}
