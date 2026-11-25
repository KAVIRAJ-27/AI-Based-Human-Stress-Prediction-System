"""
Unit tests for StressGuard ML Service Flask API
"""
import unittest
import json
from app import app, FEATURES

class MLAppTestCase(unittest.TestCase):
    def setUp(self):
        app.config['TESTING'] = True
        self.client = app.test_client()

    def test_health_endpoint(self):
        response = self.client.get('/health')
        self.assertEqual(response.status_code, 200)
        data = json.loads(response.data)
        self.assertEqual(data.get('status'), 'ok')
        self.assertEqual(data.get('service'), 'stressguard-ml-service')
        self.assertTrue(data.get('model_loaded'))
        self.assertTrue(data.get('scaler_loaded'))

    def test_model_info_endpoint(self):
        response = self.client.get('/model-info')
        self.assertEqual(response.status_code, 200)
        data = json.loads(response.data)
        self.assertIn('features', data)
        self.assertEqual(len(data['features']), 20)

    def test_prediction_endpoint_valid_input(self):
        sample_payload = {f: 2 for f in FEATURES}
        sample_payload['anxiety_level'] = 15
        sample_payload['depression'] = 18

        response = self.client.post(
            '/predict',
            data=json.dumps(sample_payload),
            content_type='application/json'
        )
        self.assertEqual(response.status_code, 200)
        data = json.loads(response.data)
        self.assertIn('stress_level', data)
        self.assertIn('stress_label', data)
        self.assertIn('confidence', data)
        self.assertIn('probabilities', data)
        self.assertIn('recommendations', data)
        self.assertIn('top_contributing_factors', data)

    def test_prediction_endpoint_missing_feature(self):
        invalid_payload = {'anxiety_level': 10}
        response = self.client.post(
            '/predict',
            data=json.dumps(invalid_payload),
            content_type='application/json'
        )
        self.assertEqual(response.status_code, 400)
        data = json.loads(response.data)
        self.assertIn('error', data)

    def test_batch_prediction_endpoint(self):
        records = [{f: 1 for f in FEATURES}, {f: 4 for f in FEATURES}]
        response = self.client.post(
            '/batch-predict',
            data=json.dumps({'records': records}),
            content_type='application/json'
        )
        self.assertEqual(response.status_code, 200)
        data = json.loads(response.data)
        self.assertEqual(data.get('total'), 2)
        self.assertEqual(len(data.get('results')), 2)

if __name__ == '__main__':
    unittest.main()
