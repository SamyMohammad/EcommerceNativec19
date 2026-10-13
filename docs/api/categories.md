# Categories API

Source: [Route E-commerce Postman docs](https://documenter.getpostman.com/view/5709532/2s93JqTRWN).
The Postman collection has no saved example responses, so the shapes below come from live calls (2026-10-13).
All endpoints here are public (no `token` header).

Base URL: `https://ecommerce.routemisr.com`

## GET /api/v1/categories

Query (optional): `limit`, `page`, `keyword`.

```json
{
  "results": 10,
  "metadata": { "currentPage": 1, "numberOfPages": 5, "limit": 2, "nextPage": 2 },
  "data": [
    {
      "_id": "6439d5b90049ad0b52b90048",
      "name": "Men's Fashion",
      "slug": "men's-fashion",
      "image": "https://ecommerce.routemisr.com/Route-Academy-categories/1681511865180.jpeg",
      "createdAt": "2023-04-14T22:37:45.627Z",
      "updatedAt": "2023-04-14T22:37:45.627Z"
    }
  ]
}
```

## GET /api/v1/subcategories?category={categoryId}

```json
{
  "results": 2,
  "metadata": { "currentPage": 1, "numberOfPages": 1, "limit": 40 },
  "data": [
    {
      "_id": "6407f243b575d3b90bf957ac",
      "name": "Men's Clothing",
      "slug": "men's-clothing",
      "category": "6439d5b90049ad0b52b90048",
      "createdAt": "2023-03-08T02:26:11.097Z",
      "updatedAt": "2023-04-14T23:10:29.386Z"
    }
  ]
}
```

## Gotchas

- `GET /api/v1/categories/{id}/subcategories` (listed in the Postman docs) **ignores the id** and returns all
  60 sub-categories. The app uses the `?category=` filter above instead.
- Sub-categories have **no image** field, while the Figma design shows an image per tile. The app renders a neutral
  placeholder (see `docs/design-decisions/categories.md`).
- Only the fields the app reads are modelled in the DTOs; `Json { ignoreUnknownKeys = true }` tolerates the rest.
