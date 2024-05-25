import numpy as np
from sklearn.tree import DecisionTreeRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
import pandas as pd
import random
import joblib
import sys

# 이전의 저장된 모델 경로는 어디에 있는지
saved_model_path = sys.argv[1]
# 추가 CSV 데이터 경로는 어디에 있는지
additional_data_path = sys.argv[2]

# 저장된 모델 로드
regressor = joblib.load(saved_model_path)

# 추가 데이터 로드
additional_data = pd.read_csv(additional_data_path, encoding='UTF-8')

# 데이터 전처리 및 학습 데이터 준비
X_add = additional_data.iloc[:, :-1]
y_add = additional_data.iloc[:, -1]

X_train, X_test, y_train, y_test = train_test_split(X_add, y_add, test_size=0.2, random_state=42)


# 모델에 추가 데이터로 재학습
regressor.fit(X_train, y_train)

# 예측 결과 확인 등 필요한 작업 수행
# 예를 들어, 테스트 데이터를 이용하여 모델 성능 평가
# X_test, y_test 는 새로운 테스트 데이터로 변경해야 함
y_pred = regressor.predict(X_test)
print("Mean Squared Error:", mean_squared_error(y_test, y_pred))

# 모델 재학습 후 저장 (선택 사항)
joblib.dump(regressor, saved_model_path)