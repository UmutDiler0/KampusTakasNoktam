# Kampüs Takas Noktam

## Purpose of Project
Kampüs Takas Noktam is a modern Android application designed for university students to swap, sell, and buy items easily within their campus network. The platform encourages a circular economy among students, allowing them to trade items like textbooks, electronics, and daily essentials safely and efficiently.

## Technologies & Libraries Used
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger Hilt
- **Network**: Retrofit, OkHttp, Gson
- **Asynchronous Programming**: Kotlin Coroutines, Flow (StateFlow/SharedFlow)
- **Image Loading**: Coil
- **Local Storage**: DataStore (for tokens/preferences), Room
- **Navigation**: Jetpack Navigation Compose

## Project Structure
The application follows a feature-based package structure to keep the codebase organized and scalable:

- `auth/` - Authentication flow (Login and Registration)
- `base/` - Base structural classes, ViewModels, and generic UiState wrappers
- `data/` - Data layer containing API Services, Repositories, DataStore managers, and Data Models
- `datastore/` - Local DataStore preferences (e.g., TokenManager)
- `navigation/` - Centralized routing logic (`AppNavHost` and destinations)
- `onboarding/` - Initial tutorial and onboarding screens
- `splash/` - Splash screen for initialization and token validation
- `ui/` - Main feature modules containing Composables and ViewModels:
  - `additem/` - Adding new advertisements
  - `basket/` - User's shopping cart functionality
  - `chat/` - In-app messaging system and conversation details
  - `editad/` - Editing existing user advertisements
  - `home/` - Main feed, search functionality, advanced filtering, and favorites
  - `itemdetail/` - Detailed view of specific advertisements
  - `myads/` - Management dashboard for the user's own items
  - `profile/` - User profile and sign-out logic
  - `seller/` - Viewing other sellers' profiles and their items
  - `settings/` - Application settings

## Screens
1. **Splash & Onboarding**: Initial app launch sequence and welcome tutorial.
2. **Auth (Login/Register)**: Secure user authentication.
3. **Home**: Primary feed displaying available items. Users can search by keywords and apply advanced filters (Category, Condition, Price Range, Swap options).
4. **Favorites**: Quick access to items the user has liked.
5. **Add Item**: Interface to upload images, set prices, and describe new items for sale/swap.
6. **My Ads**: A dedicated screen where users can view, update, and remove their active listings.
7. **Item Detail**: A comprehensive view of an item including an image carousel, seller information, item specifics, and actions like "Add to Basket", "Edit" (if owned by user), or "Send Message".
8. **Basket**: Cart functionality for organizing items the user intends to get.
9. **Chat**: Integrated messaging to negotiate and finalize swaps/purchases.
10. **Profile**: Personal profile overview.
