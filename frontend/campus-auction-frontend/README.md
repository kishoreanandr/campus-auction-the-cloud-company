# Campus Auction frontend

## Run locally

Start the Spring Boot backend on port `8080`, then run:

```sh
npm install
npm run dev
```

The Vite development server proxies `/api` and `/uploads` to
`http://localhost:8080`. If the backend runs elsewhere, set `VITE_API_URL` to
its origin (without `/api`), for example `http://localhost:8081`, before starting
Vite. For a production frontend hosted separately, also configure the backend CORS
origins with `CORS_ALLOWED_ORIGINS` on the backend, for example
`https://auction.example.edu`.

The frontend includes registration/login, public listing search, listing creation
and image upload for `BIDDER` (seller/lister) accounts, and bidding for `USER`
(student/bidder) accounts. Sellers can manage their own listings; students can
view listing details and bid history. Signed-in students can enable browser
notifications; the app checks auction
changes every 30 seconds while open, shows in-app alerts in the foreground, and
uses browser pop-ups when the tab is in the background. Browser notifications
require permission and a secure browser context (HTTPS or localhost).
