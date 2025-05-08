from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import pandas as pd  # manquait dans ton dernier app.py

app = FastAPI()

model = joblib.load("xgboost_model.pkl")
le_secteur = joblib.load("label_encoder_secteur.pkl")

# Schéma de données
class InputData(BaseModel):
    secteur: str
    annee: int
    employes: int
    est_tech: int
    dynamisme: float

# Endpoint de classification
@app.post("/classify")
def classify_entreprise(data: InputData):
    try:
        secteur = data.secteur.strip()  # on garde la casse telle qu'elle a été entraînée
        secteur_encoded = le_secteur.transform([secteur])[0]

        input_df = pd.DataFrame([{
            "Secteur": secteur_encoded,
            "Annee de creation": data.annee,
            "NombreEmployes": data.employes,
            "Est_Tech": data.est_tech,
            "Dynamisme": data.dynamisme
        }])

        prediction = model.predict(input_df)[0]
        label = "Startup" if prediction == 1 else "Non-Startup"
        return {"classification": label}
    except ValueError as e:
        return {
            "error": str(e),
            "hint": f"Secteur invalide : '{data.secteur}'. Les valeurs acceptées sont : {list(le_secteur.classes_)}"
        }