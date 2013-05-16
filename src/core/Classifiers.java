package core;

import java.io.BufferedReader;
import java.io.FileReader;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import core.Predictor.classifierType;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class Classifiers {
	
	// Feature vector
	//private FastVector fvWekaAttributes = null;

	Classifiers(String trainingSource) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(trainingSource));
			Predictor.trainingData = new Instances(reader);
			

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
