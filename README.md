# 🔐 Java Auth API with JWT, Redis & MySQL

A backend authentication & authorization API built in Java using **Javalin**, **MySQL** (**MariaDB**), and **Redis**. It implements **JWT-based authentication with refresh token rotation**, role-based access control, and secure password storage.

Currently this is a backend-only project designed for API clients like Postman or mobile apps. CORS and frontend integration are not implemented yet.

---

## 🚀 Features

- ✅ Sign-up and login endpoints
- 🔐 Passwords hashed securely
- 🪪 Access tokens (15 min) and refresh tokens (7 days)
- 🔄 Refresh token rotation stored in Redis
- 🧠 Role-based access control:
  - `USER` (default)
  - `ADMIN` (can view all users)
  - `MODERATOR` (planned, not implemented)
- 📦 Protected endpoints:
  - `/profile` – returns current user's data
  - `/admin/users` – accessible only by admins
- 🛠 Admin user injected at app startup
- 🔐 Credentials and secrets loaded from `.env`

---

## 🔑 Auth Flow

1. **Sign-up**  
   → Response: a json with `id`, `username`, `role` and `joinedAt`  
   
2. **Login**
   → Response includes: `accessToken` and `refreshToken`

3. **Using the access token**  
   → Include it in the `Authorization` header as: `Bearer <accessToken>` to a protected enpoint like `/profile`

3. **Refreshing tokens**  
   → Send the `refreshToken` to `/auth/refresh`  
   → If valid, the old token is deleted from Redis and a new pair of tokens is issued  
   → You must manually call this endpoint; no auto-refresh is implemented yet

---

## 🧪 Example Endpoints

- `POST /auth/signup`  
- `POST /auth/login`  
- `POST /auth/refresh`  
- `GET /profile` *(requires access token)*  
- `GET /admin/users` *(requires ADMIN role)*

---

## 🧰 How to Run

1. Make sure MySQL and Redis are running
2. Clone the repo
3. Create a `.env` file (see `.env.example`)
4. Build and run the project (via IDE or terminal)
5. Test it using Postman or similar

---

## 🚧 Known Limitations

- ❌ No frontend or CORS support yet
- ⏳ `MODERATOR` role is defined but not used
- 🔁 No automatic token refresh (manual call required)

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).  
Feel free to fork it, learn from it, or adapt it to your own use.

