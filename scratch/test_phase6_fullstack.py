import os
import sys
import time
import json
import subprocess
import urllib.request
import urllib.error
from playwright.sync_api import sync_playwright

SCREENSHOT_DIR = r"C:\Users\KAVIRAJ\Desktop\PROJECT\AI-Human-Stress-Prediction\docs\phase6-verification\screenshots"
os.makedirs(SCREENSHOT_DIR, exist_ok=True)

test_results = []

def record(test_name, passed, details=""):
    test_results.append({
        "test": test_name,
        "status": "PASS" if passed else "FAIL",
        "details": details
    })
    print(f"[{'PASS' if passed else 'FAIL'}] {test_name}: {details}")

API_BASE = "http://localhost:8080/api"
FRONTEND_URL = "http://localhost:3000"

TEST_USERNAME = "phase6_clinician"
TEST_EMAIL = "phase6_clinician@example.com"
TEST_PASSWORD = "Password123!"

ALL_20_BIOMARKERS = [
    {"domain": "psychological", "key": "anxietyLevel", "min": 0, "max": 21},
    {"domain": "psychological", "key": "selfEsteem", "min": 0, "max": 30},
    {"domain": "psychological", "key": "mentalHealthHistory", "min": 0, "max": 1},
    {"domain": "psychological", "key": "depression", "min": 0, "max": 27},
    {"domain": "physiological", "key": "headache", "min": 0, "max": 5},
    {"domain": "physiological", "key": "bloodPressure", "min": 0, "max": 3},
    {"domain": "physiological", "key": "sleepQuality", "min": 0, "max": 9},
    {"domain": "physiological", "key": "breathingProblem", "min": 0, "max": 5},
    {"domain": "environmental", "key": "noiseLevel", "min": 0, "max": 7},
    {"domain": "environmental", "key": "livingConditions", "min": 0, "max": 5},
    {"domain": "environmental", "key": "safety", "min": 0, "max": 5},
    {"domain": "environmental", "key": "basicNeeds", "min": 0, "max": 5},
    {"domain": "academic", "key": "academicPerformance", "min": 0, "max": 5},
    {"domain": "academic", "key": "studyLoad", "min": 0, "max": 5},
    {"domain": "academic", "key": "teacherStudentRelationship", "min": 0, "max": 5},
    {"domain": "academic", "key": "futureCareerConcerns", "min": 0, "max": 5},
    {"domain": "social", "key": "socialSupport", "min": 0, "max": 3},
    {"domain": "social", "key": "peerPressure", "min": 0, "max": 5},
    {"domain": "social", "key": "extracurricularActivities", "min": 0, "max": 5},
    {"domain": "social", "key": "bullying", "min": 0, "max": 5},
]

def http_post(url, data_dict, token=None):
    data = json.dumps(data_dict).encode("utf-8")
    req = urllib.request.Request(url, data=data, method="POST")
    req.add_header("Content-Type", "application/json")
    if token:
        req.add_header("Authorization", f"Bearer {token}")
    try:
        with urllib.request.urlopen(req, timeout=10) as resp:
            return resp.status, json.loads(resp.read().decode())
    except urllib.error.HTTPError as e:
        body = e.read().decode()
        try:
            return e.code, json.loads(body)
        except Exception:
            return e.code, {"error": body}

def start_services():
    print("Starting ML service on port 5000...")
    ml_proc = subprocess.Popen([sys.executable, "ml-model/app.py"], cwd=r"C:\Users\KAVIRAJ\Desktop\PROJECT\AI-Human-Stress-Prediction")
    
    print("Starting Frontend server on port 3000...")
    front_proc = subprocess.Popen([sys.executable, "-m", "http.server", "3000", "--directory", "frontend"], cwd=r"C:\Users\KAVIRAJ\Desktop\PROJECT\AI-Human-Stress-Prediction")
    
    print("Starting Spring Boot backend on port 8080...")
    mvn_path = r"C:\Program Files\JetBrains\IntelliJ IDEA 2025.2\plugins\maven\lib\maven3\bin\mvn.cmd"
    backend_proc = subprocess.Popen([mvn_path, "spring-boot:run"], cwd=r"C:\Users\KAVIRAJ\Desktop\PROJECT\AI-Human-Stress-Prediction\backend")
    
    return [ml_proc, front_proc, backend_proc]

def wait_for_services():
    print("Waiting for ML, Frontend, and Backend services to become healthy...")
    for i in range(45):
        ml_ok = False
        front_ok = False
        back_ok = False
        try:
            with urllib.request.urlopen("http://localhost:5000/health", timeout=1) as resp:
                if json.loads(resp.read().decode()).get("status") == "ok":
                    ml_ok = True
        except Exception:
            pass
        try:
            with urllib.request.urlopen("http://localhost:3000", timeout=1) as resp:
                if resp.status == 200:
                    front_ok = True
        except Exception:
            pass
        try:
            with urllib.request.urlopen("http://localhost:8080/api/health", timeout=1) as resp:
                if json.loads(resp.read().decode()).get("status") == "UP":
                    back_ok = True
        except Exception:
            pass
        
        if ml_ok and front_ok and back_ok:
            print(f"All services online after {i+1} seconds!")
            return True
        time.sleep(1)
    raise TimeoutError("Services failed to start in 45 seconds")

def stop_services(procs):
    print("Stopping child processes and cleaning up ports...")
    for p in procs:
        try:
            p.terminate()
        except Exception:
            pass
    time.sleep(1)
    for p in procs:
        try:
            p.kill()
        except Exception:
            pass
    subprocess.run(["powershell", "-Command", "Get-NetTCPConnection -LocalPort 5000,8080,3000 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue }"], capture_output=True)

def run_tests():
    procs = []
    try:
        procs = start_services()
        wait_for_services()

        print("\n--- 1. User Registration & Setup ---")
        reg_status, reg_res = http_post(f"{API_BASE}/auth/register", {
            "username": TEST_USERNAME,
            "email": TEST_EMAIL,
            "password": TEST_PASSWORD,
            "fullName": "Phase Six Clinical Specialist",
            "age": 35,
            "gender": "Female",
            "phone": "+1-555-0166"
        })
        token = None
        if reg_status == 200:
            token = reg_res.get("token")
            record("Setup: Registered controlled test user", True)
        else:
            login_status, login_res = http_post(f"{API_BASE}/auth/login", {
                "username": TEST_USERNAME,
                "password": TEST_PASSWORD
            })
            token = login_res.get("token")
            record("Setup: Logged in existing test user", login_status == 200)

        assert token, "Could not acquire JWT token"

        print("\n--- 2. Browser Playwright Full-Stack E2E & Form Tests ---")
        with sync_playwright() as p:
            browser = p.chromium.launch(channel="msedge", headless=True)

            # -------------------------------------------------------------
            # Test 1: Desktop Viewport (1920x1080)
            # -------------------------------------------------------------
            page = browser.new_page(viewport={"width": 1920, "height": 1080})
            page.goto(FRONTEND_URL)
            page.evaluate(f"() => {{ localStorage.setItem('api_endpoint', '{API_BASE}'); localStorage.removeItem('jwt_token'); localStorage.removeItem('user_info'); }}")
            page.goto(f"{FRONTEND_URL}/#/login")
            page.reload()
            page.wait_for_timeout(500)

            # Login
            page.locator("form input").first.fill(TEST_USERNAME)
            page.locator("input[type='password']").fill(TEST_PASSWORD)
            page.click("button[type='submit']")
            page.wait_for_selector(".topbar-title", timeout=10000)

            # Navigate to Prediction assessment
            page.click("button.nav-btn:has-text('New Prediction')")
            page.wait_for_timeout(600)
            record("Navigation: Routed to #/predict", page.evaluate("window.location.hash") == "#/predict")

            # Capture Desktop Form Screenshot
            page.screenshot(path=os.path.join(SCREENSHOT_DIR, "01_desktop_assessment_form.png"), full_page=False)
            record("Responsive: Desktop 1920x1080 screenshot captured", True)

            # -------------------------------------------------------------
            # Test 2: Tablet Viewport (768x1024)
            # -------------------------------------------------------------
            page.set_viewport_size({"width": 768, "height": 1024})
            page.wait_for_timeout(500)
            page.screenshot(path=os.path.join(SCREENSHOT_DIR, "02_tablet_assessment_form.png"), full_page=False)
            record("Responsive: Tablet 768x1024 screenshot captured", True)

            # -------------------------------------------------------------
            # Test 3: Mobile Viewport (390x844)
            # -------------------------------------------------------------
            page.set_viewport_size({"width": 390, "height": 844})
            page.wait_for_timeout(500)
            page.screenshot(path=os.path.join(SCREENSHOT_DIR, "03_mobile_assessment_form.png"), full_page=False)
            record("Responsive: Mobile 390x844 screenshot captured", True)

            # -------------------------------------------------------------
            # Test 4: Small Mobile Viewport (360x800)
            # -------------------------------------------------------------
            page.set_viewport_size({"width": 360, "height": 800})
            page.wait_for_timeout(500)
            page.screenshot(path=os.path.join(SCREENSHOT_DIR, "04_small_mobile_assessment_form.png"), full_page=False)
            record("Responsive: Small Mobile 360x800 screenshot captured", True)

            # Restore Desktop Viewport for remaining tests
            page.set_viewport_size({"width": 1920, "height": 1080})
            page.wait_for_timeout(500)

            # -------------------------------------------------------------
            # Test 5: Verify All 20 Biomarker Controls & Dual Inputs Exist
            # -------------------------------------------------------------
            print("\n--- 3. Verifying All 20 Biomarker Dual Controls Across 5 Domains ---")
            domain_tabs = ["psychological", "physiological", "environmental", "academic", "social"]
            for domain in domain_tabs:
                page.click(f"button.cat-tab:has-text('{domain[:4]}')")
                page.wait_for_timeout(300)
                domain_features = [b for b in ALL_20_BIOMARKERS if b["domain"] == domain]
                for feat in domain_features:
                    num_input = page.locator(f"#input-{feat['key']}")
                    slider = page.locator(f"#slider-{feat['key']}")
                    assert num_input.count() == 1, f"Missing numeric input for {feat['key']}"
                    assert slider.count() == 1, f"Missing slider for {feat['key']}"
                    assert num_input.get_attribute("min") == str(feat["min"])
                    assert num_input.get_attribute("max") == str(feat["max"])
                    assert slider.get_attribute("min") == str(feat["min"])
                    assert slider.get_attribute("max") == str(feat["max"])
            record("Form: All 20 biomarkers have synchronized numeric inputs and sliders", True)

            # -------------------------------------------------------------
            # Test 6: Bi-Directional Synchronization Test
            # -------------------------------------------------------------
            print("\n--- 4. Testing Bi-Directional Synchronization ---")
            page.click("button.cat-tab:has-text('Psych')")
            page.wait_for_timeout(300)

            # 6.1: Slider -> Numeric Input
            page.locator("#slider-anxietyLevel").fill("18")
            page.locator("#slider-anxietyLevel").dispatch_event("input")
            page.locator("#slider-anxietyLevel").dispatch_event("change")
            page.wait_for_timeout(200)
            num_val = page.locator("#input-anxietyLevel").input_value()
            record("Bi-Directional: Slider movement immediately updates numeric input", num_val == "18", f"num_val={num_val}")

            # 6.2: Numeric Input -> Slider
            page.locator("#input-selfEsteem").fill("25")
            page.locator("#input-selfEsteem").dispatch_event("input")
            page.locator("#input-selfEsteem").dispatch_event("change")
            page.wait_for_timeout(200)
            slider_val = page.locator("#slider-selfEsteem").input_value()
            record("Bi-Directional: Numeric input typing immediately updates slider", slider_val == "25", f"slider_val={slider_val}")

            # Capture Synchronization Screenshot
            page.screenshot(path=os.path.join(SCREENSHOT_DIR, "06_slider_numeric_synchronization.png"), full_page=False)

            # -------------------------------------------------------------
            # Test 7: Numeric Input Validation (Min, Max, Decimal, Empty)
            # -------------------------------------------------------------
            print("\n--- 5. Testing Numeric Input Validation ---")
            # 7.1: Value > max (anxietyLevel max is 21, enter 28)
            page.locator("#input-anxietyLevel").fill("28")
            page.locator("#input-anxietyLevel").dispatch_event("input")
            page.wait_for_timeout(200)
            has_max_err = page.is_visible("text=Must be between 0 and 21")
            record("Validation: Values exceeding maximum show inline error", has_max_err)

            # 7.2: Value < min (enter -3)
            page.locator("#input-selfEsteem").fill("-3")
            page.locator("#input-selfEsteem").dispatch_event("input")
            page.wait_for_timeout(200)
            has_min_err = page.is_visible("text=Must be between 0 and 30")
            record("Validation: Values below minimum show inline error", has_min_err)

            # 7.3: Decimal value (enter 4.5 in depression)
            page.locator("#input-depression").fill("4.5")
            page.locator("#input-depression").dispatch_event("input")
            page.wait_for_timeout(200)
            has_dec_err = page.is_visible("text=Must be a whole number")
            record("Validation: Decimal values where whole number required show inline error", has_dec_err)

            # 7.4: Empty value
            page.locator("#input-mentalHealthHistory").fill("")
            page.locator("#input-mentalHealthHistory").dispatch_event("input")
            page.wait_for_timeout(200)
            has_empty_err = page.is_visible("text=Value is required")
            record("Validation: Empty fields show inline error", has_empty_err)

            # 7.5: Attempt submission while invalid
            page.click("button[type='submit']")
            page.wait_for_timeout(300)
            top_err_visible = page.is_visible("text=Please correct the invalid biomarker values")
            record("Validation: Invalid form prevents submission and shows alert banner", top_err_visible)

            # Capture Validation Screenshot
            page.screenshot(path=os.path.join(SCREENSHOT_DIR, "05_numeric_input_validation.png"), full_page=False)

            # -------------------------------------------------------------
            # Test 8: Clinical Preset Personas
            # -------------------------------------------------------------
            print("\n--- 6. Testing 1-Click Clinical Presets ---")
            page.click("button.preset-pill:has-text('Calm & Balanced')")
            page.wait_for_timeout(400)
            anx_val = page.locator("#input-anxietyLevel").input_value()
            se_val = page.locator("#input-selfEsteem").input_value()
            record("Presets: 'Calm & Balanced' populates valid synchronized values", anx_val == "2" and se_val == "26",
                   f"anxiety={anx_val}, selfEsteem={se_val}")
            record("Presets: Presets clear previous validation errors", not page.is_visible("text=Must be between"))

            page.click("button.preset-pill:has-text('Severe Burnout Episode')")
            page.wait_for_timeout(400)
            burn_anx = page.locator("#input-anxietyLevel").input_value()
            record("Presets: 'Severe Burnout' populates high risk values", burn_anx == "20", f"anxiety={burn_anx}")

            # -------------------------------------------------------------
            # Test 9: Valid Submission & Real ML Inference
            # -------------------------------------------------------------
            print("\n--- 7. Testing Valid Submission & Prediction Telemetry ---")
            page.click("button[type='submit']")
            page.wait_for_selector(".result-hero", timeout=12000)

            result_level = page.locator(".result-level-title").inner_text()
            conf_visible = page.is_visible("text=Random Forest Confidence:")
            radar_visible = page.is_visible("text=5-Domain Vulnerability Radar")
            prob_visible = page.is_visible("text=Probability Distribution")
            factors_visible = page.is_visible("text=Top Contributing Stress Biomarkers")
            actions_visible = page.is_visible("text=Clinical Guidance & Action Plan")

            record("Inference: Stress classification result displayed", len(result_level) > 0, f"result={result_level}")
            record("Inference: Confidence percentage rendered", conf_visible)
            record("Inference: 5-Domain Radar chart rendered", radar_visible)
            record("Inference: Probability bar chart rendered", prob_visible)
            record("Inference: Top contributing factors rendered", factors_visible)
            record("Inference: Clinical action plan checklist rendered", actions_visible)

            # Capture Successful Prediction Result Screenshot
            page.screenshot(path=os.path.join(SCREENSHOT_DIR, "07_successful_prediction_result.png"), full_page=False)

            # -------------------------------------------------------------
            # Test 10: Payload Compatibility Regression
            # -------------------------------------------------------------
            print("\n--- 8. Testing Prediction Payload Compatibility ---")
            test_payload = {
                "anxietyLevel": 12, "selfEsteem": 18, "mentalHealthHistory": 0, "depression": 10,
                "headache": 2, "bloodPressure": 1, "sleepQuality": 6, "breathingProblem": 1,
                "noiseLevel": 3, "livingConditions": 4, "safety": 4, "basicNeeds": 4,
                "academicPerformance": 3, "studyLoad": 3, "teacherStudentRelationship": 3, "futureCareerConcerns": 2,
                "socialSupport": 2, "peerPressure": 2, "extracurricularActivities": 3, "bullying": 1
            }
            pred_status, pred_res = http_post(f"{API_BASE}/predict", test_payload, token=token)
            record("Payload: Backend accepts exact 20 camelCase features contract", pred_status == 200, f"status={pred_status}")
            record("Payload: Response matches expected schema",
                   "stressLevel" in pred_res and "confidence" in pred_res and "recommendations" in pred_res)

            # -------------------------------------------------------------
            # Test 11: Regression - Profile Persistence & Navigation
            # -------------------------------------------------------------
            print("\n--- 9. Regression Testing Profile & History ---")
            page.click("button.nav-btn:has-text('My Profile')")
            page.wait_for_timeout(600)
            profile_hash = page.evaluate("window.location.hash")
            profile_name = page.locator("input.form-control-modern").first.input_value()
            record("Regression: Profile view intact and hydrated", profile_hash == "#/profile" and len(profile_name) > 0)

            # Back to predict
            page.go_back()
            page.wait_for_timeout(400)
            record("Regression: Phase 4 browser history navigation intact", page.evaluate("window.location.hash") == "#/predict")

            browser.close()

        print("\n--- SUMMARY OF RESULTS ---")
        pass_count = sum(1 for t in test_results if t["status"] == "PASS")
        fail_count = sum(1 for t in test_results if t["status"] == "FAIL")
        print(f"Total: {len(test_results)} | Passed: {pass_count} | Failed: {fail_count}")

        with open(os.path.join(SCREENSHOT_DIR, "phase6_test_results.json"), "w") as f:
            json.dump(test_results, f, indent=2)

    finally:
        stop_services(procs)

if __name__ == "__main__":
    run_tests()
