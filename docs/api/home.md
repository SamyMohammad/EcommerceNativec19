# Home data

Home has no endpoint of its own. It reuses [categories](categories.md) and [products](products.md) through their existing
use cases; the `HomeViewModel` loads every region as a separate request, in parallel.

| Region | Request | Notes |
|---|---|---|
| Categories grid | `GET /api/v1/categories` | All 10 categories, each with an `image`. Two rows, scrolls horizontally. Every tile opens the Categories tab. |
| Best Sellers | `GET /api/v1/products?sort=-sold&limit=20` | First 10 shown. |
| New Arrival | `GET /api/v1/products?sort=-createdAt&limit=20` | First 10 shown. |
| Top Rated | `GET /api/v1/products?sort=-ratingsAverage&limit=20` | First 10 shown. |

`sort` is a standard query parameter of this API (a leading `-` means descending); verified live on 2026-10-13.
The domain exposes it as `ProductSort { BestSelling, Newest, TopRated }`, and the repository owns the translation to
the query string.

## Why rankings instead of categories

The Figma rows are "Home Appliance", "New Arrival" and "Smart Watch". The API cannot answer those: only 3 of the 10
categories contain products at all (Men's Fashion, Women's Fashion, Electronics; 56 products in total), and picking a
category by name at runtime would break as soon as the catalogue changes. A ranking works on any catalogue and the
section titles are honest about what the rows contain. The footwear banner and the promo carousel are static marketing
artwork (drawables), not API data.

## Behaviour

- Each region has its own `LoadState`; a failed request shows a retry in that region only and the others stay.
- A product row that loads but is empty is hidden together with its title.
- Product cards open Product details. Favorite hearts come from the shared wishlist cache (see wishlist.md).
- Cards use the taller design height (235) for every row because any product may carry an old price.
