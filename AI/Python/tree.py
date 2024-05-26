# -*- coding: utf-8 -*-
# coding : utf-8
import numpy as np
from sklearn.tree import DecisionTreeRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
import pandas as pd
import random
import joblib

csvPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Data/data.csv"
savePath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Model/temp.pkl"

data = pd.read_csv(csvPath, encoding='UTF-8')

combined_df = pd.DataFrame(data)

data = np.array(combined_df)
X = data[:, :-1]
y = data[:, -1]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

regressor = DecisionTreeRegressor(
)

regressor.fit(X_train, y_train)

y_pred = regressor.predict(X_test)

print("Mean Squared Error:", mean_squared_error(y_test, y_pred))

joblib.dump(regressor, savePath)
