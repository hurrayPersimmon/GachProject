# 필요한 라이브러리 import
import pandas as pd
import torch
from torch.utils.data import TensorDataset, DataLoader
import torch.nn as nn
import torch.optim as optim
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import sys

# 입력 값 파싱
num_epochs_new = int(sys.argv[1])
learning_rate_new = float(sys.argv[2])
model_path = sys.argv[3]

# 데이터 불러오기
csv_file = 're_data.csv'
df = pd.read_csv(csv_file)

# 데이터 전처리
X = df.drop('takeTime', axis=1).values
y = df['takeTime'].values
scaler = StandardScaler()
X = scaler.fit_transform(X)

# 데이터를 텐서로 변환하고 TensorDataset으로 묶기
X_tensor = torch.tensor(X, dtype=torch.float32)
y_tensor = torch.tensor(y, dtype=torch.float32)
dataset = TensorDataset(X_tensor, y_tensor)

# 데이터 로더 생성
batch_size = 32
loader = DataLoader(dataset, batch_size=batch_size, shuffle=True)

# 모델 불러오기
model = torch.load(model_path)

# 새로운 optimizer 설정
optimizer = optim.Adam(model.parameters(), lr=learning_rate_new)

# 손실 함수
criterion = nn.L1Loss()

# 재학습
model.train()
for epoch in range(num_epochs_new):
    epoch_loss = 0.0
    for inputs, targets in loader:
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs.squeeze(), targets)
        loss.backward()
        optimizer.step()
        epoch_loss += loss.item()

    print(f'Epoch [{epoch+1}/{num_epochs_new}], Loss: {epoch_loss/len(loader):.4f}')

# 모델 저장
torch.save(model, model_path)
