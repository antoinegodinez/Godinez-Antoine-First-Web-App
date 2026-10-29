# UserHub — My First Full-Stack Web App

UserHub is a simple user-management application. Users can register, log in, view a protected
dashboard, and edit their profile. An ADMIN can view all registered users. The ReactJS frontend
talks to a Spring Boot REST API backed by an H2 database.

## Technologies Used

| Layer    | Technology |
|----------|------------|
| Backend  | Java 21, Spring Boot 4.0, Spring Web, Spring Data JPA, Bean Validation, spring-security-crypto (BCrypt), Maven |
| Database | H2 (in-memory, with web console) |
| Frontend | ReactJS 18, Vite 5, React Router 6, Axios |

## System Requirements

- **JDK 21+** (`java -version`)
- **Maven 3.8+** (`mvn -version`) — or open `backend/` in IntelliJ / Eclipse / VS Code and run `UserhubApplication`
- **Node.js 18+ and npm** (`node -v`, `npm -v`)
- **Git**
- Free ports: **8080** (backend) and **5173** (frontend)

## Project Structure

```
Godinez-Antoine-First-Web-App/
├── backend/    Spring Boot API (entity, repository, dto, service, controller, config, exception)
├── frontend/   React + Vite app
├── docs/       REQUIREMENTS.md and screenshots/
└── README.md
```

## Backend Setup & Run

```bash
cd Godinez-Antoine-First-Web-App/backend
mvn spring-boot:run
```

The API starts at **http://localhost:8080**. On first start a default admin is created automatically:

| Email | Password | Role |
|-------|----------|------|
| `admin@userhub.local` | `ChangeMe123!` | ADMIN |

(For local demonstration only. Set environment variables `USERHUB_ADMIN_EMAIL` and `USERHUB_ADMIN_PASSWORD` to override the demo values; do not use the demo password for real accounts.)

**H2 Console:** http://localhost:8080/h2-console
JDBC URL: `jdbc:h2:mem:userhubdb` · User: `sa` · Password: *(blank)*
> The in-memory H2 database resets when the backend stops.

## Frontend Setup & Run

Open a second terminal:

```bash
cd Godinez-Antoine-First-Web-App/frontend
npm install
npm run dev
```

The app opens at **http://localhost:5173**.

## Application URLs and Ports

| Service        | URL |
|----------------|-----|
| Frontend       | http://localhost:5173 |
| Backend API    | http://localhost:8080/api |
| H2 Console     | http://localhost:8080/h2-console |

## Additional Configuration

- **API URL:** the frontend defaults to `http://localhost:8080/api`. To change it, copy
  `frontend/.env.example` to `frontend/.env` and edit `VITE_API_URL`.
- **CORS:** the backend allows the origin in `app.cors.allowed-origin` (default `http://localhost:5173`).
- **Token lifetime:** `app.token.ttl-hours` (default 24).
- **In-memory DB:** the database resets on backend restart. Tokens and registered accounts are cleared; the default ADMIN is seeded again.

## How to Test the Application

Start both servers, then:

1. **Register** — go to `/register`, create an account. Try a short password / bad email / duplicate email to see validation errors (400 / 409).
2. **Login** — log in with that account → redirected to `/dashboard`.
3. **Protected route** — log out, then visit `/dashboard` directly → redirected to `/login`.
4. **Edit profile** — on the dashboard click *Edit my profile*, change the full name, save. Refresh the page; the change persists.
5. **Role-based access** — as a normal USER, visit `/admin/users` → redirected to the dashboard; the API returns 403.
6. **Admin page** — log in as `admin@userhub.local` / `ChangeMe123!` → *Users* link → table of all users.
7. **Server-side logout** — click *Logout*. The token is deleted from the `auth_tokens` table (check in H2 console), and reusing the old token returns 401.

### Testing the API with curl

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" \
  -d '{"fullName":"Juan Dela Cruz","email":"juan@example.com","password":"password123"}'

# Login (copy the token from the response)
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" \
  -d '{"email":"juan@example.com","password":"password123"}'

# Protected endpoints
curl http://localhost:8080/api/dashboard/me -H "Authorization: Bearer <TOKEN>"
curl -X PUT http://localhost:8080/api/dashboard/me -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" -d '{"fullName":"Juan D. Cruz"}'
curl http://localhost:8080/api/admin/users -H "Authorization: Bearer <ADMIN_TOKEN>"
curl -X POST http://localhost:8080/api/auth/logout -H "Authorization: Bearer <TOKEN>"
```

## Documentation

See [`docs/REQUIREMENTS.md`](docs/REQUIREMENTS.md) for the full feature list and
[`docs/screenshots/`](docs/screenshots/) for screenshots.

## PowerPoint-aligned API endpoints

- `POST /api/auth/register` — create USER account (201)
- `POST /api/auth/login` — receive Bearer token
- `POST /api/auth/logout` — invalidate Bearer token
- `GET /api/dashboard` — protected dashboard/profile response
- `GET /api/dashboard/users` — ADMIN-only list of users
- `PUT /api/users/me` — change your full name

The legacy `/api/dashboard/me` and `/api/admin/users` endpoints are also retained for compatibility.

## IntelliJ IDEA quick start

Open the `backend` folder as a Maven project, select JDK 21, wait for dependencies, and run `com.userhub.UserhubApplication`. Open a terminal in `frontend`, run `npm install` and then `npm run dev`. The frontend uses `http://localhost:5173`; the backend uses `http://localhost:8080`.

## Before submission

Run and verify all flows locally. Save genuine screenshots into `docs/screenshots/` (registration, login, dashboard, ADMIN users, edit profile). Commit the repository to GitHub yourself; this ZIP does not represent a verified running app or an uploaded repository.
