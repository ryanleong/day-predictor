package core;

import java.io.BufferedReader;
import java.io.FileReader;

import core.Predictor.classifierType;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.MultipleClassifiersCombiner;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.Stacking;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.neighboursearch.covertrees.Stack;

public class Classifiers {
	
	// Feature vector
	//private FastVector fvWekaAttributes = null;

	Classifiers() {
		
		
		
		System.out.print("Building classifiers... ");
		
		if (Predictor.enableBagging) {
			
			try {
				// Build NaiveBayes Classifier
				Predictor.naiveBayesClassifier = new NaiveBayes();
				Predictor.naiveBayesClassifier.buildClassifier(Predictor.trainingData);
				
				// Build decision tree classifier
				Predictor.decisionTreeClassifier = new J48();
				Predictor.decisionTreeClassifier.buildClassifier(Predictor.trainingData);
				
				
				
				Stacking s = new Stacking();
				
				Classifier[] list = {Predictor.naiveBayesClassifier, Predictor.decisionTreeClassifier};
				s.setClassifiers(list);
				
				s.setMetaClassifier(new NaiveBayes());
				s.buildClassifier(Predictor.trainingData);
				
				//System.out.println(s.toString());
				
				Predictor.eval = new Evaluation(Predictor.trainingData);
				double[] predictions = Predictor.eval.evaluateModel(s.getMetaClassifier(), Predictor.trainingData);
				
				
				
				System.out.println(Predictor.eval.toSummaryString());
				
				for (int i = 0; i < predictions.length; i++) {
					System.out.print(predictions[i]+ ", ");
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			
			////////////////////
//			try {
//				// create evaluator
//				Predictor.eval = new Evaluation(Predictor.trainingData);
//				
//			} catch (Exception e) {
//				System.out.println("Unable to create evaluator.");
//				System.exit(1);
//			}
//			
			
			
			System.exit(0);
			return;
		}
		
		
		
		
		
		////////////////////////
		
		try {	
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
	
	void combineClassifiers() {
		MultipleClassifiersCombiner mcc = new MultipleClassifiersCombiner() {
			
			@Override
			public void buildClassifier(Instances arg0) throws Exception {
				// TODO Auto-generated method stub
				
				
			}
		};
		
		Classifier[] list = {Predictor.naiveBayesClassifier, Predictor.decisionTreeClassifier};
		
		mcc.setClassifiers(list);
	}

}
