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

print("첫번째 줄", flush=True)
def augment_data(row):
    augmented_rows = []
    for _ in range(5):  # 각 데이터마다 3개의 증식된 데이터 생성
        augmented_row = row.copy()
        augmented_row['weight'] += random.uniform(-1, 1)
        augmented_row['height'] += random.uniform(-1, 1)
        augmented_row['temperature'] += random.uniform(-1, 1)
        augmented_rows.append(augmented_row)
    return augmented_rows

data = pd.read_csv('/Users/nomyeongjun/Documents/2024-1/Project/GachProject/data.csv', encoding='UTF-8')
print("증식 성공", flush=True)
augmented_data = []
for index, row in data.iterrows():
    augmented_data.extend(augment_data(row))
print("여기도 됨", flush=True)
# 증식된 데이터를 데이터프레임으로 변환
augmented_df = pd.DataFrame(augmented_data)

# 원본 데이터와 증식된 데이터 결합
combined_data = pd.concat([data, augmented_df], ignore_index=True)

# 증식된 데이터를 데이터프레임으로 변환
combined_df = pd.DataFrame(combined_data)

# 데이터프레임에서 'dataId', 'node1', 'node2', 'node1_altitude', 'node2_altitude' 열 삭제
columns_to_drop = ['dataId', 'node1', 'node2']
combined_df.drop(columns_to_drop, axis=1, inplace=True)

combined_df.to_csv('augment_data.csv', index=False)

csv_file = 'augment_data.csv'

# CSV 파일 열 이름 확인
df = pd.read_csv(csv_file)
print(df.columns, flush=True)

# 독립 변수와 종속 변수 선택
X = df.drop('takeTime', axis=1).values
y = df['takeTime'].values

# 데이터 표준화
scaler = StandardScaler()
X = scaler.fit_transform(X)

X_train, X_val, y_train, y_val = train_test_split(X, y, test_size=0.2, random_state=42)
print("모델 정의 전 단계", flush=True)
# LSTM 모델 정의
class LSTMModel(nn.Module):
    def __init__(self, input_size, hidden_size, num_layers, output_size):
        super(LSTMModel, self).__init__()
        self.hidden_size = hidden_size
        self.num_layers = num_layers
        self.lstm = nn.LSTM(input_size, hidden_size, num_layers, batch_first=True)
        self.fc = nn.Linear(hidden_size, output_size)

    def forward(self, x):
        # LSTM 레이어의 입력은 (batch_size, seq_len, input_size) 형태여야 함
        # 현재는 (batch_size, input_size) 형태이므로 차원을 확장해야 함
        x = x.unsqueeze(1)  # seq_len 차원 추가
        # 초기 은닉 상태 및 셀 상태 생성
        h0 = torch.zeros(self.num_layers, x.size(0), self.hidden_size).to(x.device)
        c0 = torch.zeros(self.num_layers, x.size(0), self.hidden_size).to(x.device)
        out, _ = self.lstm(x, (h0, c0))
        out = self.fc(out[:, -1, :])
        return out


# 하이퍼파라미터 설정
input_size = X_train.shape[1]
print(X_train.shape[1])
hidden_size = int(sys.argv[1])
num_layers = int(sys.argv[3])
output_size = 1
num_epochs = int(sys.argv[2])
learning_rate = float(sys.argv[4])


device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
print(f"device: {device}")


# 모델 초기화
model = LSTMModel(input_size, hidden_size, num_layers, output_size).to(device)

# PyTorch용 Tensor로 변환 및 MPS로 이동
X_train_tensor = torch.tensor(X_train, dtype=torch.float32).to(device)
y_train_tensor = torch.tensor(y_train, dtype=torch.float32).to(device)
X_val_tensor = torch.tensor(X_val, dtype=torch.float32).to(device)
y_val_tensor = torch.tensor(y_val, dtype=torch.float32).to(device)

# Dataset 및 DataLoader 생성
train_dataset = TensorDataset(X_train_tensor, y_train_tensor)
train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)
val_dataset = TensorDataset(X_val_tensor, y_val_tensor)
val_loader = DataLoader(val_dataset, batch_size=32)

# 손실 함수 및 옵티마이저 설정
criterion = nn.L1Loss()
optimizer = optim.Adam(model.parameters(), lr=learning_rate)

# 모델 학습
for epoch in range(num_epochs):
    model.train()
    for inputs, targets in train_loader:
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs.squeeze(), targets)
        loss.backward()
        optimizer.step()

    # 검증 데이터를 사용하여 모델 평가
    model.eval()
    with torch.no_grad():
        val_loss = 0
        for inputs, targets in val_loader:
            outputs = model(inputs)
            val_loss += criterion(outputs.squeeze(), targets).item()

    print(f'Epoch [{epoch+1}/{num_epochs}], Validation Loss: {val_loss/len(val_loader):.4f}')


# 검증 데이터를 사용하여 MAE 계산
with torch.no_grad():
    predictions = []
    targets_list = []
    for inputs, targets in val_loader:
        outputs = model(inputs)
        predictions.extend(outputs.squeeze().tolist())
        targets_list.extend(targets.tolist())

val_mae = mean_absolute_error(targets_list, predictions)
print(f'Validation MAE: {val_mae:.4f}')

# 모델 저장
torch.save(model.state_dict(), '/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Model/second_model.pt')
