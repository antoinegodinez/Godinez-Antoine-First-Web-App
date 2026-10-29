# UserHub — Implemented Features

Base API URL: `http://localhost:8080/api`

## Core Features

| # | Feature | Description | Expected Behavior | Endpoint | Status |
|---|---------|-------------|-------------------|----------|--------|
| 1 | User Registration | New users create an account with full name, email, password. | Validates input (name 2–100 chars, valid email, password ≥ 8). Password stored as BCrypt hash. New users get role `USER`. Duplicate email → 409; invalid input → 400 with `fieldErrors`. Returns 201. | `POST /api/auth/register` | Implemented |
| 2 | User Login / Authentication | Users log in with email and password. | Valid credentials → 200 with an opaque bearer token + user info. Wrong credentials → 401 "Invalid email or password". | `POST /api/auth/login` | Implemented |
| 3 | Password Hashing | Passwords are never stored in plain text. | BCrypt (`BCryptPasswordEncoder`) used on register and verified on login. | — | Implemented |
| 4 | Token-Based Authentication | Protected endpoints require `Authorization: Bearer <token>`. | Tokens are random UUID-based strings stored in `auth_tokens` with a 24 h expiry. Missing/invalid/expired token → 401. Enforced by `AuthInterceptor`. | all `/api/dashboard/**`, `/api/admin/**`, `/api/auth/logout` | Implemented |
| 5 | Role-Based Authorization | Two roles: `USER` and `ADMIN`. | `/api/admin/**` requires ADMIN; a USER receives 403 Forbidden. | `/api/admin/**` | Implemented |
| 6 | Protected Dashboard | Authenticated user sees own profile data. | Returns id, full name, email, role, created date of the logged-in user. Unauthenticated → 401. | `GET /api/dashboard/me` | Implemented |
| 7 | Exception Handling | Consistent JSON error responses. | `GlobalExceptionHandler` returns `{timestamp, status, error, message}` for 400, 401, 403, 404, 409, 500. | — | Implemented |
| 8 | H2 + Spring Data JPA | Persistent storage. | Entities `User` and `AuthToken`; repositories extend `JpaRepository`; in-memory H2 DB (resets on backend restart); console at `/h2-console`. | — | Implemented |
| 9 | Layered Architecture | Clear separation of concerns. | Packages: `entity`, `repository`, `dto`, `service`, `controller`, `config`, `exception`. | — | Implemented |

## Additional Features

| # | Feature | Pts | Description | Expected Behavior | Endpoint | Status |
|---|---------|-----|-------------|-------------------|----------|--------|
| A1 | Seed an ADMIN User | 10 | Default admin created at startup via `CommandLineRunner` (`DataSeeder`). | On startup, if `admin@userhub.local` does not exist it is created with role ADMIN and password `ChangeMe123!` (hashed). Not duplicated while the account exists; the in-memory database resets on restart. | — | Implemented |
| A2 | Admin Users Page | 15 | ADMIN-only page listing all registered users. | `/admin/users` shows ID, name, email, role, registration date. Non-admins are redirected in the UI and get 403 from the API. | `GET /api/admin/users` | Implemented |
| A3 | Server-Side Logout | 10 | Endpoint invalidates the token. | Deletes the token row; the token is then rejected with 401. Frontend calls it, then clears local storage and redirects to `/login`. | `POST /api/auth/logout` | Implemented |
| A4 | Edit My Profile | 15 | Authenticated user updates their full name. | Dashboard form → `PUT`; validates 2–100 chars; updates DB; UI refreshes with new name. Only the caller's own record can be changed. | `PUT /api/dashboard/me` | Implemented |

## Frontend Features

| Feature | Description | Status |
|---------|-------------|--------|
| Vite + React setup | Project bootstrapped with Vite, React 18 | Implemented |
| Registration page (`/register`) | Form with client-side password confirmation and server field errors | Implemented |
| Login page (`/login`) | Form with error display; stores token | Implemented |
| Protected dashboard (`/dashboard`) | Loads profile from API; edit-profile form | Implemented |
| Admin users page (`/admin/users`) | Table of all users, ADMIN only | Implemented |
| React Router | Client-side navigation and redirects | Implemented |
| Axios | Central instance with auth-header and 401 interceptors | Implemented |
| Token handling | Stored in `localStorage`, attached to every request | Implemented |
| Protected routes | `ProtectedRoute` component (login required; optional admin-only) | Implemented |
| Logout | Navbar button → server-side logout → redirect | Implemented |

## API Summary

| Method | Path | Auth | Role |
|--------|------|------|------|
| POST | `/api/auth/register` | No | — |
| POST | `/api/auth/login` | No | — |
| POST | `/api/auth/logout` | Yes | Any |
| GET | `/api/dashboard/me` | Yes | Any |
| PUT | `/api/dashboard/me` | Yes | Any |
| GET | `/api/admin/users` | Yes | ADMIN |

## Default Account

`admin@userhub.local` / `ChangeMe123!` (ADMIN)

These are local demonstration defaults from `backend/src/main/resources/application.properties`. Environment variables `USERHUB_ADMIN_EMAIL` and `USERHUB_ADMIN_PASSWORD` can override them. Do not use demo credentials in production.


## PowerPoint endpoint alignment (updated)

| Feature | Endpoint | Status |
|---|---|---|
| Protected dashboard | `GET /api/dashboard` | Implemented; user-reported functional testing passed |
| ADMIN user list | `GET /api/dashboard/users` | Implemented; user-reported functional testing passed |
| Edit full name | `PUT /api/users/me` | Implemented; user-reported functional testing passed |
| Server-side logout | `POST /api/auth/logout` | Implemented; user-reported functional testing passed |
| Seed ADMIN | `CommandLineRunner` | Implemented; user-reported functional testing passed |

Screenshots must be captured from a running application before submission.
