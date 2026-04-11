import pandas as pd
from sklearn.tree import DecisionTreeClassifier
import joblib

def synthesize_dataset():
    data = []
    
    # Let's generate a robust synthetic set of roughly 30-40 rows
    
    # 1. Computer Engineering (High Math & Logic)
    for _ in range(10):
        data.append({"math_score": 85, "physics_score": 60, "logic_score": 90, "interest_score": 80, "field": "Computer Engineering"})
        data.append({"math_score": 95, "physics_score": 75, "logic_score": 85, "interest_score": 70, "field": "Computer Engineering"})
    
    # 2. Mechanical Engineering (High Physics)
    for _ in range(10):
        data.append({"math_score": 70, "physics_score": 90, "logic_score": 65, "interest_score": 85, "field": "Mechanical Engineering"})
        data.append({"math_score": 65, "physics_score": 95, "logic_score": 60, "interest_score": 80, "field": "Mechanical Engineering"})
        
    # 3. Civil Engineering (Lower overall, maybe physically inclined or practical)
    for _ in range(10):
        data.append({"math_score": 55, "physics_score": 50, "logic_score": 50, "interest_score": 80, "field": "Civil Engineering"})
        data.append({"math_score": 60, "physics_score": 55, "logic_score": 45, "interest_score": 75, "field": "Civil Engineering"})
        
    # 4. Electronics Engineering (Mixed Scores / High Math + Physics but lower Logic)
    for _ in range(10):
        data.append({"math_score": 80, "physics_score": 85, " logic_score": 60, "interest_score": 75, "field": "Electronics Engineering"})
        data.append({"math_score": 75, "physics_score": 80, "logic_score": 70, "interest_score": 85, "field": "Electronics Engineering"})

    # Normalize spelling mistake generated in list comprehension above implicitly
    normalized_data = []
    for row in data:
        normalized_data.append({
            "math_score": row.get("math_score", 0),
            "physics_score": row.get("physics_score", 0),
            "logic_score": row.get("logic_score", row.get(" logic_score", 0)), 
            "interest_score": row.get("interest_score", 0),
            "field": row.get("field")
        })

    df = pd.DataFrame(normalized_data)
    return df

def train():
    print("Synthesizing dataset...")
    df = synthesize_dataset()
    
    X = df[['math_score', 'physics_score', 'logic_score', 'interest_score']]
    y = df['field']
    
    print("Training DecisionTreeClassifier...")
    clf = DecisionTreeClassifier(random_state=42)
    clf.fit(X, y)
    
    model_path = "model.joblib"
    print(f"Exporting model to {model_path}...")
    joblib.dump(clf, model_path)
    print("Model training completed successfully!")

if __name__ == "__main__":
    train()
