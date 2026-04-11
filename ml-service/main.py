from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
import joblib
import os

app = FastAPI(title="AI Career Prediction Service")

# Locate and specify the model early
MODEL_PATH = "model.joblib"
model = None

# We use lifecycle startup event to load model safely
@app.on_event("startup")
def load_model():
    global model
    if os.path.exists(MODEL_PATH):
        model = joblib.load(MODEL_PATH)
        print("Model loaded successfully.")
    else:
        print(f"Warning: Model file not found at {MODEL_PATH}. Make sure to run train_model.py first.")

# Define strictly verified boundary metrics for Score input
class PredictionInput(BaseModel):
    math_score: int = Field(..., ge=0, le=100, description="Math score must be between 0 and 100")
    physics_score: int = Field(..., ge=0, le=100, description="Physics score must be between 0 and 100")
    logic_score: int = Field(..., ge=0, le=100, description="Logic score must be between 0 and 100")
    interest_score: int = Field(..., ge=0, le=100, description="Interest score must be between 0 and 100")

class PredictionOutput(BaseModel):
    predicted_field: str

@app.post("/predict", response_model=PredictionOutput)
def predict_career(data: PredictionInput):
    if model is None:
        raise HTTPException(status_code=503, detail="Model is not loaded or currently unavailable.")
    
    try:
        features = [[data.math_score, data.physics_score, data.logic_score, data.interest_score]]
        prediction = model.predict(features)[0]
        
        return {"predicted_field": prediction}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
