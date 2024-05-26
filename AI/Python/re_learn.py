import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
import pandas as pd
import joblib
import sys

# 이전의 저장된 모델 경로는 어디에 있는지
saved_model_path = sys.argv[1]
# 추가 CSV 데이터 경로는 어디에 있는지
additional_data_path = sys.argv[2]

new_model_path = sys.argv[3]

# 저장된 모델 로드
regressor = joblib.load(saved_model_path)

# 추가 데이터 로드
additional_data = pd.read_csv(additional_data_path, encoding='UTF-8')

data = pd.DataFrame(additional_data)

# 데이터 전처리 및 학습 데이터 준비
X_add = data.iloc[:, :-1]
y_add = data.iloc[:, -1]

X_train, X_test, y_train, y_test = train_test_split(X_add, y_add, test_size=0.2, random_state=42)

regressor.fit(X_train, y_train)

# 왜 MSE가 0으로 나오는지
y_pred = regressor.predict(X_test)
print(mean_squared_error(y_test, y_pred))

# 모델 재학습 후 저장 (선택 사항)
joblib.dump(regressor, new_model_path)