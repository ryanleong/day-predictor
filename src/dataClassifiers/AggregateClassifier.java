package dataClassifiers;

import core.Predictor;
import core.Predictor.classifierType;
import weka.classifiers.Classifier;
import weka.classifiers.meta.Vote;
import weka.core.Instances;

public class AggregateClassifier {

	Vote aggregateClassifier = null;
	Classifier[] classifierList = null;
	
	public AggregateClassifier(Instances trainingData, Predictor.classifierType[] cl) {
		
		classifierList = new Classifier[cl.length];
		
		for (int i = 0; i < cl.length; i++) {
			if (cl[i] == classifierType.NAIVEBAYES) {
				classifierList[i] = new NaiveBayesClassifier(trainingData).getClassifier();
			} 
			else if (cl[i] == classifierType.DECISION_TREE) {
				classifierList[i] = new DecisionTreeClassifier(trainingData).getClassifier();
			}
		}
		
		
		try {
			aggregateClassifier.setClassifiers(classifierList);
			aggregateClassifier.buildClassifier(trainingData);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public Vote getClassifierVote() {
		return aggregateClassifier;
	}
}
