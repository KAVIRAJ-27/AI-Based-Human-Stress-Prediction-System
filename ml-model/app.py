"""
Stress Prediction ML API - Flask Server
Model: Random Forest (88.64% accuracy)
"""

from flask import Flask, request, jsonify
import joblib
import numpy as np
import json
import os

app = Flask(__name__)

# Load model and scaler
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
model = joblib.load(os.path.join(BASE_DIR, 'stress_model.pkl'))
scaler = joblib.load(os.path.join(BASE_DIR, 'scaler.pkl'))

with open(os.path.join(BASE_DIR, 'model_meta.json')) as f:
    meta = json.load(f)

FEATURES = meta['features']
LABELS = {0: 'Low', 1: 'Medium', 2: 'High'}

def add_cors_headers(response):
    response.headers['Access-Control-Allow-Origin'] = '*'
    response.headers['Access-Control-Allow-Headers'] = 'Content-Type,Authorization'
    response.headers['Access-Control-Allow-Methods'] = 'GET,POST,OPTIONS'
    return response

@app.after_request
def after_request(response):
    return add_cors_headers(response)

@app.route('/health', methods=['GET'])
def health():
    return jsonify({'status': 'ok', 'model': 'RandomForest', 'accuracy': meta['accuracy']})

@app.route('/predict', methods=['POST', 'OPTIONS'])
def predict():
    if request.method == 'OPTIONS':
        return jsonify({}), 200

    data = request.get_json()
    if not data:
        return jsonify({'error': 'No input data provided'}), 400

    try:
        # Build feature vector in correct order
        feature_vector = []
        for feat in FEATURES:
            val = data.get(feat)
            if val is None:
                return jsonify({'error': f'Missing feature: {feat}'}), 400
            feature_vector.append(float(val))

        X = np.array([feature_vector])
        X_scaled = scaler.transform(X)

        prediction = int(model.predict(X_scaled)[0])
        probabilities = model.predict_proba(X_scaled)[0].tolist()
        confidence = max(probabilities)

        # Feature importance for this prediction
        feature_impact = {
            feat: round(meta['feature_importance'][feat] * 100, 2)
            for feat in FEATURES
        }
        top_factors = sorted(feature_impact.items(), key=lambda x: x[1], reverse=True)[:5]

        return jsonify({
            'stress_level': prediction,
            'stress_label': LABELS[prediction],
            'confidence': round(confidence * 100, 2),
            'probabilities': {
                'Low': round(probabilities[0] * 100, 2),
                'Medium': round(probabilities[1] * 100, 2),
                'High': round(probabilities[2] * 100, 2)
            },
            'top_contributing_factors': dict(top_factors),
            'recommendations': get_recommendations(prediction, data)
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/batch-predict', methods=['POST', 'OPTIONS'])
def batch_predict():
    if request.method == 'OPTIONS':
        return jsonify({}), 200

    data = request.get_json()
    records = data.get('records', [])
    if not records:
        return jsonify({'error': 'No records provided'}), 400

    results = []
    for record in records:
        try:
            fv = [float(record.get(f, 0)) for f in FEATURES]
            X_scaled = scaler.transform([fv])
            pred = int(model.predict(X_scaled)[0])
            proba = model.predict_proba(X_scaled)[0].tolist()
            results.append({
                'stress_level': pred,
                'stress_label': LABELS[pred],
                'confidence': round(max(proba) * 100, 2)
            })
        except Exception as e:
            results.append({'error': str(e)})

    return jsonify({'results': results, 'total': len(results)})


@app.route('/model-info', methods=['GET'])
def model_info():
    return jsonify({
        'model_type': 'Random Forest Classifier',
        'accuracy': meta['accuracy'],
        'cv_score': meta['cv_score'],
        'features': FEATURES,
        'feature_importance': meta['feature_importance'],
        'stress_classes': LABELS,
        'training_samples': 880,
        'test_samples': 220
    })


def get_recommendations(stress_level, data):
    recs = []
    if stress_level == 0:
        recs = [
            "Great! Your stress levels are low. Maintain your current healthy habits.",
            "Continue regular exercise and good sleep patterns.",
            "Keep practicing mindfulness and social connections."
        ]
    elif stress_level == 1:
        tips = []
        if float(data.get('sleep_quality', 5)) < 5:
            tips.append("Improve sleep quality — aim for 7-8 hours per night.")
        if float(data.get('anxiety_level', 10)) > 12:
            tips.append("Practice deep breathing or meditation to reduce anxiety.")
        if float(data.get('social_support', 5)) < 4:
            tips.append("Strengthen social connections and seek peer support.")
        if float(data.get('breathing_problem', 0)) > 2:
            tips.append("Try breathing exercises like 4-7-8 technique.")
        recs = tips if tips else [
            "Consider stress management techniques like yoga or journaling.",
            "Maintain a balanced study/work schedule.",
            "Reach out to friends or a counselor if needed."
        ]
    else:  # High
        tips = []
        if float(data.get('anxiety_level', 10)) > 15:
            tips.append("Seek professional help for anxiety management immediately.")
        if float(data.get('depression', 10)) > 15:
            tips.append("Consult a mental health professional about depression symptoms.")
        if float(data.get('sleep_quality', 5)) < 3:
            tips.append("Address sleep issues urgently — consider speaking with a doctor.")
        tips.append("Reduce study/work load temporarily and prioritize self-care.")
        tips.append("Contact a counselor or therapist for professional support.")
        recs = tips

    return recs


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
