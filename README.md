# 🧠 StressGuard AI – Human Stress Prediction System

[![Deploy with Vercel](https://vercel.com/button)](https://vercel.com/new/clone?repository-url=https%3A%2F%2Fgithub.com%2FKAVIRAJ-27%2FAI-Based-Human-Stress-Prediction-System&root-directory=frontend)

### 🌐 Live Public Deployments
- **Live Frontend (GitHub Pages)**: [https://kaviraj-27.github.io/AI-Based-Human-Stress-Prediction-System/](https://kaviraj-27.github.io/AI-Based-Human-Stress-Prediction-System/)
- **Live AI/ML Cloud Engine (Render)**: [https://ai-based-human-stress-prediction-system.onrender.com/health](https://ai-based-human-stress-prediction-system.onrender.com/health)
- **Live Backend REST API (Render)**: [https://stressguard-backend-l5mp.onrender.com/api/health](https://stressguard-backend-l5mp.onrender.com/api/health)
- **1-Click Vercel Deploy**: Click the **Deploy with Vercel** button above for an instant custom `.vercel.app` domain.

## Tech Stack
- **Frontend**: HTML5 + CSS3 + Bootstrap 5 + React 18 + Chart.js + Web Audio API
- **Backend**: Java 17 + Spring Boot 3.3 + Spring Security (JWT) + REST API
- **Database**: MySQL 8 / TiDB Cloud Serverless
- **ML Service**: Python 3 + Flask + Scikit-learn (Random Forest) + Gunicorn
- **Model Accuracy**: **88.64%** (Cross-Validation: 87.36%)

---

## 📁 Project Structure

```
stress-prediction/
├── ml-model/
│   ├── app.py              ← Flask REST API for ML inference
│   ├── stress_model.pkl    ← Trained Random Forest model (100 trees)
│   ├── scaler.pkl          ← Feature scaler
│   ├── model_meta.json     ← Model metadata & feature importance
│   └── requirements.txt    ← Python dependencies
│
├── backend/
│   ├── pom.xml             ← Maven dependencies (Java 17, Spring Boot 3.3)
│   └── src/main/java/com/stress/
│       ├── entity/         ← JPA entities (User, PredictionRecord)
│       ├── repository/     ← Spring Data JPA repositories
│       ├── service/        ← Business logic & RestTemplate ML integration
│       ├── controller/     ← REST controllers (Auth, Predict, Admin, Health)
│       ├── security/       ← JWT filter + fail-safe utilities
│       ├── config/         ← Security + dynamic CORS configuration
│       └── dto/            ← Request/Response DTOs (Zero-Lombok builders)
│
├── frontend/
│   ├── index.html          ← Canonical React SPA (Single source of truth)
│   ├── vercel.json         ← Vercel SPA routing configuration
│   └── _redirects          ← Static hosting rewrite rules
│
└── database/
    ├── setup.sql           ← Clean production schema DDL (users & prediction_records)
    └── seed-dev.sql        ← Local development sample accounts ONLY
```

---

## 🚀 Setup Instructions

### Step 1 – Database Setup

1. **Initialize Database Schema:**
   ```sql
   mysql -u root -p < database/setup.sql
   ```
   *Note:* `database/setup.sql` creates the `stress_db` database and table structures with zero hardcoded default credentials.

2. **(Optional) Local Development Seed Data:**
   In local development environments only, you can optionally seed sample accounts:
   ```sql
   mysql -u root -p stress_db < database/seed-dev.sql
   ```

3. **Configure Local Credentials Safely:**
   Do **not** commit passwords to `application.properties`. Instead, set the `DB_PASSWORD` environment variable or create `backend/src/main/resources/application-local.properties` (which is git-ignored):
   ```properties
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```

---

### Step 2 – Python ML Service

```bash
cd ml-model

# Install dependencies
pip install -r requirements.txt

# Start the Flask API
python app.py
# Runs on http://localhost:5000
```

**Test health:**
```bash
curl http://localhost:5000/health
# {"accuracy":88.64,"model":"RandomForest","model_loaded":true,"scaler_loaded":true,"service":"stressguard-ml-service","status":"ok"}
```

---

### Step 3 – Spring Boot Backend

```bash
cd backend
mvn clean test
mvn spring-boot:run
# Runs on http://localhost:8080/api
```

**Test health:**
```bash
curl http://localhost:8080/api/health
# {"status":"UP","service":"stressguard-backend","version":"1.0.0"}
```

---

### Step 4 – Frontend

The canonical frontend file is `frontend/index.html`.

You can serve it with any local static HTTP server:
```bash
cd frontend
python -m http.server 3000
# Open http://localhost:3000 in your browser
```

---

## 🔑 User Accounts

### Production
Production databases start clean with zero default accounts for security. Create your account directly using the **"Register here"** button on the login view.

### Local Development Seed Accounts (Optional)
If you populated your local database with `database/seed-dev.sql`, the following test accounts are available:

| Role  | Username  | Password  | Scope |
|-------|-----------|-----------|-------|
| Admin | `admin`   | `admin123`| Local development only |
| User  | `testuser`| `user123` | Local development only |

---

## 📡 API Endpoints

### System & Health
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/health` | Backend container health status |
| GET | `http://localhost:5000/health` | ML microservice health & model status |

### Auth
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login, get JWT token |

### Predictions (JWT required)
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/predict` | Submit 20-biomarker prediction |
| GET | `/api/predict/history` | Get current user's history |
| GET | `/api/predict/{id}` | Get prediction by ID |
| GET | `/api/predict/dashboard` | User personal dashboard stats |

### Admin (ADMIN role required)
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/admin/stats` | System-wide statistics |
| GET | `/api/admin/users` | All users list |
| GET | `/api/admin/users/{id}` | User details |
| PUT | `/api/admin/users/{id}` | Update user status or role |
| DELETE | `/api/admin/users/{id}` | Delete user account |
| GET | `/api/admin/stress-distribution` | Global stress distribution |
| GET | `/api/admin/recent-predictions` | Recent global predictions |

### ML Service (Python Microservice)
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/health` | Model health & status |
| POST | `/predict` | Single prediction |
| POST | `/batch-predict` | Batch predictions |
| GET | `/model-info` | Feature list & importance |

---

## 🧠 ML Model – Feature Reference

| Feature | Range | Description |
|---------|-------|-------------|
| anxiety_level | 0–21 | Anxiety intensity |
| self_esteem | 0–30 | Self-confidence |
| mental_health_history | 0–1 | Prior mental health issues |
| depression | 0–27 | Depression severity |
| headache | 0–5 | Headache frequency |
| blood_pressure | 0–3 | Blood pressure level |
| sleep_quality | 0–9 | Sleep quality |
| breathing_problem | 0–5 | Breathing difficulties |
| noise_level | 0–7 | Environmental noise |
| living_conditions | 0–5 | Home quality |
| safety | 0–5 | Personal safety |
| basic_needs | 0–5 | Basic needs fulfilment |
| academic_performance | 0–5 | Academic grades |
| study_load | 0–5 | Study intensity |
| teacher_student_relationship | 0–5 | Teacher rapport |
| future_career_concerns | 0–5 | Career anxiety |
| social_support | 0–3 | Support network |
| peer_pressure | 0–5 | Peer-related pressure |
| extracurricular_activities | 0–5 | Activity involvement |
| bullying | 0–5 | Bullying exposure |

**Output:** 0 = Low Stress | 1 = Medium Stress | 2 = High Stress

---

## 🔧 Modules

### User Module
- Register / Login with JWT
- View personal wellness dashboard
- Submit stress predictions (20-slider clinical UI)
- View prediction history archive with CSV export
- Edit personal profile

### Admin Module
- System-wide user and prediction statistics
- User management (activate/deactivate/delete)
- Global stress distribution analytics
- Recent prediction audit stream

### Dashboard Module
- Trend and distribution charts via Chart.js
- Real-time confidence metrics
- Clinical vulnerability domain mapping

### Prediction Module
- 20-feature input form categorized across 5 clinical domains
- Clinical presets (Exam Period, Healthy Baseline, High Burnout, Chronic Stress)
- 4-7-8 guided breathing relaxation widget
- Real-time Random Forest prediction with probability distributions
- Personalized recommendations

---

## 📦 Dependencies Summary

### Backend (pom.xml)
- `spring-boot-starter-web` (Spring Boot 3.3.0)
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `spring-boot-starter-validation`
- `mysql-connector-j`
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` (0.11.5)
- *(Zero Lombok — 100% manual builders and accessors)*

### Python ML Service (requirements.txt)
- `Flask==3.0.3`
- `gunicorn==23.0.0`
- `scikit-learn==1.5.1`
- `numpy==2.0.2`
- `joblib==1.4.2`

### Frontend (CDN)
- Bootstrap 5.3.2
- React 18.2.0 + Babel Standalone 7.23.2
- Chart.js 4.4.0
- Google Fonts (Outfit & Plus Jakarta Sans)
- Web Audio API (native browser engine)
