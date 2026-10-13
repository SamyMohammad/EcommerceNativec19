# Products API

Source: [Route E-commerce Postman docs](https://documenter.getpostman.com/view/5709532/2s93JqTRWN).
The Postman collection has no saved example responses, so the shapes below come from live calls (2026-10-13).
All endpoints here are public (no `token` header). Payloads are trimmed to representative fields; the details
response also contains a `reviews[]` array with reviewer names, which is deliberately omitted here and in the tests.

Base URL: `https://ecommerce.routemisr.com`

## GET /api/v1/products

Query (all optional):

| Param | Notes |
|---|---|
| `page` | 1-based. |
| `limit` | Default 40, **capped at 50**. The app asks for 20. |
| `category` | Category id. `category[in]=<id>` behaves the same. |
| `subcategory` | Sub-category id. `subcategory[in]=<id>` behaves the same. |

```json
{
  "results": 56,
  "metadata": { "currentPage": 1, "numberOfPages": 28, "limit": 2, "nextPage": 2 },
  "data": [
    {
      "_id": "6428ebc6dc1175abc65ca0b9",
      "id": "6428ebc6dc1175abc65ca0b9",
      "title": "Woman Shawl",
      "description": "Material\tPolyester Blend\nColour Name\tMulticolour\nDepartment\tWomen",
      "price": 149,
      "imageCover": "https://ecommerce.routemisr.com/Route-Academy-products/1680403397402-cover.jpeg",
      "images": [
        "https://ecommerce.routemisr.com/Route-Academy-products/1680403397482-1.jpeg",
        "https://ecommerce.routemisr.com/Route-Academy-products/1680403397482-2.jpeg"
      ],
      "ratingsAverage": 4.1,
      "ratingsQuantity": 46,
      "sold": 2841,
      "quantity": 220,
      "category": { "_id": "6439d58a0049ad0b52b9003f", "name": "Women's Fashion", "slug": "women's-fashion", "image": "https://ecommerce.routemisr.com/Route-Academy-categories/1681511818071.jpeg" },
      "subcategory": [ { "_id": "6407f1bcb575d3b90bf95797", "name": "Women's Clothing", "category": "6439d58a0049ad0b52b9003f" } ],
      "brand": { "_id": "64089bbe24b25627a253158b", "name": "DeFacto", "slug": "defacto", "image": "https://ecommerce.routemisr.com/Route-Academy-brands/1678285758109.png" },
      "createdAt": "2023-04-02T02:43:18.400Z",
      "updatedAt": "2026-10-13T15:47:29.937Z"
    }
  ]
}
```

A discounted product additionally carries `"priceAfterDiscount": 349` next to `"price": 499` (about 60% of the
catalogue; it was always below `price`). The key is absent otherwise.

### Pagination

- `results` is the **total** number of matching products, not the size of this page.
- `metadata.nextPage` is present only while there is a next page. On the last page it is missing and
  `prevPage` appears instead:
  `{ "currentPage": 28, "numberOfPages": 28, "limit": 2, "prevPage": 27 }`.
- A page past the end returns `200` with `"data": []`.

### Filters

- `category=<id>` and `subcategory=<id>` return only matching products (22 for "Men's Fashion", and the same 22
  for its sub-category "Men's Clothing").
- A sub-category with no products ("Bags & luggage") or an unknown id returns `{"results":0, ... "data":[]}` with `200`.
  The Empty state is therefore a common real path, not an edge case.

## GET /api/v1/products/{id}

The product is wrapped in `data` and there is no `results` / `metadata`. The fields are those of a list item plus
`__v` and `reviews`.

```json
{
  "data": {
    "_id": "6428ebc6dc1175abc65ca0b9",
    "title": "Woman Shawl",
    "description": "Material\tPolyester Blend\nColour Name\tMulticolour\nDepartment\tWomen",
    "price": 149,
    "imageCover": "https://ecommerce.routemisr.com/Route-Academy-products/1680403397402-cover.jpeg",
    "images": ["https://ecommerce.routemisr.com/Route-Academy-products/1680403397482-1.jpeg"],
    "ratingsAverage": 4.1,
    "ratingsQuantity": 46,
    "sold": 2841,
    "category": { "_id": "6439d58a0049ad0b52b9003f", "name": "Women's Fashion" },
    "brand": { "_id": "64089bbe24b25627a253158b", "name": "DeFacto" },
    "reviews": []
  }
}
```

Errors: an unknown but well-formed id returns `404 {"statusMsg":"fail","message":"No document for this id ..."}`;
a malformed id (`abc`) returns `400 {"message":"fail","errors":{...}}`. Both surface as `AppError.Server(code)`.

## Gotchas

- `price` and `priceAfterDiscount` are whole EGP integers (largest seen: 42,960). The app keeps them as `Int`.
- `ratingsAverage` is sometimes an integer (`4`) and sometimes a float (`4.1`); the DTO uses `Double`.
- Both an `_id` and an `id` are sent; the DTO reads `_id`.
- `images` can hold anywhere from 1 to 10 URLs and the cover is not part of it.
- `description` contains literal tabs and newlines and is shown as-is.
- There are **no size or colour variants**: `availableColors` appears as a key on some products but was never populated
  in the 50 products inspected, so the details screen has no option pickers.
- Only the fields the app reads are modelled in the DTOs; `Json { ignoreUnknownKeys = true }` tolerates the rest.
- The mapper drops a product with a blank id, a blank title or a missing/negative price, and ignores a discount that
  is not below the price.
