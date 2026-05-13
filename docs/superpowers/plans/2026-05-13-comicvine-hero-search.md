# Comic Vine Hero Search Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add debounced Comic Vine character search suggestions to the existing main screen on Android and iOS.

**Architecture:** Add a shared `core:comicvine` KMP module for Comic Vine API access, inject it from `app:shared` into `feature:main-screen:impl`, and render the resulting shared state in Android Compose plus native iOS SwiftUI. Keep the feature limited to search suggestions; the detail screen remains out of scope.

**Tech Stack:** Kotlin Multiplatform, Ktor client, kotlinx.serialization, coroutines, Compose Multiplatform, SwiftUI, existing manual DI.

---

## File Structure

- Modify `settings.gradle.kts`: include `core:comicvine`.
- Create `core/comicvine/build.gradle.kts`: KMP module with Ktor, serialization, coroutines, tests, and `MockEngine`.
- Create `core/comicvine/src/commonMain/kotlin/com/keyflare/exchange/core/comicvine/ComicVineCharacterSearchResult.kt`: public domain model.
- Create `core/comicvine/src/commonMain/kotlin/com/keyflare/exchange/core/comicvine/ComicVineCharactersRepository.kt`: public repository interface.
- Create `core/comicvine/src/commonMain/kotlin/com/keyflare/exchange/core/comicvine/ComicVineCharactersRepositoryImpl.kt`: Ktor-backed implementation.
- Create `core/comicvine/src/commonMain/kotlin/com/keyflare/exchange/core/comicvine/internal/ComicVineDto.kt`: internal response DTOs.
- Create `core/comicvine/src/commonTest/kotlin/com/keyflare/exchange/core/comicvine/ComicVineCharactersRepositoryImplTest.kt`: API mapping tests.
- Modify `app/shared/build.gradle.kts`: depend on and export `core:comicvine`.
- Modify `app/shared/src/commonMain/kotlin/com/keyflare/exchange/api/ExchangePlatformDependencies.kt`: add `comicVineApiKey`.
- Modify `app/android/build.gradle.kts` and `app/android/src/main/java/com/keyflare/exchange/android/AndroidApp.kt`: expose `COMICVINE_API_KEY` as `BuildConfig.COMICVINE_API_KEY`.
- Modify `app/ios/ios/Info-Debug.plist`, `app/ios/ios/Info-Release.plist`, `app/ios/ios.xcodeproj/project.pbxproj`, and `app/ios/ios/src/AppDelegate.swift`: expose `COMICVINE_API_KEY` to shared code.
- Modify `app/shared/src/commonMain/kotlin/com/keyflare/exchange/impl/di/RootDi.kt`: create and pass the repository.
- Modify `feature/main-screen/impl/build.gradle.kts`: depend on `core:comicvine`.
- Modify `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenDi.kt`: add repository and dispatcher.
- Modify `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenUiEvent.kt`: add search events.
- Modify `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenViewState.kt`: add search state.
- Modify `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/internal/MainScreenState.kt`: add internal state.
- Modify `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenViewModel.kt`: implement debounce and repository calls.
- Create `feature/main-screen/impl/src/commonTest/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenViewModelTest.kt`: debounce and state tests.
- Modify `feature/main-screen/ui/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenView.kt`: pass search events.
- Modify `feature/main-screen/ui/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/internal/MainScreenPureView.kt`: Android search UI.
- Modify `app/ios/ios/src/Feature/MainScreen/MainScreenView.swift`: iOS search UI.

---

### Task 1: Add `core:comicvine` Module

**Files:**
- Modify: `settings.gradle.kts`
- Create: `core/comicvine/build.gradle.kts`

- [ ] **Step 1: Register the module**

Add this line near the other `core:*` includes:

```kotlin
include("core:comicvine")
```

- [ ] **Step 2: Create the module build file**

Create `core/comicvine/build.gradle.kts`:

```kotlin
plugins {
    applyFor(androidLibrary = true, multiplatform = true)
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.kotlinxSerialization)
        implementation(libs.ktor.clientCore)
        implementation(libs.ktor.clientContentNegotiation)
        implementation(libs.ktor.kotlinxSerializationJson)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
        implementation(libs.coroutines.test)
        implementation(libs.ktor.clientMock)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.comicvine")
    iosLibrary(name = "ExchangeComicVine")
}
```

- [ ] **Step 3: Run module discovery**

Run:

```bash
./gradlew projects
```

Expected: `:core:comicvine` appears in the project list.

- [ ] **Step 4: Commit**

```bash
git add settings.gradle.kts core/comicvine/build.gradle.kts
git commit -m "chore: add comic vine core module"
```

---

### Task 2: Implement Comic Vine Repository with Tests

**Files:**
- Create: `core/comicvine/src/commonMain/kotlin/com/keyflare/exchange/core/comicvine/ComicVineCharacterSearchResult.kt`
- Create: `core/comicvine/src/commonMain/kotlin/com/keyflare/exchange/core/comicvine/ComicVineCharactersRepository.kt`
- Create: `core/comicvine/src/commonMain/kotlin/com/keyflare/exchange/core/comicvine/ComicVineCharactersRepositoryImpl.kt`
- Create: `core/comicvine/src/commonMain/kotlin/com/keyflare/exchange/core/comicvine/internal/ComicVineDto.kt`
- Create: `core/comicvine/src/commonTest/kotlin/com/keyflare/exchange/core/comicvine/ComicVineCharactersRepositoryImplTest.kt`

- [ ] **Step 1: Write repository tests first**

Create `ComicVineCharactersRepositoryImplTest.kt`:

```kotlin
package com.keyflare.exchange.core.comicvine

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ComicVineCharactersRepositoryImplTest {

    @Test
    fun searchCharacters_sendsExpectedQueryParametersAndMapsResults() = runTest {
        var requestedUrl = ""
        val client = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            engine {
                addHandler { request ->
                    requestedUrl = request.url.toString()
                    respond(
                        content = SUCCESS_RESPONSE,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
        val repository = ComicVineCharactersRepositoryImpl(
            httpClient = client,
            apiKey = "test-key",
        )

        val results = repository.searchCharacters("batman")

        assertEquals(1, results.size)
        assertEquals(
            ComicVineCharacterSearchResult(
                id = 1699,
                name = "Batman",
                apiDetailUrl = "https://comicvine.gamespot.com/api/character/4005-1699/"
            ),
            results.single(),
        )
        assertEquals(true, requestedUrl.contains("api_key=test-key"))
        assertEquals(true, requestedUrl.contains("format=json"))
        assertEquals(true, requestedUrl.contains("query=batman"))
        assertEquals(true, requestedUrl.contains("resources=character"))
        assertEquals(true, requestedUrl.contains("limit=10"))
        assertEquals(true, requestedUrl.contains("field_list=id%2Cname%2Capi_detail_url%2Cresource_type"))
    }

    @Test
    fun searchCharacters_throwsWhenApiStatusIsNotOk() = runTest {
        val repository = repositoryFor(ERROR_RESPONSE)

        assertFailsWith<ComicVineException> {
            repository.searchCharacters("batman")
        }
    }

    @Test
    fun searchCharacters_returnsEmptyListForBlankQuery() = runTest {
        val repository = repositoryFor(SUCCESS_RESPONSE)

        assertEquals(emptyList(), repository.searchCharacters(" "))
    }

    private fun repositoryFor(response: String): ComicVineCharactersRepository {
        val client = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            engine {
                addHandler {
                    respond(
                        content = response,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
        return ComicVineCharactersRepositoryImpl(client, apiKey = "test-key")
    }

    private companion object {
        const val SUCCESS_RESPONSE = """
            {
              "status_code": 1,
              "error": "OK",
              "number_of_total_results": 1,
              "number_of_page_results": 1,
              "limit": 10,
              "offset": 0,
              "results": [
                {
                  "id": 1699,
                  "name": "Batman",
                  "api_detail_url": "https://comicvine.gamespot.com/api/character/4005-1699/",
                  "resource_type": "character"
                }
              ]
            }
        """

        const val ERROR_RESPONSE = """
            {
              "status_code": 100,
              "error": "Invalid API Key",
              "results": []
            }
        """
    }
}
```

- [ ] **Step 2: Run tests and verify they fail**

Run:

```bash
./gradlew :core:comicvine:allTests
```

Expected: compilation fails because repository classes do not exist.

- [ ] **Step 3: Add domain model and interface**

Create `ComicVineCharacterSearchResult.kt`:

```kotlin
package com.keyflare.exchange.core.comicvine

public data class ComicVineCharacterSearchResult(
    val id: Int,
    val name: String,
    val apiDetailUrl: String,
)
```

Create `ComicVineCharactersRepository.kt`:

```kotlin
package com.keyflare.exchange.core.comicvine

public interface ComicVineCharactersRepository {
    public suspend fun searchCharacters(query: String): List<ComicVineCharacterSearchResult>
}

public class ComicVineException(message: String) : RuntimeException(message)
```

- [ ] **Step 4: Add DTOs**

Create `internal/ComicVineDto.kt`:

```kotlin
package com.keyflare.exchange.core.comicvine.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ComicVineSearchResponseDto(
    @SerialName("status_code")
    val statusCode: Int,
    val error: String,
    val results: List<ComicVineSearchResultDto> = emptyList(),
)

@Serializable
internal data class ComicVineSearchResultDto(
    val id: Int,
    val name: String,
    @SerialName("api_detail_url")
    val apiDetailUrl: String,
    @SerialName("resource_type")
    val resourceType: String,
)
```

- [ ] **Step 5: Add Ktor implementation**

Create `ComicVineCharactersRepositoryImpl.kt`:

```kotlin
package com.keyflare.exchange.core.comicvine

import com.keyflare.exchange.core.comicvine.internal.ComicVineSearchResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

public class ComicVineCharactersRepositoryImpl(
    private val httpClient: HttpClient,
    private val apiKey: String,
) : ComicVineCharactersRepository {

    override suspend fun searchCharacters(
        query: String,
    ): List<ComicVineCharacterSearchResult> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return emptyList()
        if (apiKey.isBlank()) throw ComicVineException("Comic Vine API key is missing")

        val response: ComicVineSearchResponseDto =
            httpClient.get("https://comicvine.gamespot.com/api/search/") {
                parameter("api_key", apiKey)
                parameter("format", "json")
                parameter("query", normalizedQuery)
                parameter("resources", "character")
                parameter("limit", SEARCH_LIMIT)
                parameter("field_list", "id,name,api_detail_url,resource_type")
            }.body()

        if (response.statusCode != OK_STATUS_CODE) {
            throw ComicVineException(response.error)
        }

        return response.results
            .filter { it.resourceType == "character" }
            .map {
                ComicVineCharacterSearchResult(
                    id = it.id,
                    name = it.name,
                    apiDetailUrl = it.apiDetailUrl,
                )
            }
    }

    private companion object {
        const val OK_STATUS_CODE = 1
        const val SEARCH_LIMIT = 10
    }
}
```

- [ ] **Step 6: Run tests and verify they pass**

Run:

```bash
./gradlew :core:comicvine:allTests
```

Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add core/comicvine
git commit -m "feat: add comic vine character search repository"
```

---

### Task 3: Pass Comic Vine API Key and Repository Through DI

**Files:**
- Modify: `app/shared/build.gradle.kts`
- Modify: `app/shared/src/commonMain/kotlin/com/keyflare/exchange/api/ExchangePlatformDependencies.kt`
- Modify: `app/shared/src/commonMain/kotlin/com/keyflare/exchange/impl/network/HttpClientFactory.kt`
- Modify: `app/shared/src/commonMain/kotlin/com/keyflare/exchange/impl/di/RootDi.kt`
- Modify: `app/android/build.gradle.kts`
- Modify: `app/android/src/main/java/com/keyflare/exchange/android/AndroidApp.kt`
- Modify: `app/ios/ios/Info-Debug.plist`
- Modify: `app/ios/ios/Info-Release.plist`
- Modify: `app/ios/ios.xcodeproj/project.pbxproj`
- Modify: `app/ios/ios/src/AppDelegate.swift`

- [ ] **Step 1: Export the new shared module**

In `app/shared/build.gradle.kts`, add:

```kotlin
api(projects.core.comicvine)
```

to `commonDeps`, and add:

```kotlin
projects.core.comicvine,
```

to `exportDependencies`.

- [ ] **Step 2: Make shared JSON tolerant**

In `HttpClientFactory.kt`, change common JSON setup to:

```kotlin
import kotlinx.serialization.json.Json

internal fun HttpClientConfig<*>.installCommonHttpClientPlugins() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
}
```

- [ ] **Step 3: Add API key to platform dependencies**

Add `comicVineApiKey` to `ExchangePlatformDependencies`:

```kotlin
public data class ExchangePlatformDependencies(
    val dataStorePlatform: DataStorePlatform,
    val buildType: AppBuildType,
    val comicVineApiKey: String = "",
    val analyticsAgent: AnalyticsAgent = NoOpAnalyticsAgent,
    val platformServices: PlatformServices = PlatformServices(),
    val openNetworkLogs: (() -> Unit)? = null,
)
```

Update the secondary constructor:

```kotlin
public constructor(
    dataStorePlatform: DataStorePlatform,
    buildType: AppBuildType,
    comicVineApiKey: String = "",
    openNetworkLogs: (() -> Unit)? = null,
) : this(
    dataStorePlatform = dataStorePlatform,
    buildType = buildType,
    comicVineApiKey = comicVineApiKey,
    analyticsAgent = NoOpAnalyticsAgent,
    platformServices = PlatformServices(),
    openNetworkLogs = openNetworkLogs,
)
```

- [ ] **Step 4: Wire Android env var**

In each Android `buildTypes` block in `app/android/build.gradle.kts`, add:

```kotlin
buildConfigField(
    "String",
    "COMICVINE_API_KEY",
    "\"${System.getenv("COMICVINE_API_KEY").orEmpty()}\"",
)
```

In `AndroidApp.kt`, pass:

```kotlin
comicVineApiKey = BuildConfig.COMICVINE_API_KEY,
```

inside `ExchangePlatformDependencies(...)`.

- [ ] **Step 5: Wire iOS env var**

Add this key to both plist files:

```xml
<key>ComicVineApiKey</key>
<string>$(COMICVINE_API_KEY)</string>
```

Add `COMICVINE_API_KEY = "$(COMICVINE_API_KEY)";` to Debug and Release build settings in `app/ios/ios.xcodeproj/project.pbxproj`.

In `AppDelegate.swift`, read and pass:

```swift
let comicVineApiKey =
    Bundle.main.object(forInfoDictionaryKey: "ComicVineApiKey") as? String ?? ""
```

then:

```swift
comicVineApiKey: comicVineApiKey,
```

inside `ExchangePlatformDependencies(...)`.

- [ ] **Step 6: Create repository in RootDi**

In `RootDi.kt`, add:

```kotlin
import com.keyflare.exchange.core.comicvine.ComicVineCharactersRepository
import com.keyflare.exchange.core.comicvine.ComicVineCharactersRepositoryImpl
```

Then create:

```kotlin
private val comicVineCharactersRepository: ComicVineCharactersRepository =
    ComicVineCharactersRepositoryImpl(
        httpClient = httpClient,
        apiKey = platformDependencies.comicVineApiKey,
    )
```

Pass it to `MainScreenDi`:

```kotlin
val mainScreenDi: MainScreenDi = MainScreenDi(
    buildType = platformDependencies.buildType,
    comicVineCharactersRepository = comicVineCharactersRepository,
)
```

- [ ] **Step 7: Run compile check**

Run:

```bash
./gradlew :app:shared:allTests
```

Expected: PASS or no tests executed without compilation errors.

- [ ] **Step 8: Commit**

```bash
git add app/shared app/android app/ios
git commit -m "chore: wire comic vine api key"
```

---

### Task 4: Implement Main Screen Search State with Tests

**Files:**
- Modify: `feature/main-screen/impl/build.gradle.kts`
- Modify: `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenDi.kt`
- Modify: `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenUiEvent.kt`
- Modify: `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenViewState.kt`
- Modify: `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/internal/MainScreenState.kt`
- Modify: `feature/main-screen/impl/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenViewModel.kt`
- Create: `feature/main-screen/impl/src/commonTest/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenViewModelTest.kt`

- [ ] **Step 1: Add module dependency**

In `feature/main-screen/impl/build.gradle.kts`, add:

```kotlin
implementation(projects.core.comicvine)
```

to `commonDeps`.

- [ ] **Step 2: Add view model tests first**

Create tests covering:

```kotlin
@Test fun query_change_waits_500ms_before_searching()
@Test fun search_click_runs_immediately()
@Test fun blank_query_clears_suggestions()
@Test fun stale_search_response_is_ignored()
@Test fun failed_search_sets_error_and_clears_loading()
```

Use a fake `ComicVineCharactersRepository` that records queries and can return controlled results from `CompletableDeferred`.

- [ ] **Step 3: Run tests and verify they fail**

Run:

```bash
./gradlew :feature:main-screen:impl:allTests
```

Expected: compilation fails because new state/events do not exist.

- [ ] **Step 4: Add DI fields**

Update `MainScreenDi.kt`:

```kotlin
package com.keyflare.exchange.feature.converter.api

import com.keyflare.common.utils.AppBuildType
import com.keyflare.exchange.core.comicvine.ComicVineCharactersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public class MainScreenDi(
    internal val buildType: AppBuildType,
    internal val comicVineCharactersRepository: ComicVineCharactersRepository,
    internal val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
)
```

- [ ] **Step 5: Add public view state models**

Update `MainScreenViewState.kt`:

```kotlin
package com.keyflare.exchange.feature.converter.api

import androidx.compose.runtime.Immutable

@Immutable
public data class MainScreenViewState(
    val query: String,
    val suggestions: List<HeroSearchSuggestionViewState>,
    val isSearching: Boolean,
    val searchError: String?,
)

@Immutable
public data class HeroSearchSuggestionViewState(
    val id: Int,
    val name: String,
)
```

- [ ] **Step 6: Add UI events**

Update `MainScreenUiEvent.kt`:

```kotlin
package com.keyflare.exchange.feature.converter.api

public sealed interface MainScreenUiEvent {
    public data object OnDebugPanelClick : MainScreenUiEvent
    public data object OnSettingsClick : MainScreenUiEvent
    public data class OnSearchQueryChanged(val query: String) : MainScreenUiEvent
    public data object OnSearchClick : MainScreenUiEvent
    public data class OnSuggestionClick(val id: Int) : MainScreenUiEvent
}
```

- [ ] **Step 7: Add internal state**

Update `MainScreenState.kt`:

```kotlin
package com.keyflare.exchange.feature.converter.internal

import com.keyflare.exchange.core.comicvine.ComicVineCharacterSearchResult

internal data class MainScreenState(
    val query: String,
    val suggestions: List<ComicVineCharacterSearchResult>,
    val isSearching: Boolean,
    val searchError: String?,
)
```

- [ ] **Step 8: Implement debounce and stale-response protection**

In `MainScreenViewModel.kt`, add a `searchJob`, incrementing request id, and these constants:

```kotlin
private var searchJob: Job? = null
private var latestSearchId: Long = 0L

private fun onSearchQueryChanged(query: String) {
    state.value = state.value.copy(query = query, searchError = null)
    searchJob?.cancel()
    if (query.isBlank()) {
        state.value = state.value.copy(
            suggestions = emptyList(),
            isSearching = false,
            searchError = null,
        )
        return
    }
    searchJob = viewModelScope.launch {
        delay(SEARCH_DEBOUNCE_MS)
        search(query)
    }
}

private fun onSearchClick() {
    searchJob?.cancel()
    val query = state.value.query
    if (query.isBlank()) return
    searchJob = viewModelScope.launch {
        search(query)
    }
}

private suspend fun search(query: String) {
    val searchId = ++latestSearchId
    state.value = state.value.copy(isSearching = true, searchError = null)
    runCatching {
        withContext(di.ioDispatcher) {
            di.comicVineCharactersRepository.searchCharacters(query)
        }
    }.onSuccess { results ->
        if (searchId != latestSearchId) return
        state.value = state.value.copy(
            suggestions = results,
            isSearching = false,
            searchError = null,
        )
    }.onFailure {
        if (searchId != latestSearchId) return
        state.value = state.value.copy(
            suggestions = emptyList(),
            isSearching = false,
            searchError = "Search failed",
        )
    }
}
```

Use:

```kotlin
const val SEARCH_DEBOUNCE_MS = 500L
```

- [ ] **Step 9: Run tests and verify they pass**

Run:

```bash
./gradlew :feature:main-screen:impl:allTests
```

Expected: PASS.

- [ ] **Step 10: Commit**

```bash
git add feature/main-screen/impl
git commit -m "feat: add hero search state"
```

---

### Task 5: Add Android Compose Search UI

**Files:**
- Modify: `feature/main-screen/ui/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/api/MainScreenView.kt`
- Modify: `feature/main-screen/ui/src/commonMain/kotlin/com/keyflare/exchange/feature/converter/internal/MainScreenPureView.kt`

- [ ] **Step 1: Pass search events**

Update `MainScreenView.kt` so `MainScreenPureView` receives:

```kotlin
onQueryChange = { viewModel.onUiEvent(MainScreenUiEvent.OnSearchQueryChanged(it)) },
onSearchClick = { viewModel.onUiEvent(MainScreenUiEvent.OnSearchClick) },
onSuggestionClick = { viewModel.onUiEvent(MainScreenUiEvent.OnSuggestionClick(it)) },
```

- [ ] **Step 2: Render search controls**

In `MainScreenPureView.kt`, add below the header:

```kotlin
DsTextField(
    value = state.value.query,
    onValueChange = onQueryChange,
    placeholder = "Search hero",
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
)

Button(
    onClick = onSearchClick,
    enabled = state.value.query.isNotBlank(),
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
) {
    Text("Search")
}
```

Render suggestions:

```kotlin
state.value.suggestions.forEach { suggestion ->
    Text(
        text = suggestion.name,
        style = CustomTheme.type.body1,
        color = CustomTheme.colors.textPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSuggestionClick(suggestion.id) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}
```

Render loading/error:

```kotlin
if (state.value.isSearching) {
    Text(
        text = "Searching...",
        style = CustomTheme.type.body2,
        color = CustomTheme.colors.textAdditional,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

state.value.searchError?.let { error ->
    Text(
        text = error,
        style = CustomTheme.type.body2,
        color = CustomTheme.colors.textAlert,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
```

- [ ] **Step 3: Run Android compile check**

Run:

```bash
./gradlew :feature:main-screen:ui:assembleDebug
```

Expected: PASS.

- [ ] **Step 4: Commit**

```bash
git add feature/main-screen/ui
git commit -m "feat: add android hero search ui"
```

---

### Task 6: Add iOS SwiftUI Search UI

**Files:**
- Modify: `app/ios/ios/src/Feature/MainScreen/MainScreenView.swift`

- [ ] **Step 1: Add local text binding**

In `MainScreenView`, add:

```swift
@State private var query: String = ""
```

Keep it synchronized from shared state:

```swift
.onChange(of: state.value.query) { _, newValue in
    if query != newValue {
        query = newValue
    }
}
```

- [ ] **Step 2: Add search field and button under the header**

Add:

```swift
TextField("Search hero", text: $query)
    .font(theme.type.body1)
    .foregroundColor(theme.color.textPrimaryVariant)
    .padding(.horizontal, 12)
    .frame(height: 48)
    .background(theme.color.surfaceSecondary)
    .clipShape(RoundedRectangle(cornerRadius: 12))
    .padding(.horizontal, 16)
    .onChange(of: query) { _, newValue in
        viewModel.onUiEvent(
            event: MainScreenUiEventOnSearchQueryChanged(query: newValue)
        )
    }

Button(
    action: {
        viewModel.onUiEvent(event: MainScreenUiEventOnSearchClick())
    }
) {
    Text("Search")
        .font(theme.type.body1)
        .foregroundColor(theme.color.textAction)
        .frame(maxWidth: .infinity)
        .frame(height: 44)
}
.disabled(state.value.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
.padding(.horizontal, 16)
```

- [ ] **Step 3: Render suggestions and states**

Add:

```swift
if state.value.isSearching {
    Text("Searching...")
        .font(theme.type.body2)
        .foregroundColor(theme.color.textAdditional)
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 16)
}

if let error = state.value.searchError {
    Text(error)
        .font(theme.type.body2)
        .foregroundColor(theme.color.textAlert)
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 16)
}

ForEach(state.value.suggestions, id: \.id) { suggestion in
    Button(
        action: {
            viewModel.onUiEvent(
                event: MainScreenUiEventOnSuggestionClick(id: suggestion.id)
            )
        }
    ) {
        Text(suggestion.name)
            .font(theme.type.body1)
            .foregroundColor(theme.color.textPrimary)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
    }
}
```

- [ ] **Step 4: Run shared build**

Run:

```bash
./gradlew :app:shared:assembleDebug
```

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/ios
git commit -m "feat: add ios hero search ui"
```

---

### Task 7: Final Verification

**Files:**
- Verify all changed files.

- [ ] **Step 1: Run narrow tests**

```bash
./gradlew :core:comicvine:allTests :feature:main-screen:impl:allTests
```

Expected: PASS.

- [ ] **Step 2: Run Android app build**

```bash
./gradlew :app:android:assembleDebug
```

Expected: PASS.

- [ ] **Step 3: Check git status**

```bash
git status --short --branch
```

Expected: branch `codex/add-comicvine-hero-search` with a clean worktree after commits.

- [ ] **Step 4: Manual smoke test**

Run the Android debug app with `COMICVINE_API_KEY` in the environment, type `batman`, wait at least 500 ms, and confirm suggestions appear. Press Search immediately after changing the text and confirm the request runs without waiting.
