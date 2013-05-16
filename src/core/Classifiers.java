package core;

import java.io.BufferedReader;
import java.io.FileReader;

import core.Predictor.classifierType;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class Classifiers {
	
	// Feature vector
	//private FastVector fvWekaAttributes = null;

	Classifiers(String trainingSource) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(trainingSource));
			Predictor.trainingData = new Instances(reader);
			
//			// combine dev and training instances
//			reader = new BufferedReader(new FileReader("melb.dev"));
//			Instances temp = new Instances(reader);
//			
//			for (int i = 0; i < temp.numInstances(); i++) {
//				Predictor.trainingData.add(temp.instance(i));
//			}
//			
//			temp = null;
			
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
			
			System.out.print("Building classifiers... ");
			
			// Build all classifiers if flag is enabled
			if (Predictor.multipleClassifierComparision) {
				
				// Build NaiveBayes Classifier
				Predictor.naiveBayesClassifier = new NaiveBayes();
				Predictor.naiveBayesClassifier.buildClassifier(Predictor.trainingData);
				
				// Build decision tree classifier
				Predictor.decisionTreeClassifier = new J48();
				Predictor.decisionTreeClassifier.buildClassifier(Predictor.trainingData);
				
				// Build support vector machine classifier
				Predictor.supportVectorMachineClassifier = new SMO();
				Predictor.supportVectorMachineClassifier.buildClassifier(Predictor.trainingData);
			}
			else {
				if (Predictor.cType == classifierType.DECISION_TREE) {
					// Build decision tree classifier
					Predictor.decisionTreeClassifier = new J48();
					Predictor.decisionTreeClassifier.buildClassifier(Predictor.trainingData);
				}
				else if (Predictor.cType == classifierType.SUPPORT_VECTOR_MACHINE) {
					// Build support vector machine classifier
					Predictor.supportVectorMachineClassifier = new SMO();
					Predictor.supportVectorMachineClassifier.buildClassifier(Predictor.trainingData);
				}
				else {
					// Build NaiveBayes Classifier by default
					Predictor.naiveBayesClassifier = new NaiveBayes();
					Predictor.naiveBayesClassifier.buildClassifier(Predictor.trainingData);
				}
			}

			System.out.println("Done!\n");
			
		} catch (Exception e) {
			System.err.print("Unable to create classifier or evaluator.");
		}
	}
	

}
