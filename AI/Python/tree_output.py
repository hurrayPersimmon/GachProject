# -*- coding: utf-8 -*-

import pickle
import sys
import joblib
import os

birthYear = float(sys.argv[1])
gender = float(sys.argv[2])
height = float(sys.argv[3])
weight = float(sys.argv[4])
walkSpeed = float(sys.argv[5])
temperature = float(sys.argv[6])
precipitationProbability = float(sys.argv[7])
precipitation = float(sys.argv[8])
weightShortest = float(sys.argv[9])
weightOptimal = float(sys.argv[10])

regressor = joblib.load("/home/t24102/AI/temp.pkl")

X_new = [[birthYear, gender, height, weight, walkSpeed, temperature, precipitationProbability, precipitation, weightShortest, weightOptimal]]

y_pred_new = regressor.predict(X_new)
print(y_pred_new)
