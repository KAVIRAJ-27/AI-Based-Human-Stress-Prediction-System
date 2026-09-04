# 🚀 StressGuard AI 2.0 – Production Cloud Deployment Guide

This guide walks you through deploying the complete Full-Stack **StressGuard AI** system to the cloud:
1. **Database**: Aiven Free Cloud MySQL (or Railway MySQL)
2. **Python ML Service**: Render Web Service
3. **Spring Boot Backend**: Render Web Service (Docker)
4. **Frontend**: Vercel / Netlify (Free Global CDN)

---

## 🗄️ Step 1: Cloud MySQL Database (Aiven)

1. Go to [https://aiven.io](https://aiven.io) and create a free account.
2. Click **Create Service** ➔ Select **MySQL** ➔ Choose the **Free Tier**.
3. Once provisioned (1–2 minutes), copy your connection details:
   - **Host / Port**: e.g., `mysql-xxxx-xxxx.aivencloud.com:12345`
   - **User**: `avnadmin`
   - **Password**: `your_aiven_password`
   - **Database**: `defaultdb` (or create `stress_db`)
4. Construct your Spring Boot `DB_URL`:
   ```text
   jdbc:mysql://<HOST>:<PORT>/<DATABASE>?sslmode=require&allowPublicKeyRetrieval=true
   ```

---

## 🧠 Step 2: Deploy Python ML Service (Render)

1. Log in to [https://render.com](https://render.com).
2. Click **New +** ➔ **Web Service**.
3. Connect your repository: `https://github.com/KAVIRAJ-27/AI-Based-Human-Stress-Prediction-System`.
4. Configure service settings:
   - **Name**: `stressguard-ml-service`
   - **Root Directory**: `ml-model`
   - **Runtime**: `Python 3`
   - **Build Command**: `pip install -r requirements.txt`
   - **Start Command**: `gunicorn app:app --bind 0.0.0.0:$PORT --workers 2 --timeout 120`
   - **Plan**: `Free`
5. Click **Deploy Web Service**.
6. Once deployed, note down your ML URL:  
   `https://stressguard-ml-service.onrender.com`

---

## ☕ Step 3: Deploy Spring Boot Backend (Render)

1. On [https://render.com](https://render.com), click **New +** ➔ **Web Service**.
2. Select the same repository.
3. Configure service settings:
   - **Name**: `stressguard-backend`
   - **Root Directory**: `backend`
   - **Runtime**: `Docker`
   - **Dockerfile Path**: `Dockerfile`
   - **Plan**: `Free`
4. Under **Environment Variables**, add:
   | Key | Value |
   | :--- | :--- |
   | `PORT` | `8080` |
   | `DB_URL` | `jdbc:mysql://<HOST>:<PORT>/defaultdb?sslmode=require&allowPublicKeyRetrieval=true` |
   | `DB_USERNAME` | `avnadmin` |
   | `DB_PASSWORD` | `<your_aiven_password>` |
   | `ML_SERVICE_URL` | `https://stressguard-ml-service.onrender.com` |
5. Click **Deploy Web Service**.
6. Once deployed, your API base will be:  
   `https://stressguard-backend.onrender.com/api`

---

## 🌐 Step 4: Deploy Frontend (Vercel or Netlify)

### Option A: Vercel (Fastest & Recommended)
1. Go to [https://vercel.com](https://vercel.com) and click **Add New Project**.
2. Import your GitHub repository.
3. In **Root Directory**, select `frontend`.
4. Leave Build & Output settings default (it uses the static files + `vercel.json`).
5. Click **Deploy**.
6. Done! Your site will be live at `https://your-project.vercel.app`.

### Option B: Netlify
1. Go to [https://netlify.com](https://netlify.com) ➔ **Add new site** ➔ **Import an existing project**.
2. Select your repository and set the base directory to `frontend`.
3. Click **Deploy site**.

---

## ⚡ 1-Click Blueprints (Alternative)

### Render Blueprint (`render.yaml`)
If you prefer Infrastructure-as-Code:
1. Push this repository to GitHub.
2. In Render, click **New +** ➔ **Blueprint**.
3. Point to your repository. Render will automatically parse `render.yaml` and create both the ML Service and Java Backend services together!

### Multi-Container Docker (`docker-compose.yml`)
To run all 4 containers (MySQL, ML Service, Spring Boot, Nginx) on any VPS (AWS EC2, DigitalOcean, Hetzner):
```bash
docker compose up -d --build
```
Everything will be live with full internal networking!
