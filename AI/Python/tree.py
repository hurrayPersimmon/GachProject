# -*- coding: utf-8 -*-
# coding : utf-8
import numpy as np
from sklearn.tree import DecisionTreeRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
import pandas as pd
import random
import joblib

#csvPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Data/data.csv"
# savePath = "/home/t24102/GachProject/AI/Model/temp.pkl"
csvPath = "/home/t24102/GachProject/AI/Data/data.csv";

data = pd.read_csv(csvPath, encoding='UTF-8')

def augment_data(row):
    augmented_rows = []
    for _ in range(10):
        augmented_row = row.copy()
        augmented_row['weight'] += random.uniform(-2, 2)
        augmented_row['height'] += random.uniform(-2, 2)
        augmented_row['temperature'] += random.uniform(-2, 2)
        augmented_row['takeTime'] += random.uniform(-2, 2)
        augmented_rows.append(augmented_row)
    return augmented_rows

augmented_data = []

for index, row in data.iterrows():
    augmented_data.extend(augment_data(row))

augmented_df = pd.DataFrame(augmented_data)

combined_data = pd.concat([data, augmented_df], ignore_index=True)

combined_df = pd.DataFrame(combined_data)

data = np.array(combined_df)
X = data[:, :-1]
y = data[:, -1]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

regressor = DecisionTreeRegressor(
)

regressor.fit(X_train, y_train)

y_pred = regressor.predict(X_test)

print("Mean Squared Error:", mean_squared_error(y_test, y_pred))

joblib.dump(regressor, "temp.pkl")
