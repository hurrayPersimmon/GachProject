import pandas as pd
import random
import sys

import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import torch
from torch.utils.data import TensorDataset, DataLoader
import torch.nn as nn
import torch.optim as optim
from sklearn.metrics import mean_absolute_error

localSavePath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Model/temp.pt"
savePath = "/home/t24102/GachProject/AI/Model/temp.pt"
csvPath = "/home/t24102/GachProject/AI/Data/data.csv"
localCsvPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Data/data.csv"
localAugCsvPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Data/augment_data.csv"
augCsvPath = "/home/t24102/GachProject/AI/Data/augment_data.csv"

def augment_data(row):
    augmented_rows = []
    for _ in range(5):
        augmented_row = row.copy()
        augmented_row['weight'] += random.uniform(-1, 1)
        augmented_row['height'] += random.uniform(-1, 1)
        augmented_row['temperature'] += random.uniform(-1, 1)
        augmented_rows.append(augmented_row)
    return augmented_rows

# 변경 지점
data = pd.read_csv(csvPath, encoding='UTF-8')

augmented_data = []
for index, row in data.iterrows():
    augmented_data.extend(augment_data(row))

augmented_df = pd.DataFrame(augmented_data)

combined_data = pd.concat([data, augmented_df], ignore_index=True)

combined_df = pd.DataFrame(combined_data)

columns_to_drop = ['dataId', 'node1', 'node2']
combined_df.drop(columns_to_drop, axis=1, inplace=True)

# 변경 지점
combined_df.to_csv(augCsvPath, index=False)

# 변경 지점
csv_file = augCsvPath

df = pd.read_csv(csv_file)

X = df.drop('takeTime', axis=1).values
y = df['takeTime'].values

# 데이터 표준화
scaler = StandardScaler()
X = scaler.fit_transform(X)

X_train, X_val, y_train, y_val = train_test_split(X, y, test_size=0.2, random_state=42)
# LSTM 모델 정의
class LSTMModel(nn.Module):
    def __init__(self, input_size, hidden_size, num_layers, output_size):
        super(LSTMModel, self).__init__()
        self.hidden_size = hidden_size
        self.num_layers = num_layers
        self.lstm = nn.LSTM(input_size, hidden_size, num_layers, batch_first=True)
        self.fc = nn.Linear(hidden_size, output_size)

    def forward(self, x):
        x = x.unsqueeze(1)
        h0 = torch.zeros(self.num_layers, x.size(0), self.hidden_size).to(x.device)
        c0 = torch.zeros(self.num_layers, x.size(0), self.hidden_size).to(x.device)
        out, _ = self.lstm(x, (h0, c0))
        out = self.fc(out[:, -1, :])
        return out

input_size = X_train.shape[1]
hidden_size = int(sys.argv[1])
num_layers = int(sys.argv[3])
output_size = 1
num_epochs = int(sys.argv[2])
learning_rate = float(sys.argv[4])

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

model = LSTMModel(input_size, hidden_size, num_layers, output_size).to(device)

X_train_tensor = torch.tensor(X_train, dtype=torch.float32).to(device)
y_train_tensor = torch.tensor(y_train, dtype=torch.float32).to(device)
X_val_tensor = torch.tensor(X_val, dtype=torch.float32).to(device)
y_val_tensor = torch.tensor(y_val, dtype=torch.float32).to(device)

batch_size = int(sys.argv[5])

train_dataset = TensorDataset(X_train_tensor, y_train_tensor)
train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
val_dataset = TensorDataset(X_val_tensor, y_val_tensor)
val_loader = DataLoader(val_dataset, batch_size=batch_size)

criterion = nn.L1Loss()
optimizer = optim.Adam(model.parameters(), lr=learning_rate)

for epoch in range(num_epochs):
    model.train()
    for inputs, targets in train_loader:
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs.squeeze(), targets)
        loss.backward()
        optimizer.step()

    model.eval()
    with torch.no_grad():
        val_loss = 0
        for inputs, targets in val_loader:
            outputs = model(inputs)
            val_loss += criterion(outputs.squeeze(), targets).item()

    print(f'{epoch+1} {val_loss/len(val_loader):.4f},')

with torch.no_grad():
    predictions = []
    targets_list = []
    for inputs, targets in val_loader:
        outputs = model(inputs)
        predictions.extend(outputs.squeeze().tolist())
        targets_list.extend(targets.tolist())

val_mae = mean_absolute_error(targets_list, predictions)
print(f'{val_mae:.4f}')

# 변경 지점
torch.save(model.state_dict(), savePath)
