import pandas as pd
import xgboost as xgb
import joblib
from sklearn.preprocessing import LabelEncoder

# 1. Charger les données
df = pd.read_csv("Updated_Startup_TunisieC_DSO1.csv")

# 2. Préparation
df['Taille'] = df['Taille'].apply(lambda x: 1 if x == 'Startup' else 0)  # Startup → 1, Non-Startup → 0

# Encoder 'Secteur'
le_secteur = LabelEncoder()
df['Secteur'] = le_secteur.fit_transform(df['Secteur'])

# 3. Features et target
X = df[['Secteur', 'Annee de creation', 'NombreEmployes', 'Est_Tech', 'Dynamisme']]
y = df['Taille']

# 4. Entraînement du modèle
model = xgb.XGBClassifier(use_label_encoder=False, eval_metric='logloss')
model.fit(X, y)


# Sauvegarder le modèle et l’encodeur
joblib.dump(model, "xgboost_model.pkl")
joblib.dump(le_secteur, "label_encoder_secteur.pkl")

print("✅ Modèle et encodeur sauvegardés avec succès.")
