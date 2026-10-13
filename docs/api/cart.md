# Cart API

Source: [Route E-commerce Postman docs](https://documenter.getpostman.com/view/5709532/2s93JqTRWN).
Shapes below come from live calls (2026-10-13) made with a throwaway account. Identifiers are real catalogue product
ids (public data); the token is a placeholder.

Base URL: `https://ecommerce.routemisr.com`

Every endpoint needs the `token` header (`<jwt>`, see [auth.md](auth.md)). Without it the server answers
`401 { "statusMsg": "fail", "message": "You are not logged in. Please login to get access" }`.

## One envelope, two product shapes

Add, update, remove and get all answer with the **whole cart** after the change:

```json
{
  "status": "success",
  "numOfCartItems": 1,
  "cartId": "<id>",
  "data": {
    "_id": "<id>",
    "cartOwner": "<id>",
    "products": [ { "count": 3, "_id": "<line id>", "product": <see below>, "price": 149 } ],
    "totalCartPrice": 447
  }
}
```

- `numOfCartItems` is the number of **lines**, not units (three shawls in one line is `1`).
- `price` is the **unit** price of the line; `totalCartPrice` is the sum of `price * count`. The app shows the
  server's total and never trusts its own multiplication once an answer arrives.
- An empty cart has no `cartId`: `{ "status": "success", "numOfCartItems": 0, "data": { "products": [], "totalCartPrice": 0 } }`.

`product` differs by endpoint:

| Endpoint | `product` |
|---|---|
| `POST /cart` | the bare **id string**: `"product": "6428ebc6dc1175abc65ca0b9"` |
| `GET /cart`, `PUT /cart/{id}`, `DELETE /cart/{id}` | a populated **object** |

```json
{
  "subcategory": [{ "_id": "6407f1bcb575d3b90bf95797", "name": "Women's Clothing", "slug": "women's-clothing", "category": "6439d58a0049ad0b52b9003f" }],
  "_id": "6428ebc6dc1175abc65ca0b9",
  "id": "6428ebc6dc1175abc65ca0b9",
  "title": "Woman Shawl",
  "imageCover": "https://ecommerce.routemisr.com/Route-Academy-products/1680403397402-cover.jpeg",
  "category": { "_id": "6439d58a0049ad0b52b9003f", "name": "Women's Fashion", "slug": "women's-fashion", "image": "..." },
  "brand": { "_id": "64089bbe24b25627a253158b", "name": "DeFacto", "slug": "defacto", "image": "..." },
  "ratingsAverage": 4.1,
  "quantity": 220
}
```

The populated product is a **subset**: no price, no description, no `images`. `quantity` (stock) is present on GET and
DELETE but not on PUT. The app decodes a bare id as an object with only `_id` (`CartProductSerializer`), and
shows only lines that have a title, so a POST answer cannot fill the screen by itself.

## GET /api/v1/cart

The current cart (shape above). Line order is insertion order; re-adding a removed product puts it last.

## POST /api/v1/cart

```json
{ "productId": "6428ebc6dc1175abc65ca0b9" }
```

- **Adds ONE unit.** A product already in the cart is **incremented by one** (`count` 1, then 2 on the second call),
  so this call is not idempotent. There is no way to add a quantity in one request.
- Unknown product id: `404 { "statusMsg": "fail", "message": "No product exist for this id: <id>" }`.

## PUT /api/v1/cart/{productId}

```json
{ "count": 3 }
```

Sets the line's count (an absolute value, not a delta); idempotent.

- `count` of `0` **or a negative number** removes the line and answers 200.
- A product that is **not in the cart** answers `500 { "statusMsg": "error", "message": "No Product Cart item found for this id: <id>" }`
  (a 500, not a 404). Add it with POST first.

## DELETE /api/v1/cart/{productId}

Removes the line; answers with the remaining cart (populated). **Idempotent:** removing a product that is not in the
cart answers 200 with the cart unchanged.

## DELETE /api/v1/cart

Clears the cart: `200 { "message": "success" }` (no cart body). The app does not call it yet.

## How the app uses it

`CartRepositoryImpl` keeps the last cart the server sent plus the counts the user has asked for but the server has not
confirmed. See `docs/design-decisions/cart.md`.

- GET on sign-in and every time the Cart screen opens.
- "Add to cart" is POST (+1), then PUT with the wanted count when more than one unit was wanted, or GET otherwise,
  because POST's answer has no titles or images. The PUT/GET answer replaces the cache.
- Count changes are PUT (DELETE for 0 or removal), sent optimistically; the answer replaces the cache.
