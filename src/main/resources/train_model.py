import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
import xgboost as xgb
import joblib

# 1. Charger les données
df = pd.read_excel("internship_dataset_5000_uppercase_no_accents.xlsx")
X = df[['Option', 'Internship Subject', 'Company']]
y = df['Accepted']

# 2. Pipeline : OneHot + XGBoost
column_transformer = ColumnTransformer(
    transformers=[('cat', OneHotEncoder(handle_unknown='ignore'), ['Option', 'Internship Subject', 'Company'])]
)

pipeline = Pipeline([
    ('preprocessor', column_transformer),
    ('classifier', xgb.XGBClassifier(use_label_encoder=False, eval_metric='logloss'))
])

# 3. Entraînement
pipeline.fit(X, y)

# 4. Sauvegarde du modèle
joblib.dump(pipeline, "xgboost_model.pkl")
print("✅ Modèle XGBoost exporté → xgboost_model.pkl")
