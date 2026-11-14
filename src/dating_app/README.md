# Dating application LLD

Simple Java 17, in-memory implementation of the supplied UML. Classes are grouped into entities, enums, strategy, observer and factory packages. No frameworks or external dependencies.

Run `dating_app.Main` in IntelliJ, or from the project root in PowerShell:

```powershell
$sources = Get-ChildItem src/dating_app -Recurse -Filter *.java | ForEach-Object FullName
javac -d out $sources
java -cp out dating_app.Main
```

- `DatingApp`: registers users, finds candidates and handles mutual swipes.
- `User`, `UserProfile`, `Preference`, `Interest`, `Location`: user data and swipe history.
- `Matcher` implementations: basic, preferred-interest count and distance ranking.
- `MatcherFactory`: creates the selected matching strategy.
- `LocationStrategy` and `BasicLocationStrategy`: simple nearby-user lookup.
- `LocationService` and `NotificationService`: singleton services.
- `NotificationObserver` and `UserNotificationObserver`: console notifications.
- `ChatRoom` and `Message`: participant-only, in-memory chat.

Candidate discovery checks both users' age, gender and distance preferences,
excludes the current user and already swiped profiles, then sorts by the chosen
matcher. Interest preferences affect ranking, not eligibility. Empty gender
preferences accept any gender. A higher match score ranks first.

Only mutual right swipes create a chat room. Repeated swipes reuse that room.
Changing a swipe does not delete an existing match. Use `DatingApp.swipe` for
the full flow; `User.swipe` only records history. As a simple LLD example, direct
swipes require registered users but do not enforce discovery preferences.

The UML's abbreviated method names are expanded for readability. Java lists
replace vectors, and message timestamps use `LocalDateTime`. Singleton services
assume one application instance. Persistence, authentication, concurrency,
unmatching and real notification delivery are outside this example's scope.

## Package structure

- entities: user/profile/preferences, interests, location, chat and messages.
- enums: gender, swipe action and matcher type.
- strategy: matching and nearby-user lookup interfaces and implementations.
- observer: notification observers.
- factory: matcher creation.
- dating_app: application coordinator, services and runnable Main.

