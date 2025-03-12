<p align="center">  
CowryConverter is a simple Currency Conversion App demonstrates modern Android development with Hilt, Coroutines, Flow, Jetpack libraries, Retrofit Jetpack Compose and Material Design based on MVVM architecture.

<p align="center">
</p>
<img src="https://github.com/user-attachments/assets/3dc33b8c-45ca-4d73-8557-1a3a3c4b08b8" width="300" height="600"/>
<img src="https://github.com/user-attachments/assets/379cf1d6-f6c2-469c-a7f6-2f7dbf599e96" width="300" height="600"/>
<img src="https://github.com/user-attachments/assets/91347d59-cfce-4127-976a-46bce199dde8" width="300" height="600"/>

## Tech stack & Open-source libraries
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, StateFlow, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - Stateflow: An observable hot flow for emitting the latest data.
  - Kotlin Flow: A cold flow triggered by the collecting functions.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
  - Jetpack Compose for UI design layout.
- Architecture
  - MVVM Architecture (Model - View - ViewModel - Model)
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.
- [Retrofit](https://github.com/square/retrofit): A networking library for fetching data over the internet
- [Turbine](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- Material design guidelines
  
## Architecture
This app is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overall architecture here is composed of three layers; the presentation layer, the domain layer, the data layer according to the Clean Architecture paradigm. Each layer has dedicated components and they have each different responsibilities, as defined below:

### Presentation Layer
The presentation layer consists of the ui components as well as the view models to control the lifecyle of the application.

### Data Layer
The data Layer consists of repositories, which include business logic, such as querying data from the remote data source. It follows the [single source of truth](https://en.wikipedia.org/wiki/Single_source_of_truth) principle. Api used: [Fixer.io](https://fixer.io/)<br>

### Domain
The domain layer consists mainly of repostory interfaces, use cases and business logic as followed by the clean architecure principles.


## Prerequisites
To build this project, you require:

- Android Studio ladybug
- AGP 8.8.2
- Kotlin version 2.1.0,
- Get your api key from [Fixer.io](https://fixer.io/), paste it in your local.properties file as api-key, and rebuild your project.
