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

The frontend includes registration/login, public listing search, bidder listing
creation, image upload, listing details and bid history, bidder actions, and
authenticated listing management. Register as `BIDDER` to post listings and place
bids.
