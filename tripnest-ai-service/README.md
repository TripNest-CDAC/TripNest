# TripNest AI Service

This service answers only from active TripNest packages and future trips stored in MySQL.

It uses Gemini `gemini-3.5-flash`. Do not change this to the retired `gemini-2.5-flash-lite` model.

## Required IntelliJ environment variables

```text
TRIPNEST_DB_PASSWORD=root
GEMINI_API_KEY=your_Google_AI_Studio_key
```

Run `TripnestAiServiceApplication` on port `8084`. Then send a `POST` request to `http://localhost:8080/api/chat`:

```json
{ "message": "Show Goa trips under 20000" }
```
