# MakeMyTrip Automation (Sprinto Assignment)

## Prerequisites
- Java 1.7 or higher
- Maven
- TestNG
- Selenium WebDriver
- ExtentReports (for reporting)

## Setup
1. Clone the repository.
2. Update `pom.xml` with necessary dependencies.
3. Run the tests

## Running Tests
1. To run the tests on suite level using xml file.
   - Open the testng.xml file, right click on it and run the tests.
2. To run the tests, navigate to the project directory and execute:
   ```bash
- mvn clean test

# Project Components
1. Configuration
  - config.properties: Contains configuration settings like browser type, URL, and test data.
2. Page Objects
  - DestinationPickerPage.java: Page object model for the destination picker page.
  - FlightsPage.java: Page object model for the flights page.
  - LandingPage.java: Page object model for the landing page.
  - Utilities
  - ExtentReporter.java: Utility class for generating ExtentReports.
  - MyListeners.java: TestNG listeners for reporting and logging.
  - UtilityClass.java: General utility methods used across the project.
3. Base Class
  - BaseClass.java: Contains the setup and teardown methods for initializing and quitting the WebDriver.
4. Test Cases
  - TestCases.java: Contains the test methods for the MakeMyTrip international trip planning feature.
5. testData
  - SprintoTestData.xlsx : Contains a set of data that is used in testCase_02.

# Test Cases
1. TestCase_01 is non parameterised testcase which accepts data from config.properties file.
2. TestCase_02 is Data Driven Testcase whish accepts data from excel sheet by using dataProvider annotation.

# Test Execution
The test cases are defined in the TestCases class. The main test scenario includes:
  - Navigating to the flights page.
  - Selecting the "From" city and "To" destination.
  - Selecting the departure month and adjusting the trip duration.
  - Initiating the search.
  - Retrieving flight prices and dates.
  - Selecting a suitable flight date.
  - Verifying the availability of flights for the selected date.
    
# TestNG Configuration
The TestNG configuration is defined in testng.xml. It specifies the test suite and the test class to be executed.

# Reporting
Test execution results are generated using ExtentReports and saved in the reports directory.
