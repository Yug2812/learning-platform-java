from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
import joblib
import pandas as pd
import os

app = FastAPI(title="Career Prediction Service")

MODEL_PATH = "model.joblib"

# Load the model if it exists
try:
    if os.path.exists(MODEL_PATH):
        model = joblib.load(MODEL_PATH)
    else:
        model = None
except Exception as e:
    model = None
    print(f"Error loading model: {e}")

class PredictionInput(BaseModel):
    math_score: int = Field(..., ge=0, le=100, description="Math score between 0 and 100")
    physics_score: int = Field(..., ge=0, le=100, description="Physics score between 0 and 100")
    logic_score: int = Field(..., ge=0, le=100, description="Logic score between 0 and 100")
    interest_score: int = Field(..., ge=0, le=100, description="Interest score between 0 and 100")

class PredictionOutput(BaseModel):
    predicted_field: str

@app.post("/predict", response_model=PredictionOutput)
def predict_career(input_data: PredictionInput):
    # Try to reload if it wasn't available at startup but might be now
    global model
    if model is None:
        if os.path.exists(MODEL_PATH):
            model = joblib.load(MODEL_PATH)
        else:
            raise HTTPException(status_code=500, detail="Model file not found. Please run train_model.py first.")

    try:
        # Create a dataframe for the input data since scikit-learn expects feature names
        input_df = pd.DataFrame([{
            "math_score": input_data.math_score,
            "physics_score": input_data.physics_score,
            "logic_score": input_data.logic_score,
            "interest_score": input_data.interest_score
        }])
        
        # Predict using the loaded model
        prediction = model.predict(input_df)
        
        return {"predicted_field": prediction[0]}
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Prediction error: {str(e)}")
