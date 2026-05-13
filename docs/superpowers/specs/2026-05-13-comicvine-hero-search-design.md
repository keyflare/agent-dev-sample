# Comic Vine Hero Search Design

## Goal

Add hero search to the existing main screen. The user opens the mobile app, types a comic character name, sees character suggestions below the text field after a 500 ms debounce, and can press a Search button to run the same search immediately.

This iteration implements only search suggestions. It does not add the hero detail screen, detail navigation, image rendering, or full character description display.

## API

Use Comic Vine Search:

```text
GET https://comicvine.gamespot.com/api/search/
```

Query parameters:

```text
api_key=<COMICVINE_API_KEY value passed from platform shell>
format=json
query=<user input>
resources=character
limit=10
field_list=id,name,api_detail_url,resource_type
```

The API key is already available to local builds as `COMICVINE_API_KEY`. The implementation must not hardcode it. Android and iOS platform shells will read the environment-backed build setting and pass it into shared code through `ExchangePlatformDependencies`.

Comic Vine response handling:

- Treat `status_code == 1` as success.
- Treat other status codes as user-visible search failure.
- Treat blank queries as no-op and clear suggestions.
- Decode JSON with unknown fields ignored.
- Keep `api_detail_url` in the suggestion model so a later detail-screen task can fetch `image`, `deck`, and `description` from the character detail resource without changing the search contract.

## Architecture

Add a shared KMP Comic Vine client/repository and inject it into `feature:main-screen:impl`.

Responsibilities:

- `core:comicvine`: API DTOs, Ktor client calls, repository interface, production repository implementation, and small domain models for character search suggestions.
- `app:shared`: owns the existing Ktor `HttpClient`, creates the Comic Vine repository, and passes it to `MainScreenDi`.
- `feature:main-screen:impl`: owns query state, debounce, loading/error state, stale-response protection, and UI events.
- `feature:main-screen:ui`: Android Compose rendering for the search field, button, and suggestion list.
- `app/ios/ios/src/Feature/MainScreen`: SwiftUI rendering for the same state and events.

No new DI framework is introduced. The existing manual DI style is preserved.

## Main Screen Behavior

State:

- `query`: current text in the search field.
- `suggestions`: up to 10 character suggestions.
- `isSearching`: true while a network request is active.
- `searchError`: nullable short message for invalid API key, network failure, or API error.

Events:

- `OnSearchQueryChanged(query: String)`: updates `query`, schedules a debounced search after 500 ms, or clears suggestions when blank.
- `OnSearchClick`: cancels pending debounce and runs search immediately for the current query.
- `OnSuggestionClick(id: Int)`: kept as a no-op for this iteration except for future navigation readiness.

Debounce and request ordering:

- A new query cancels the previous debounce job.
- A new request supersedes earlier in-flight requests.
- If an older response arrives after a newer request has started, it must be ignored.
- The Search button bypasses the remaining debounce delay.

## UI

Android:

- Keep the current header with debug/settings actions.
- Add a `DsTextField` under the header.
- Add a visible Search button.
- Render suggestions below the text field.
- Render loading and error states inline below the controls.

iOS:

- Keep the current header with debug/settings actions.
- Add a themed SwiftUI text field under the header.
- Add a Search button.
- Render suggestions, loading, and error states inline below the controls.

Both platforms should use the existing theme colors and typography. Do not add image loading in this task.

## Testing

Use TDD for shared behavior:

- `core:comicvine` tests with Ktor `MockEngine` for query parameters, success decoding, non-OK `status_code`, and network failure mapping.
- `feature:main-screen:impl` tests with a fake repository and coroutine test dispatcher for debounce, blank query clearing, immediate search button behavior, loading/error state, and stale-response protection.

Verification commands:

```bash
./gradlew :core:comicvine:allTests
./gradlew :feature:main-screen:impl:allTests
./gradlew :app:android:assembleDebug
```

For iOS, compile the Xcode project or shared framework when the local Xcode environment is available.

## Out Of Scope

- Hero detail screen.
- Character image display.
- HTML description cleanup.
- Pagination.
- Offline caching.
- Favorites or recent searches.
