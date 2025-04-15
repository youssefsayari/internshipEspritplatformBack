import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import OneHotEncoder
from sklearn.linear_model import LogisticRegression
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn2pmml import PMMLPipeline, sklearn2pmml

# 1. Charger le dataset (le fichier doit être dans le même dossier que ce script)
df = pd.read_excel("internship_dataset_realistic.xlsx")

# 2. Sélectionner les colonnes utiles (on enlève 'Class')
X = df[['Option', 'Internship Subject', 'Company']]
y = df['Accepted']

# 3. Pipeline : encodage OneHot + modèle Logistic Regression
column_transformer = ColumnTransformer(
    [("cat", OneHotEncoder(), ['Option', 'Internship Subject', 'Company'])]
)

pipeline = PMMLPipeline([
    ("preprocessor", column_transformer),
    ("classifier", LogisticRegression(max_iter=1000))
])

# 4. Entraîner le modèle
pipeline.fit(X, y)

# 5. Exporter en .pmml
sklearn2pmml(pipeline, "hiring_predictor_model.pmml", with_repr=True)

print("✅ Modèle exporté avec succès → hiring_predictor_model.pmml")
