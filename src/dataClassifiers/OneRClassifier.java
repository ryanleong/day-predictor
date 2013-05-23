package dataClassifiers;

import weka.classifiers.Classifier;
import weka.classifiers.rules.OneR;
import weka.core.Instances;

public class OneRClassifier {

	Classifier OneRClassifier;
	
	public OneRClassifier(Instances trainingData) {
		// TODO Auto-generated constructor stub
		
		try {
			OneRClassifier = new OneR();
			OneRClassifier.buildClassifier(trainingData);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	public Classifier getClassifier() {
		return OneRClassifier;
	}
	
}
