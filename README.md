# 🧠 StressGuard AI – Human Stress Prediction System

[![Deploy with Vercel](https://vercel.com/button)](https://vercel.com/new/clone?repository-url=https%3A%2F%2Fgithub.com%2FKAVIRAJ-27%2FAI-Based-Human-Stress-Prediction-System&root-directory=frontend)

### 🌐 Live Public Deployments
- **Live Frontend (GitHub Pages)**: [https://kaviraj-27.github.io/AI-Based-Human-Stress-Prediction-System/](https://kaviraj-27.github.io/AI-Based-Human-Stress-Prediction-System/)
- **Live AI/ML Cloud Engine (Render)**: [https://ai-based-human-stress-prediction-system.onrender.com/health](https://ai-based-human-stress-prediction-system.onrender.com/health)
- **1-Click Vercel Deploy**: Click the **Deploy with Vercel** button above for an instant custom `.vercel.app` domain.

## Tech Stack
- **Frontend**: HTML5 + CSS3 + Bootstrap 5 + React 18 + Chart.js
- **Backend**: Java 17 + Spring Boot 3.2 + JWT Auth + REST API
- **Database**: MySQL 8
- **ML Service**: Python 3 + Flask + Scikit-learn (Random Forest)
- **Model Accuracy**: **88.64%** (Cross-Validation: 87.36%)

---

## 📁 Project Structure

```
stress-prediction/
├── ml-model/
│   ├── app.py              ← Flask REST API for ML inference
│   ├── stress_model.pkl    ← Trained Random Forest model
│   ├── scaler.pkl          ← Feature scaler
│   └── model_meta.json     ← Model metadata & feature importance
│
├── backend/
│   ├── pom.xml             ← Maven dependencies
│   └── src/main/java/com/stress/
│       ├── entity/         ← JPA entities (User, PredictionRecord)
│       ├── repository/     ← Spring Data JPA repositories
│       ├── service/        ← Business logic
│       ├── controller/     ← REST controllers
│       ├── security/       ← JWT filter + utilities
│       ├── config/         ← Security + CORS config
│       └── dto/            ← Request/Response DTOs
│
├── frontend/
│   └── index.html          ← Complete React SPA
│
└── database/
    └── setup.sql           ← MySQL setup & seed script
```

---

## 🚀 Setup Instructions

### Step 1 – MySQL Database

```sql
mysql -u root -p < database/setup.sql
```

Update `backend/src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

---

### Step 2 – Python ML Service

```bash
cd ml-model

# Install dependencies
pip install flask scikit-learn pandas numpy joblib

# Start the Flask API
python app.py
# Runs on http://localhost:5000
```

**Test it:**
```bash
curl http://localhost:5000/health
# {"status":"ok","model":"RandomForest","accuracy":88.64}
```

---

### Step 3 – Spring Boot Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
# Runs on http://localhost:8080/api
```

---

### Step 4 – Frontend

Simply open `frontend/index.html` in any browser.

Or serve it:
```bash
cd frontend
npx serve .      # OR
python3 -m http.server 3000
```

---

## 🔑 Login Credentials

| Role  | Username  | Password  |
|-------|-----------|-----------|
| Admin | `admin`   | `admin123`|
| User  | `testuser`| `user123` |

Or register a new user via the UI.

---

## 📡 API Endpoints

### Auth
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login, get JWT token |

### Predictions (JWT required)
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/predict` | Submit prediction |
| GET | `/api/predict/history` | Get user's history |
| GET | `/api/predict/{id}` | Get prediction by ID |
| GET | `/api/predict/dashboard` | User dashboard stats |

### Admin (ADMIN role only)
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/admin/stats` | System statistics |
| GET | `/api/admin/users` | All users list |
| PUT | `/api/admin/users/{id}` | Update user |
| DELETE | `/api/admin/users/{id}` | Delete user |
| GET | `/api/admin/stress-distribution` | Stress breakdown |
| GET | `/api/admin/recent-predictions` | Recent activity |

### ML Service (Python)
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/health` | Model health check |
| POST | `/predict` | Single prediction |
| POST | `/batch-predict` | Batch predictions |
| GET | `/model-info` | Model details |

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
- View personal dashboard
- Submit stress predictions (slider UI)
- View prediction history
- Edit profile

### Admin Module
- System-wide statistics
- All users management (activate/deactivate/delete)
- View all predictions
- Stress distribution analytics

### Dashboard Module
- Doughnut charts for stress distribution
- Recent prediction history
- Confidence metrics
- Model performance indicators

### Prediction Module
- 20-feature input form with sliders
- Real-time Random Forest prediction
- Probability distribution bar chart
- Top contributing factors with progress bars
- Personalized recommendations

---

## 📦 Dependencies Summary

### Backend (pom.xml)
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- mysql-connector-j
- jjwt (0.11.5)
- lombok

### Python ML
- flask, scikit-learn, pandas, numpy, joblib

### Frontend (CDN)
- Bootstrap 5.3
- React 18 + Babel
- Chart.js 4.4
