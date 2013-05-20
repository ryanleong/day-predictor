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
			
			// Use binary splits for nominal attributes.
			String[] options = weka.core.Utils.splitOptions("-M 6");
			decisionTreeClassifier.setOptions(options);
			
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
