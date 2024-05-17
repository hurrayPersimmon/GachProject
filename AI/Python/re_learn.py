# 필요한 라이브러리 import
import pandas as pd
import torch
from torch.utils.data import TensorDataset, DataLoader
import torch.nn as nn
import torch.optim as optim
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import mean_absolute_error
import sys

# 새로운 epochs와 learning_rate 입력 받기
num_epochs_new = int(sys.argv[1])
learning_rate_new = float(sys.argv[2])

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
print(f"device: {device}")

# 'augmentation.csv' 파일 불러오기
csv_file = 'augment_data.csv'
df = pd.read_csv(csv_file)

# 데이터 전처리
X = df.drop('takeTime', axis=1).values
y = df['takeTime'].values
scaler = StandardScaler()
X = scaler.fit_transform(X)
# 데이터를 텐서로 변환하고 TensorDataset으로 묶기
X_train, X_val, y_train, y_val = train_test_split(X, y, test_size=0.2, random_state=42)
X_train_tensor = torch.tensor(X_train, dtype=torch.float32).to(device)
y_train_tensor = torch.tensor(y_train, dtype=torch.float32).to(device)
X_val_tensor = torch.tensor(X_val, dtype=torch.float32).to(device)
y_val_tensor = torch.tensor(y_val, dtype=torch.float32).to(device)

train_dataset = TensorDataset(X_train_tensor, y_train_tensor)
val_dataset = TensorDataset(X_val_tensor, y_val_tensor)

# 데이터 로더 생성
batch_size = 32
train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
val_loader = DataLoader(val_dataset, batch_size=batch_size)

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

input_size = X_train.shape[1]
hidden_size = 10
num_layers = 10
output_size = 1  # 출력 크기 (이 예제에서는 회귀 문제이므로 1)

# 모델 초기화
model = LSTMModel(input_size, hidden_size, num_layers, output_size).to(device)

criterion = nn.L1Loss()
# 새로운 optimizer 설정
optimizer = optim.Adam(model.parameters(), lr=learning_rate_new)

# 모델 재학습
for epoch in range(num_epochs_new):
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

    print(f'Epoch [{epoch+1}/{num_epochs_new}], Validation Loss: {val_loss/len(val_loader):.4f}')

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
torch.save(model.state_dict(), 'retrained_model.pt')
