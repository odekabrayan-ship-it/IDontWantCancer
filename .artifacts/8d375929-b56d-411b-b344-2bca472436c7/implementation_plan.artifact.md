# Implementation Plan - Production Refinement & Settings Implementation

This plan focuses on making the "I Don't Want Cancer" application production-ready by externalizing UI strings, enhancing accessibility, and implementing a functional Settings screen.

## User Review Required

> [!NOTE]
> This step involves refactoring many UI components to use `stringResource` instead of hardcoded strings. This is a standard production requirement.

## Proposed Changes

### 1. String Externalization

#### [MODIFY] [strings.xml](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/res/values/strings.xml)
- Add all UI strings currently hardcoded in Composables.
- Add strings for `SignalCategory` and `SignalImportance`.

#### [MODIFY] UI Components
Update the following files to use `stringResource()`:
- [HomeScreen.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/home/HomeScreen.kt)
- [AlertsScreen.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/alerts/AlertsScreen.kt)
- [SearchScreen.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/search/SearchScreen.kt)
- [SignalDetailScreen.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/signal/SignalDetailScreen.kt)
- [SignalComponents.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/components/SignalComponents.kt)
- [ReentryComponents.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/components/ReentryComponents.kt)
- [AgencyStateComponents.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/components/AgencyStateComponents.kt)

### 2. Settings Screen Implementation

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/Administrator/AndroidStudioProjects/IDontWantCancer2/app/src/main/java/com/idontwantcancer/app/presentation/settings/SettingsScreen.kt)
- Replace placeholder with a Material 3 list of settings.
- Sections:
    - **Notifications**: Frequency (Placeholder).
    - **Intelligence Sources**: List of enabled sources (e.g., "openFDA Food Recalls").
    - **Data Management**: Clear intelligence memory.
    - **About**: Version, Privacy Policy (URL), and Legal Information.

### 3. Accessibility Audit

#### [MODIFY] UI Components
- Add `contentDescription` to icons where missing or sub-optimal.
- Ensure `heading()` semantics are correctly applied to all screen and section titles.
- Verify that `selected` semantics are present for list items.

## Verification Plan

### Automated Tests
- **Gradle Build**: Ensure the project compiles after string extraction.
- **Unit Tests**: Verify no regressions in presentation mapping.

### Manual Verification
- Verify all screens render strings correctly from `strings.xml`.
- Test the new Settings screen UI.
- Verify Accessibility (TalkBack) behavior for key sections.
