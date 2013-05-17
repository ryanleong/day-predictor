#!/bin/bash

javac -d bin/ -cp lib/weka.jar src/core/Evaluator.java src/core/Classifiers.java src/core/Predictor.java


#run
java -Xmx128m -cp bin:lib/weka.jar core.Predictor
exit
