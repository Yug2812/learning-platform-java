import pandas as pd
from sklearn.tree import DecisionTreeClassifier
import joblib

def main():
    # 1. Create synthetic dataset
    data = [
        # High math + logic -> Computer Engineering
        {"math_score": 90, "physics_score": 60, "logic_score": 85, "interest_score": 75, "field": "Computer Engineering"},
        {"math_score": 85, "physics_score": 65, "logic_score": 90, "interest_score": 80, "field": "Computer Engineering"},
        {"math_score": 95, "physics_score": 50, "logic_score": 88, "interest_score": 70, "field": "Computer Engineering"},
        {"math_score": 88, "physics_score": 55, "logic_score": 89, "interest_score": 90, "field": "Computer Engineering"},
        {"math_score": 92, "physics_score": 65, "logic_score": 95, "interest_score": 85, "field": "Computer Engineering"},
        {"math_score": 89, "physics_score": 60, "logic_score": 86, "interest_score": 78, "field": "Computer Engineering"},
        {"math_score": 91, "physics_score": 62, "logic_score": 87, "interest_score": 76, "field": "Computer Engineering"},
        {"math_score": 86, "physics_score": 58, "logic_score": 92, "interest_score": 82, "field": "Computer Engineering"},
        {"math_score": 94, "physics_score": 55, "logic_score": 94, "interest_score": 80, "field": "Computer Engineering"},
        {"math_score": 87, "physics_score": 68, "logic_score": 88, "interest_score": 85, "field": "Computer Engineering"},
        # Explicit test case from instructions
        {"math_score": 80, "physics_score": 70, "logic_score": 85, "interest_score": 75, "field": "Computer Engineering"},

        # High physics -> Mechanical Engineering
        {"math_score": 70, "physics_score": 90, "logic_score": 65, "interest_score": 80, "field": "Mechanical Engineering"},
        {"math_score": 65, "physics_score": 95, "logic_score": 60, "interest_score": 85, "field": "Mechanical Engineering"},
        {"math_score": 75, "physics_score": 88, "logic_score": 70, "interest_score": 75, "field": "Mechanical Engineering"},
        {"math_score": 60, "physics_score": 92, "logic_score": 65, "interest_score": 90, "field": "Mechanical Engineering"},
        {"math_score": 68, "physics_score": 89, "logic_score": 62, "interest_score": 88, "field": "Mechanical Engineering"},
        {"math_score": 72, "physics_score": 91, "logic_score": 68, "interest_score": 81, "field": "Mechanical Engineering"},
        {"math_score": 66, "physics_score": 96, "logic_score": 61, "interest_score": 82, "field": "Mechanical Engineering"},
        {"math_score": 78, "physics_score": 87, "logic_score": 72, "interest_score": 77, "field": "Mechanical Engineering"},
        {"math_score": 62, "physics_score": 94, "logic_score": 67, "interest_score": 84, "field": "Mechanical Engineering"},
        {"math_score": 69, "physics_score": 85, "logic_score": 64, "interest_score": 86, "field": "Mechanical Engineering"},

        # Low scores -> Civil Engineering
        {"math_score": 40, "physics_score": 45, "logic_score": 40, "interest_score": 50, "field": "Civil Engineering"},
        {"math_score": 45, "physics_score": 40, "logic_score": 45, "interest_score": 45, "field": "Civil Engineering"},
        {"math_score": 35, "physics_score": 50, "logic_score": 32, "interest_score": 35, "field": "Civil Engineering"},
        {"math_score": 50, "physics_score": 35, "logic_score": 48, "interest_score": 55, "field": "Civil Engineering"},
        {"math_score": 38, "physics_score": 42, "logic_score": 36, "interest_score": 40, "field": "Civil Engineering"},
        {"math_score": 42, "physics_score": 38, "logic_score": 45, "interest_score": 48, "field": "Civil Engineering"},
        {"math_score": 48, "physics_score": 46, "logic_score": 35, "interest_score": 52, "field": "Civil Engineering"},
        {"math_score": 36, "physics_score": 44, "logic_score": 41, "interest_score": 46, "field": "Civil Engineering"},
        {"math_score": 44, "physics_score": 41, "logic_score": 44, "interest_score": 49, "field": "Civil Engineering"},
        {"math_score": 41, "physics_score": 39, "logic_score": 39, "interest_score": 44, "field": "Civil Engineering"},

        # Balanced scores -> Electronics Engineering
        {"math_score": 70, "physics_score": 75, "logic_score": 70, "interest_score": 75, "field": "Electronics Engineering"},
        {"math_score": 75, "physics_score": 70, "logic_score": 75, "interest_score": 70, "field": "Electronics Engineering"},
        {"math_score": 72, "physics_score": 72, "logic_score": 72, "interest_score": 72, "field": "Electronics Engineering"},
        {"math_score": 68, "physics_score": 68, "logic_score": 68, "interest_score": 68, "field": "Electronics Engineering"},
        {"math_score": 73, "physics_score": 74, "logic_score": 71, "interest_score": 76, "field": "Electronics Engineering"},
        {"math_score": 76, "physics_score": 71, "logic_score": 74, "interest_score": 69, "field": "Electronics Engineering"},
        {"math_score": 71, "physics_score": 73, "logic_score": 73, "interest_score": 71, "field": "Electronics Engineering"},
        {"math_score": 69, "physics_score": 69, "logic_score": 69, "interest_score": 69, "field": "Electronics Engineering"},
        {"math_score": 74, "physics_score": 76, "logic_score": 72, "interest_score": 74, "field": "Electronics Engineering"},
        {"math_score": 67, "physics_score": 67, "logic_score": 67, "interest_score": 67, "field": "Electronics Engineering"},
    ]

    df = pd.DataFrame(data)

    # 2. Features and target
    X = df[["math_score", "physics_score", "logic_score", "interest_score"]]
    y = df["field"]

    # 3. Train model
    clf = DecisionTreeClassifier(random_state=42)
    clf.fit(X, y)

    # 4. Save model
    joblib.dump(clf, "model.joblib")
    print("Model created, trained, and saved to model.joblib successfully. Dataset size:", len(df))

if __name__ == "__main__":
    main()
