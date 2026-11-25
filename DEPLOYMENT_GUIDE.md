# 🚀 StressGuard AI 2.0 – Production Cloud Deployment Guide

This guide provides the complete, production-ready roadmap for deploying **StressGuard AI** to the cloud across free-tier hosting platforms:

- **Frontend SPA**: [Vercel](https://vercel.com) (Global Edge CDN, instantaneous loads, 0 cold starts)
- **Java Spring Boot Backend**: [Render](https://render.com) (Web Service via Docker, 512 MB Free Tier)
- **Python ML Microservice**: [Render](https://render.com) (Native Python 3 Web Service, Gunicorn)
- **Database**: [TiDB Cloud Serverless](https://tidbcloud.com) (5 GB Free Forever, MySQL 8.0 wire-compatible, 0 sleep)
- **Source Control & CI/CD**: [GitHub](https://github.com)

---

## 1. Deployment Architecture

```
┌────────────────────────────────────────────────────────┐
│                      USER CLIENTS                      │
│            📱 Mobile Browser / 💻 Desktop PC           │
└───────────────────────────┬────────────────────────────┘
                            │ HTTPS (Global Edge CDN)
                            ▼
┌────────────────────────────────────────────────────────┐
│                   FRONTEND (Vercel)                    │
│      React 18 SPA (index.html) + Web Audio API         │
│   • Configurable Cloud API & Direct ML Fallback        │
│   • 75s Cold-Start Graceful Timeout & Progress UX      │
└─────────────┬───────────────────────────┬──────────────┘
              │ REST / Bearer JWT         │ Emergency ML Fallback
              ▼                           ▼
┌──────────────────────────────┐    ┌────────────────────┐
│    BACKEND (Render Docker)   │    │ ML ENGINE (Render) │
│  Spring Boot 3.3.0 REST API  │───▶│  Flask 3.0 / ML    │
│  • Memory Capped (-Xmx256m)  │    │  • Random Forest   │
│  • Dynamic CORS & Health     │    │  • 88.64% Accuracy │
│  • Port 8080 / Context /api  │    │  • Port $PORT      │
└──────────────┬───────────────┘    └────────────────────┘
               │ JDBC / TLSv1.3
               ▼
┌──────────────────────────────┐
│  DATABASE (TiDB Serverless)  │
│  Managed Cloud MySQL 8.0     │
│  • Users & Predictions Log   │
│  • 5GB Free / Never Sleeps   │
└──────────────────────────────┘
```

---

## 2. Required Accounts

All recommended services offer generous 100% free tiers:
1. **GitHub Account**: Hosts source code and coordinates automated deployment triggers.
2. **TiDB Cloud Account**: [https://tidbcloud.com](https://tidbcloud.com) — Provides permanent free-tier Serverless MySQL with SSL.
3. **Render Account**: [https://render.com](https://render.com) — Hosts the Python ML service and Java backend container.
4. **Vercel Account**: [https://vercel.com](https://vercel.com) — Hosts the single-page application on global edge CDN.

---

## 3. Environment Variables Reference

See `.env.example` in the project root for variable templates.

### A. Java Spring Boot Backend (Render)
| Environment Variable | Description | Example / Recommended Value |
| :--- | :--- | :--- |
| `PORT` | Container internal port | `8080` |
| `DB_URL` | TiDB Cloud JDBC connection string | `jdbc:mysql://<HOST>:4000/stress_db?sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3` |
| `DB_USERNAME` | TiDB user provided by cluster | `xxxxxxxx.root` |
| `DB_PASSWORD` | TiDB cluster password | *(Your TiDB Password)* |
| `JWT_SECRET` | 32+ byte / 64+ char random secret (Mandatory on Render) | Run `openssl rand -hex 32` |
| `ML_SERVICE_URL` | Deployed Python ML Service URL | `https://stressguard-ml-service.onrender.com` |
| `CORS_ALLOWED_ORIGINS`| Allowed frontend domains | `https://<your-project>.vercel.app,*` |
| `DB_POOL_MAX` | Hikari max connection pool | `5` |
| `JPA_DDL_AUTO` | Hibernate schema creation | `update` |

### B. Python ML Microservice (Render)
| Environment Variable | Description | Default |
| :--- | :--- | :--- |
| `PORT` | Assigned dynamically by Render | `5000` |

---

## 4. Step-by-Step Deployment Instructions

### Step 1: Provision TiDB Cloud MySQL Database
1. Sign in to [TiDB Cloud Console](https://tidbcloud.com).
2. Click **Create Cluster** ➔ Select **Serverless** ➔ Choose your nearest region (e.g., `us-west-2` or `ap-southeast-1`) ➔ Click **Create**.
3. In the security modal, copy and save your cluster password.
4. Click **Connect** ➔ Choose **Java (Spring Boot / JDBC)** to view your connection parameters:
   - **Host**: e.g., `gateway01.us-west-2.prod.aws.tidbcloud.com`
   - **Port**: `4000`
   - **User**: e.g., `xxxx.root`
   - **Password**: *(Your password)*
5. Click **SQL Editor** on the TiDB dashboard and run:
   ```sql
   CREATE DATABASE IF NOT EXISTS stress_db;
   ```
6. Open and paste the contents of `database/setup.sql` into the SQL Editor and execute it to create and seed the `users` and `prediction_records` tables.

---

### Step 2: Deploy Python ML Microservice on Render
1. Log in to [Render Dashboard](https://dashboard.render.com).
2. Click **New +** ➔ **Web Service**.
3. Connect your GitHub repository: `AI-Based-Human-Stress-Prediction-System`.
4. Configure service parameters:
   - **Name**: `stressguard-ml-service`
   - **Region**: Same region as your DB if possible (e.g., Oregon)
   - **Root Directory**: `ml-model`
   - **Runtime**: `Python 3`
   - **Build Command**: `pip install -r requirements.txt`
   - **Start Command**: `gunicorn app:app --bind 0.0.0.0:$PORT --workers 2 --timeout 120`
   - **Instance Type**: `Free`
5. Click **Deploy Web Service**.
6. Once deployment finishes (~2 minutes), test the health endpoint:
   ```bash
   curl https://stressguard-ml-service.onrender.com/health
   # Expected response: {"accuracy":88.64,"model":"RandomForest","model_loaded":true,"scaler_loaded":true,"service":"stressguard-ml-service","status":"ok"}
   ```

---

### Step 3: Deploy Java Spring Boot Backend on Render
1. On [Render Dashboard](https://dashboard.render.com), click **New +** ➔ **Web Service**.
2. Select the same repository.
3. Configure service parameters:
   - **Name**: `stressguard-backend`
   - **Region**: Same region as ML service
   - **Root Directory**: `backend`
   - **Runtime**: `Docker`
   - **Dockerfile Path**: `Dockerfile`
   - **Instance Type**: `Free`
4. Under **Environment Variables**, click **Add Environment Variable** and enter:
   - `PORT` = `8080`
   - `DB_URL` = `jdbc:mysql://<TIDB_HOST>:4000/stress_db?sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3`
   - `DB_USERNAME` = `<TIDB_USERNAME>`
   - `DB_PASSWORD` = `<TIDB_PASSWORD>`
   - `JWT_SECRET` = *(Generate a secure 64-character secret, e.g. via `openssl rand -hex 32`. The backend will refuse to start on Render if left with default development keys)*
   - `ML_SERVICE_URL` = `https://stressguard-ml-service.onrender.com`
   - `CORS_ALLOWED_ORIGINS` = `*`
5. Click **Deploy Web Service**.
6. Render builds the Docker image and launches the JVM with memory limits (`-XX:+UseSerialGC -Xms128m -Xmx256m`).
7. Verify health:
   ```bash
   curl https://stressguard-backend.onrender.com/api/health
   # Expected response: {"status":"UP","service":"stressguard-backend","version":"1.0.0","timestamp":"..."}
   ```

---

### Step 4: Deploy Frontend on Vercel
1. Log in to [Vercel](https://vercel.com).
2. Click **Add New...** ➔ **Project**.
3. Import your GitHub repository: `AI-Based-Human-Stress-Prediction-System`.
4. In project configuration:
   - **Framework Preset**: `Other`
   - **Root Directory**: `frontend` (or leave root; both contain `index.html` and `vercel.json`)
   - **Build & Development Settings**: Defaults (build command: `echo 'Static assets ready'`, output dir: `.`)
5. Click **Deploy**.
6. In ~15 seconds, your app will be live at `https://<your-project>.vercel.app`!

---

## 5. How to Connect the Services

1. Open your live Vercel URL on your mobile phone or browser.
2. In the topbar, click the **API: Cloud** badge pill:
   - Paste your Render backend URL: `https://stressguard-backend.onrender.com/api`
   - Click OK. The frontend stores this in `localStorage.setItem('api_endpoint')` and connects instantly!
3. Alternatively, update `DEFAULT_PROD_API` in `frontend/index.html` to your exact Render URL.

---

## 6. Verification and Testing

1. **Backend Health Check**:
   Navigate to `https://<backend>.onrender.com/api/health` ➔ Confirm `{"status": "UP"}`.
2. **ML Health Check**:
   Navigate to `https://<ml-service>.onrender.com/health` ➔ Confirm `model_loaded: true`.
3. **End-to-End User Sign-in**:
   - Open your Vercel URL.
   - Click **"Register here"** to create your primary user or administrator profile. *(Production databases start clean with zero default accounts. In local development environments only, you can optionally run `database/seed-dev.sql` to populate demo accounts)*.
   - *Note on first request*: If Render services were idle, the first request will take 45–60s while the container wakes up. The UI shows a friendly notification while waiting.
4. **Stress Prediction**:
   - Navigate to **New Prediction**.
   - Adjust the 20 biometric sliders or pick a clinical preset.
   - Click **Run Random Forest Analysis**.
   - Verify the 5-domain vulnerability radar, probability breakdown, and customized recommendations.
5. **History & Dashboard Verification**:
   - Navigate to **History Archive** to verify the record was saved in TiDB Cloud.
   - Refresh the page on your mobile device to verify persistence across sessions.

---

## 7. Troubleshooting Common Errors

### Error: "Request timed out while connecting to cloud server"
- **Cause**: Render's free tier spins down web services after 15 minutes of inactivity. Cold start wake-up time is 45–60 seconds.
- **Solution**: The frontend timeout is configured to 75 seconds with a visual loading status. If a timeout occurs on a slow cellular connection, simply click the button again. Once awake, requests respond in under 300ms.

### Error: "Communications link failure / SSL handshake error"
- **Cause**: TiDB Cloud requires TLS 1.2 or 1.3.
- **Solution**: Ensure your `DB_URL` contains `?sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3`.

### Error: "Render out of memory (OOM / Exit code 137)"
- **Cause**: Java default heap sizing exceeding Render's 512 MB RAM free container limit.
- **Solution**: The provided `backend/Dockerfile` already restricts the JVM via `-XX:+UseSerialGC -Xms128m -Xmx256m`, keeping the entire container footprint under 380 MB.

### Error: "CORS error / NetworkError when attempting to fetch resource"
- **Cause**: Frontend origin not permitted by the backend.
- **Solution**: The backend `SecurityConfig` allows all origins by default (`CORS_ALLOWED_ORIGINS=*`), or you can specify your exact Vercel domain in Render's environment variables (`CORS_ALLOWED_ORIGINS=https://your-project.vercel.app`).

---

## 8. Rollback Procedure

If a deployed version has issues:
1. **Frontend Rollback (Vercel)**:
   - In Vercel Dashboard ➔ Go to **Deployments** tab ➔ Locate the last successful deployment ➔ Click the three dots `...` ➔ **Promote to Production**. Instant 0-downtime rollback.
2. **Backend / ML Rollback (Render)**:
   - In Render Dashboard ➔ Go to the service ➔ **Events** tab ➔ Select previous build ➔ Click **Rollback to this version**.
3. **Database Rollback (TiDB)**:
   - TiDB Serverless provides built-in Point-in-Time Recovery (PITR). In TiDB Console ➔ Cluster ➔ Backups ➔ Restore.
