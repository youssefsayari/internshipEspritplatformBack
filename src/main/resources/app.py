from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import numpy as np
import pandas as pd  # manquait dans ton dernier app.py

app = FastAPI()
model = joblib.load("xgboost_model.pkl")

class InputData(BaseModel):
    option: str
    subject: str
    company: str

@app.post("/predict")
def predict(data: InputData):
    input_df = {
        "Option": [data.option.upper()],
        "Internship Subject": [data.subject.upper()],
        "Company": [data.company.upper()]
    }

    df = pd.DataFrame(input_df)
    prediction_proba = model.predict_proba(df)[0][1]
    return {"probability": round(float(prediction_proba), 4)}
