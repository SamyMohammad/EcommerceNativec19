# Wishlist API

Source: [Route E-commerce Postman docs](https://documenter.getpostman.com/view/5709532/2s93JqTRWN).
Shapes below come from live calls (2026-10-13) made with a throwaway account. Identifiers are real catalogue product
ids (public data); the token is a placeholder.

Base URL: `https://ecommerce.routemisr.com`

All three endpoints need the `token` header (`<jwt>`, see [auth.md](auth.md)). Without it the server answers
`401 { "statusMsg": "fail", "message": "You are not logged in. Please login to get access" }`.

## GET /api/v1/wishlist

Returns the **full products**, not ids. No paging: `metadata` is absent. An empty wishlist is
`{ "status": "success", "count": 0, "data": [] }`.

```json
{
  "status": "success",
  "count": 1,
  "data": [
    {
      "_id": "6428ebc6dc1175abc65ca0b9",
      "id": "6428ebc6dc1175abc65ca0b9",
      "title": "Woman Shawl",
      "price": 149,
      "imageCover": "https://ecommerce.routemisr.com/Route-Academy-products/1680403397402-cover.jpeg",
      "images": ["1680403397482-1.jpeg", "1680403397482-2.jpeg"],
      "ratingsAverage": 4.1,
      "ratingsQuantity": 46,
      "sold": 2841,
      "category": { "_id": "6439d58a0049ad0b52b9003f", "name": "Women's Fashion" },
      "brand": { "_id": "64089bbe24b25627a253158b", "name": "DeFacto" }
    }
  ]
}
```

Same product shape as [products.md](products.md) (the app reuses `ProductDto`), with one difference: `images[]`
holds bare **file names**, not URLs. `imageCover` is a full URL, which is what the Wishlist screen uses. Order is
insertion order.

## POST /api/v1/wishlist

```json
{ "productId": "6428ebc6dc1175abc65ca0b9" }
```

200 (the `data` array is the ids of the **whole wishlist** after the change, not just the added product):

```json
{ "status": "success", "message": "Product added successfully to your wishlist", "data": ["6428ebc6dc1175abc65ca0b9"] }
```

- **Idempotent:** adding a product that is already there returns 200 with the same ids.
- Unknown product id: `404 { "statusMsg": "fail", "message": "this product not found" }`.

## DELETE /api/v1/wishlist/{productId}

200, same envelope; `data` is the ids that remain (the message wording says "removed ... to your wishlist"):

```json
{ "status": "success", "message": "Product removed successfully to your wishlist", "data": ["6428eb43dc1175abc65ca0b3"] }
```

- **Idempotent:** removing a product that is not in the wishlist also returns 200.

## How the app uses it

`WishlistRepositoryImpl` keeps the favorite ids in memory. GET is used to load them (on sign-in) and to fill the
Wishlist screen; POST and DELETE are sent optimistically and rolled back if they fail. The response ids of POST and
DELETE are ignored: the app already knows what it asked for, and trusting them would overwrite concurrent toggles
of other products. Idempotency is what makes a retry or a rollback safe to repeat.
