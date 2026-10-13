# Auth API

Source: [Route E-commerce Postman docs](https://documenter.getpostman.com/view/5709532/2s93JqTRWN).
Shapes and rules below come from live calls (2026-10-13) made with a throwaway account. Every identifier, email and
token in this file is a placeholder.

Base URL: `https://ecommerce.routemisr.com`

Authenticated endpoints (cart, wishlist, account) expect the JWT in a **`token`** request header, not
`Authorization: Bearer`. The app redacts that header in logs (see `core/network/NetworkModule.kt`).

## POST /api/v1/auth/signup

```json
{ "name": "C19 Test", "email": "user@example.com", "password": "<password>", "rePassword": "<password>", "phone": "01000000000" }
```

201:

```json
{ "message": "success", "user": { "name": "C19 Test", "email": "user@example.com", "role": "user" }, "token": "<jwt>" }
```

Server-enforced rules (validated in this order, **only the first failing field is reported**):

| Field | Rule |
|---|---|
| `name` | At least 3 characters ("must be at least 3 chars"). No upper bound observed. |
| `email` | Valid email ("Invalid email "). |
| `password` | At least 6 characters. **No complexity rules** (`abcdef` is accepted). No upper bound observed. |
| `rePassword` | Must equal `password`. The error is attached to `param: "password"` ("Password confirmation is incorrect"). |
| `phone` | Egyptian mobile: `01[0125]` + 8 digits ("accept only egypt phone numbers"). `+20` prefix is also accepted; whitespace, other prefixes and wrong lengths are rejected. Not unique across accounts. |

Missing fields give the same validation error as invalid ones (for example "Email is required").

## POST /api/v1/auth/signin

```json
{ "email": "user@example.com", "password": "<password>" }
```

200 (same shape as sign-up, including the token):

```json
{ "message": "success", "user": { "name": "C19 Test", "email": "user@example.com", "role": "user" }, "token": "<jwt>" }
```

## GET /api/v1/auth/verifyToken

Header `token: <jwt>`.

200: `{ "message": "verified", "decoded": { "id": "<id>", "name": "C19 Test", "role": "user", "iat": 0, "exp": 0 } }`

The JWT payload carries `id`, `name`, `role`, `iat` and `exp`; the token lives about 90 days. The app does not call
this endpoint yet: the session is restored from local storage, and an expired or revoked token surfaces as a 401 on the
first authenticated request (which clears the session).

## Error shapes

Two different shapes, distinguished by the HTTP status:

| Status | Body | Meaning |
|---|---|---|
| 400 | `{ "message": "fail", "errors": { "value": "<input>", "msg": "<reason>", "param": "<field>", "location": "body" } }` | Validation. `errors` is a single **object** (not an array); `value` is omitted when the field was missing. Echoes the submitted value, including passwords, so never log bodies. |
| 401 | `{ "statusMsg": "fail", "message": "Incorrect email or password" }` | Sign-in with an unknown email or wrong password (indistinguishable by design). |
| 401 | `{ "statusMsg": "fail", "message": "You are not logged in. Please login to get access" }` | Protected endpoint without `token`. |
| 401 | `{ "statusMsg": "fail", "message": "invalid token. Please login to get access" }` | Malformed, expired or revoked `token`. |
| 409 | `{ "statusMsg": "fail", "message": "Account Already Exists" }` | Sign-up with an email that is already registered. |

Because the 400 body repeats the submitted password, the HTTP logger never prints bodies of `/auth/` requests.
