# Q&A Assistant Android App (Dark Theme)

A native Android application in Kotlin (no Jetpack Compose) with a modern dark UI. Users can enter questions, see answers, and browse conversation history. Uses Retrofit and OkHttp for backend REST API calls.

## Build and Run

- Build: `./gradlew build`
- Install debug on connected device/emulator: `./gradlew :app:installDebug`
- Launch the app named "Q&A Assistant"

## Backend Configuration

The app reads the backend base URL from the environment variable BACKEND_BASE_URL at runtime/build time. If not provided, it defaults to `http://10.0.2.2:8000` (Android emulator loopback to host).

Example:
- BACKEND_BASE_URL=https://your-backend.example.com

Ensure your backend provides:
- POST /api/ask
  - Request: { "question": "..." }
  - Response: { "answer": "..." }

## Features

- Top app bar with title
- Central bottom input bar to ask questions
- RecyclerView shows conversation (user, assistant, system messages)
- Modern dark theme with:
  - Primary: #1976D2
  - Accent: #FFC107
  - Secondary: #424242

## Notes

- No Jetpack Compose; traditional Views + XML layouts
- Uses AndroidX and Material components