# Implementation Plan - Step 20: Real Home Screen Implementation

Replace the placeholder Home screen with a professional, calm intelligence briefing UI using `HomeViewModel` and `HomeUiState`.

## User Review Required

> [!IMPORTANT]
> This step introduces `androidx.hilt:hilt-navigation-compose` (1.4.0) and `androidx.lifecycle:lifecycle-runtime-compose` (2.11.0) dependencies to follow idiomatic Compose/Hilt patterns.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/gradle/libs.versions.toml)
- Add `androidx-hilt-navigation-compose` library.
- Add `androidx-lifecycle-runtime-compose` library.
- Update `lifecycleRuntimeKtx` version if needed (currently 2.6.1).

#### [MODIFY] [build.gradle.kts (app)](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/build.gradle.kts)
- Add the new dependencies.

### Presentation Layer - Home

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/home/HomeScreen.kt)
- Integrate `HomeViewModel` using `hiltViewModel()`.
- Use `collectAsStateWithLifecycle()` for `HomeUiState`.
- Implement a `Scaffold` with a `LazyColumn`.
- Implement specialized sections:
    - `HomeHeader`: App title and subtitle.
    - `BriefingStatusSection`: Human-friendly translation of `DailyBriefingStatus`.
    - `BriefingSummarySection`: "WHAT CHANGED?" title, headline, and summary.
    - `ImportantSignalsSection`: List of `Signal` items with title, summary, importance, and category.
    - `ActionSection`: "WHAT THIS MEANS FOR YOU" list of actions.
    - `HomeLoadingState`: Calm loading indicator.
    - `HomeErrorState`: Safe error message with a "Try again" button calling `retry()`.
    - `HomeClearState`: Calm state for when everything is clear.

## Verification Plan

### Automated Tests
- **Gradle Sync**: Verify new dependencies are resolved.
- **Gradle Build**: Ensure project compiles successfully.

### Manual Verification
- Launch the app and verify the Home screen renders the "Loading" state.
- Since there's no backend, it will likely transition to "Error". Verify the error message and "Try again" button.
- Verify that the bottom navigation still works.
- Verify visual hierarchy and Material 3 styling.
