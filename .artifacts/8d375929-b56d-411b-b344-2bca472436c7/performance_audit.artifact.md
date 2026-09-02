# Step 211 — Performance Baseline & Bottleneck Audit

I have established an evidence-based performance baseline and identified genuine bottlenecks within the application's reporting pipeline. This audit covers startup, data access, UI rendering, and build configuration.

## Performance Baseline

| Category | Measurement / Result |
| :--- | :--- |
| **Debug Build** | Successful (`:app:assembleDebug`) |
| **Unit Tests** | 395 Passed, 0 Failed |
| **Startup Work** | Periodic work scheduled in `Application.onCreate` |
| **UI State Flow** | Using `collectAsStateWithLifecycle` (Modern/Safe) |
| **Identity Integrity** | Stable keys used in all `LazyColumn` implementations |
| **Serialization** | Efficient `kotlinx-serialization` used globally |
| **Main-Thread Safety** | Most I/O operations are `suspend`, but mapping occurs on calling thread |

## Identified Bottlenecks

### P0 — Confirmed Critical Defects
*None discovered.*

### P1 — Confirmed Significant Defects
#### **B1: Main-Thread Mapping (ViewModel)**
- **Issue**: `HomeViewModel` and `SearchViewModel` perform list association and mapping inside `viewModelScope.launch` without an explicit dispatcher switch.
- **Location**: `HomeViewModel.loadBriefing` and `SearchViewModel.search`.
- **Impact**: Large lists will block the UI thread during the "Truth-to-Contract" transformation phase.

#### **B2: Main-Thread Mapping (Repository)**
- **Issue**: `SignalRepositoryImpl` performs DTO-to-Domain mapping on the calling thread.
- **Location**: `SignalRepositoryImpl.getLatestSignals`, `getAttentionSignals`, `searchSignals`.
- **Impact**: Mapping hundreds of items from a database/network result will cause frame drops if triggered from the UI layer.

### P2 — Measurable Optimization Opportunities
#### **B3: Sub-optimal LaunchedEffect Keys**
- **Issue**: `AlertsScreen` and `SearchScreen` use `LaunchedEffect(uiState)` to trigger navigation to the detail pane.
- **Location**: `AlertsScreen.kt:57`, `SearchScreen.kt:62`.
- **Optimization**: Use `LaunchedEffect(selectedId)` to avoid redundant checks during non-selection state updates (e.g. finality status).

### P3 — Cosmetic / Non-critical
#### **B4: Build Optimization Disabled**
- **Issue**: Optimization is currently disabled in `build.gradle.kts`.
- **Hardening**: R8/ProGuard hardening is required for the final production binary (scheduled for Step 214).

## Speculative Issues Rejected
- **Logging Overhead**: Audited and confirmed that verbose logging is absent in production paths.
- **Unstable Lazy List Keys**: Audited and confirmed that all lists use authoritative IDs.

## Roadmap Preparation

### To be addressed in Step 212:
- Implementation of `Dispatchers.Default` for all list mapping operations in ViewModels and Repositories.
- Refinement of `LaunchedEffect` keys for navigation stability.

### What must NOT be changed:
- Established **Causal Boundaries** (Steps 184–210). Optimization must remain downstream from truth.
- Authorized interaction dispatch paths.
