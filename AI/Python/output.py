import torch
import torch.nn as nn
import sys
import numpy as np

from torch.utils.data import DataLoader

# 모델 정의
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

input_size = 10
hidden_size = 30
num_layers = 30
output_size = 1

# 모델 불러오기
loaded_model = LSTMModel(input_size, hidden_size, num_layers, output_size)
loaded_model.load_state_dict(torch.load('/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Model/second_model.pt'))
loaded_model.eval()

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

# 입력 데이터 준비
# 예를 들어, X_test_tensor를 사용한다고 가정
# X_test_tensor는 테스트용 데이터를 PyTorch Tensor로 변환한 것입니다.
# 적절한 입력 데이터를 준비해야 합니다.
# 입력 데이터 준비
input_data = [birthYear, gender, height, weight, walkSpeed, temperature, precipitationProbability, precipitation, weightShortest, weightOptimal]

# 리스트를 PyTorch 텐서로 변환
X_test_tensor = torch.tensor(input_data, dtype=torch.float32).unsqueeze(0)  # 배치 차원을 추가하여 3D 텐서로 변환

# 모델에 입력 데이터 전달하여 출력 데이터 얻기
with torch.no_grad():
    output_data = loaded_model(X_test_tensor)

# 출력 데이터 확인
print(output_data)
