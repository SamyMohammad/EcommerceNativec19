# Account API

Source: [Route E-commerce Postman docs](https://documenter.getpostman.com/view/5709532/2s93JqTRWN).
Shapes and rules below come from live calls (2026-10-13) made with a throwaway account that was restored afterwards.
Every identifier, email, phone and token in this file is a placeholder.

Base URL: `https://ecommerce.routemisr.com`. Every endpoint needs the JWT in the **`token`** header (see
[`auth.md`](auth.md)).

## GET /api/v1/users/getMe

The only endpoint that returns the full profile (the sign-in response and the JWT carry just name and role).

200:

```json
{
  "data": {
    "_id": "<id>", "role": "user", "active": true, "name": "Jane Doe", "email": "jane.doe@example.com",
    "phone": "01000000000", "wishlist": [],
    "addresses": [ { "_id": "<id>", "name": "Home", "details": "123 Example Street", "phone": "01000000000", "city": "Cairo" } ],
    "password": "<bcrypt hash>", "createdAt": "...", "updatedAt": "..."
  }
}
```

- **The body contains the stored password hash.** The app's DTO does not declare it, and `/users/` requests are logged
  headers-only.
- `addresses` is embedded, so no second call is needed. It may be empty.

## PUT /api/v1/users/updateMe

Body: any subset of `{ "name", "email", "phone" }`. 200:

```json
{ "message": "success", "user": { "name": "Jane Doe", "email": "jane.doe@example.com", "role": "user" } }
```

The answer has no `phone`, so the app merges the sent fields into its copy instead of trusting the response.

| Case | Result |
|---|---|
| Empty body, or only unchanged name/phone | 200, nothing changes. |
| **`email` equal to the current one** | 400 `"E-mail already in use"`. Send the e-mail only when it changed. |
| `email` used by another account | 400 `"E-mail already in use"` (a 400, not the 409 that sign-up uses). |
| Invalid `phone` | 400 `"accept only egypt phone numbers"` (same rule as sign-up). |
| No / bad token | 401 (see `auth.md`). |

400 body: `{ "message": "fail", "errors": { "value": "<input>", "msg": "<reason>", "param": "<field>" } }`; `value`
echoes the input, so bodies are never logged.

## PUT /api/v1/users/changeMyPassword

Body: `{ "currentPassword", "password", "rePassword" }`. 200 returns a **new token**:

```json
{ "message": "success", "user": { "name": "Jane Doe", "email": "jane.doe@example.com", "role": "user" }, "token": "<jwt>" }
```

- **The old token stops working immediately** (401 `"User recently changed password! Please login again."`), so the
  client must store the returned token before its next request.
- Wrong `currentPassword`: 400 `"Incorrect current password"` (`param: "password"`).
- `rePassword` is required (`"rePassword required"`) and must equal `password` (`"Password confirmation is incorrect"`).
- **No length rule on `password`:** a 3-character value was accepted (and the test account then had to be reset). The
  6-character minimum exists only in the app (`AuthValidation.newPassword`).

## /api/v1/addresses

| Call | Body | Answer |
|---|---|---|
| `GET /addresses` | none | `{ "results": 0, "status": "success", "data": [ <address> ] }` |
| `POST /addresses` | `{ "name", "details", "phone", "city" }` | `{ "status": "success", "message": "Address added successfully", "data": [ <whole list> ] }` |
| `DELETE /addresses/{id}` | none | `{ "status": "success", "message": "Address removed successfully", "data": [ <remaining list> ] }` |

- **The server validates nothing:** `{ "name": "Home" }` alone was accepted and stored. Multiple addresses are allowed.
- New addresses are appended, so the last entry is the newest.
- The app sends `name: "Home"`, the typed line as `details` and the profile phone; it never sends `city`.

## Endpoints the app must not use

`GET /api/v1/users` (no id) lists **other people's** names and e-mails to any signed-in user. It is not used and must
never be: it is a data exposure of the public demo server, not a profile source.
