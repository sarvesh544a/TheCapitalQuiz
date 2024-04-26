# Capital Quiz Game
Welcome to the Capital Quiz Game, an engaging and educational app designed to test your knowledge of world capitals. In this game, you will have the opportunity to answer questions about the capitals of 10 randomly selected countries. Additionally, you can mark countries for review later and explore a comprehensive list of 250 countries and their capitals.

## Features

### Quiz Mode
+ _**Randomly Selected Questions:**_ Each game session presents you with 10 questions, each asking for the capital of a randomly chosen country from a database of 250 countries.
+ _**Immediate Feedback:**_ After submitting an answer, you'll receive instant feedback indicating whether your answer was correct or incorrect. If incorrect, the correct capital will be displayed.
+ _**Scoring System:**_ Earn points for each correct answer and track your progress over time.

### Review Mode
+ _**Bookmark Countries:**_ During the quiz, you have the option to mark any country for further review. This feature is useful for learning and mastering capitals that are challenging for you.
+ _**Review List:**_ Access a list of countries you have marked for review, allowing you to focus on areas where you need improvement.

### Learn Mode
+ _**Complete Country List:**_ Browse through an extensive list of all 250 countries and their capitals. This feature is perfect for studying and familiarizing yourself with world capitals outside of the quiz context.

## How to Use

### Getting Started
+ _**Download and Install the App:**_ First, ensure you have downloaded and installed the app on your device.
+ _**Open the App:**_ Launch the Capital Quiz Game from your device’s app menu.

### Playing the Quiz
+ _**Start Quiz:**_ Tap on 'Start Quiz' to begin your game session.
+ _**Answer Questions:**_ Each question will present a country name and 4 capital names as options. Submit your answer to see if you were correct.
+ _**Mark for Review:**_ If unsure or incorrect, you can mark the country for later review by tapping the 'Mark for Review' button.

### Reviewing Marked Countries
+ _**Access Review List:**_ From the main menu, select 'Review List' to see all countries you have marked for review.
+ _**Study Capitals:**_ Use this list to study and memorize capitals at your own pace.

### Exploring the Country List
+ _**View Country List:**_ Select 'Country List' from the main menu to browse through all available countries and their capitals.
+ _**Search and Navigate:**_ Use the search bar to find specific countries or scroll through the list manually.

## Code Quality and Contribution

### Dependencies

This project uses **detekt** and **ktlint** for static code analysis to maintain code quality:

- _**detekt:**_ A static code analysis tool for Kotlin that helps detect code smells and maintain clean code practices.
- _**ktlint:**_ An anti-bikeshedding Kotlin linter with built-in formatted.

### Running Static Analysis
To ensure high-quality code, contributors are encouraged to run static analysis tools before submitting pull requests:

● _**Run detekt:**_

./gradlew detekt

● _**Run ktlint:**_

./gradlew ktlintCheck



## App Code Details

### Main Screens and File names

* This app has an animated splash screen for launch - SplashScreen.kt
* This app has a custom launcher icon - world.png
* This app has one screen with a list using LazyColumn - CountryListScreen.kt and CountryInfoList.kt
* This app supports tapping an item in this list which navigates the user to a detail view - CountryDetailsScreen.kt
* This app makes a network call to https://restcountries.com/v3.1/all using Retrofit and Moshi - MainActivity.kt and CountryService.kt
* The app handles all typical errors related to network calls
* This app saves user settings using PreferenceDataStore
* The project has a test plan for unit tests, with a minimum of 50% code coverage for Methods, and all tests succeed.
  - SettingsViewModel
  - QuizViewModel
  - CountryListViewModel
  - CountryDetailsViewModel
  - CountryRepositoryImpl
  - CountryAdapter
* This App has the following -
  - At least 4 screens (for example: splash, list, detail, about)
  - A custom app name
  - At least one Compose animation





