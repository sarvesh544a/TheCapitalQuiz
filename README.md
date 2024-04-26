# Capital Quiz Game
Welcome to the Capital Quiz Game, an engaging and educational app designed to test your knowledge of world capitals. In this game, you will have the opportunity to answer questions about the capitals of 10 randomly selected countries. Additionally, you can mark countries for review later and explore a comprehensive list of 250 countries and their capitals.

## Features

### Quiz Mode
● _**Randomly Selected Questions:**_ Each game session presents you with 10 questions, each asking for the capital of a randomly chosen country from a database of 250 countries.

● _**Immediate Feedback:**_ After submitting an answer, you'll receive instant feedback indicating whether your answer was correct or incorrect. If incorrect, the correct capital will be displayed.

● _**Scoring System:**_ Earn points for each correct answer and track your progress over time.

### Review Mode
● _**Bookmark Countries:**_ During the quiz, you have the option to mark any country for further review. This feature is useful for learning and mastering capitals that are challenging for you.

● _**Review List:**_ Access a list of countries you have marked for review, allowing you to focus on areas where you need improvement.

### Learn Mode
● _**Complete Country List:**_ Browse through an extensive list of all 250 countries and their capitals. This feature is perfect for studying and familiarizing yourself with world capitals outside of the quiz context.

## How to Use

### Getting Started
● _**Download and Install the App:**_ First, ensure you have downloaded and installed the app on your device.

● _**Open the App:**_ Launch the Capital Quiz Game from your device’s app menu.

### Playing the Quiz
● _**Start Quiz:**_ Tap on 'Start Quiz' to begin your game session.

● _**Answer Questions:**_ Each question will present a country name and 4 capital names as options. Submit your answer to see if you were correct.

● _**Mark for Review:**_ If unsure or incorrect, you can mark the country for later review by tapping the 'Mark for Review' button.

### Reviewing Marked Countries
● _**Access Review List:**_ From the main menu, select 'Review List' to see all countries you have marked for review.

●_**Study Capitals:**_ Use this list to study and memorize capitals at your own pace.

### Exploring the Country List
● _**View Country List:**_ Select 'Country List' from the main menu to browse through all available countries and their capitals.

● _**Search and Navigate:**_ Use the search bar to find specific countries or scroll through the list manually.



## App Code Details

### Main Screens and File names

● This app has an animated splash screen for launch - SplashScreen.kt
● This app has a custom launcher icon - world.png
● This app has one screen with a list using LazyColumn - CountryListScreen.kt and CountryInfoList.kt
● This app supports tapping an item in this list which navigates the user to a detail view - CountryDetailsScreen.kt
● This app makes a network call to https://restcountries.com/v3.1/all using Retrofit and Moshi - MainActivity.kt and CountryService.kt
● 










The app has a README file including a basic explanation of the app, as well as
explanations of how your app fulfills each of the rubric items. Identify your features and
any specific file names so that your mentor doesn’t have to search for them while
grading.
● .
● 


● The app has one or more network calls using a library like Retrofit to
download/upload data that relate to the core tasks of the app. The app’s repo does not
contain API keys or other authentication information: Don’t store API keys or other
authentication information in your app’s repo. For one option of doing this, see this video
on how you can utilize the secrets-gradle-platform created by the Google Maps Platform
team to store your API keys in the local.properties file. Just make sure to update the
README with information on what you need to add to the local.properties file so others
that check out your project can add their appropriate keys/secrets.
● If your API has a low request limit that your mentor might hit, highlight this in the
README and explain how to use your freeze-dried data.
● The app handles all typical errors related to network calls — including server error
response codes and no network connection — and keeps the user informed.
● The app uses at least one way to save user settings: PreferenceDataStore or
SharedPreferences. Specify your method in the README.
● The app uses Kotlin Coroutines (and look into properly scoped launches like
viewModelScope) appropriately to keep slow-running tasks off the main thread and to
update the UI on the main thread.
● The app communicates to the user whenever data is missing or empty, the reason for
that condition — for example, data cannot be loaded or the user hasn’t created any —
and action the user should perform in order to move forward. The app should have no
blank screens, and the user should never feel “lost”.
● All included screens work successfully without crashes or UI issues.
○ Views work for both landscape and portrait orientations, for the wide range of
common Android devices (mostly targeting Pixel devices and up-to-date
emulators running the latest Android OS).
○ App works on the latest 5 versions of Android OS.
○ Views work for both light and dark modes.
○ There are no obvious UI issues, like UI components overlapping or running off
the screen.
● The code should be organized and easily readable.
○ Project source files are organized in packages such as ui.components,
ui.screens, models, networking etc.
○ Composables should be in their own files, smaller sub-component composables
(not screen level composables) should each have their own Preview functions as
well.
○ The project uses MVVM or MVI architecture: The viewmodel preferably includes
only one StateFlow object that returns a “stateful” representation of the
associated data (loading, error, success) typically in the form of a sealed
class. Networking code is in a service interface (following the Retrofit pattern).
○ The project includes dependencies on either detekt or ktlint. Mentors will run
either one of these gradle tasks to ensure there’s no errors that are returned.
○ The app builds without Warnings or Errors. (Move your TODO warnings to a
different branch.)
● The project has a test plan for unit tests, with a minimum of 50% code coverage for
Methods, and all tests succeed. Make sure your test plan is in your repo.
○ Classes to have covered:
■ ViewModels
■ Repositories
■ Adapters
● The app includes:
○ A custom app icon.
○ At least 4 screens (for example: splash, list, detail, about).
○ A custom app name.
○ At least one Compose animation.
○ Make sure to note these in the README.
