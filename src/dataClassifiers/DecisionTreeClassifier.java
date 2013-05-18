package dataClassifiers;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class DecisionTreeClassifier {
	
	Classifier decisionTreeClassifier = null;
	
	public DecisionTreeClassifier(Instances trainingData) {
		
		try {
			// Build NaiveBayes Classifier
			decisionTreeClassifier = new J48();
			decisionTreeClassifier.buildClassifier(trainingData);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public Classifier getClassifier() {
		return decisionTreeClassifier;
	}
	
}
