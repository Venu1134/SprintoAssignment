package com.sprinto.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.AssertJUnit;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import com.sprinto.baseClass.BaseClass;
import com.sprinto.pageObjects.DestinationPickerPage;
import com.sprinto.pageObjects.FlightsPage;
import com.sprinto.pageObjects.LandingPage;
import com.sprinto.utils.UtilityClass;

public class TestCases extends BaseClass {

	public TestCases() {
		super();
	}

	public WebDriver driver;
	public SoftAssert softAssert;
	public LandingPage landingPage;
	public FlightsPage flightsPage;
	public DestinationPickerPage destinationPickerPage;

	@BeforeMethod
	public void setUp() {
		driver = initilizeBrowser(prop.getProperty("browser"));
		softAssert = new SoftAssert();
		landingPage = new LandingPage(driver);
		landingPage.clickOnCloseButton();
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void TestCase_01() throws InterruptedException {

		// Step 1 :- Navigate to Flights page and verify user is on flights page
		flightsPage = landingPage.navigateToFlightsPage();
		boolean flightPageStatus = flightsPage.verifyUserIsOnFlightsPage(prop.getProperty("flightsURL"));
		AssertJUnit.assertTrue(flightPageStatus);

		// Step 2 :- Select "From" as "Bengaluru"
		flightsPage.selectFromCityFromDorpDown(prop.getProperty("fromCity"));

		// Step 3 :- Click on the "To" dropdown and choose "Planning a trip
		// internationally."
		destinationPickerPage = flightsPage.selectPlanningAnInternationalHoliday();
		boolean destinationPickerPageStatus = destinationPickerPage.verifyUserIsOnDestinationPickerPage();
		AssertJUnit.assertTrue(destinationPickerPageStatus);

		// Step 4 :- Set "To" as "Dubai."
		destinationPickerPage.selectToCityFromDestination(prop.getProperty("destination"));

		// Step 5 & 6 :- Click on "DATES&DURATION." and Select "December 2024" and
		// adjust the duration to 10 days.
		destinationPickerPage.selectDepartureMonth(prop.getProperty("tripType"), prop.getProperty("departureMonth"));

		// Step 7 :- Initiate the search.
		destinationPickerPage.clickOnSearchButton();

		// Step 8 :- Retrieve all the dates where the flight price is lower than the
		// median price for that month.
		List<Double> flightPrices = destinationPickerPage.getFlightPrices();
		List<String> flightDates = destinationPickerPage.getFlightDates();

		double medianPrice = destinationPickerPage.getMedianPrice(flightPrices);
		List<String> datesBelowMedian = destinationPickerPage.getDatesWithPricesBelowMedian(flightPrices, flightDates,
				medianPrice);

		// Step 9 & 10:- select a weekend date (Saturday or Sunday), if available. If a
		// weekend date is not available, choose the date with the lowest price.
		String selectedDate = destinationPickerPage.selectWeekendOrLowestPriceDate(datesBelowMedian, flightPrices);

		// Step 11 :- Verify that at least one flight is available for the selected
		// date.
		boolean isFlightAvailable = destinationPickerPage.isFlightAvailableForDate(selectedDate);
		softAssert.assertTrue(isFlightAvailable);
		softAssert.assertAll();
	}

	@Test(dataProvider="SearchTestData")
	public void TestCase_02(String FromCity, String Destination, String TripType, String DepartureMonth) throws InterruptedException {

		// Step 1 :- Navigate to Flights page and verify user is on flights page
		flightsPage = landingPage.navigateToFlightsPage();
		boolean flightPageStatus = flightsPage.verifyUserIsOnFlightsPage(prop.getProperty("flightsURL"));
		AssertJUnit.assertTrue(flightPageStatus);

		// Step 2 :- Select "From" as "Bengaluru"
		flightsPage.selectFromCityFromDorpDown(FromCity);

		// Step 3 :- Click on the "To" dropdown and choose "Planning a trip
		// internationally."
		destinationPickerPage = flightsPage.selectPlanningAnInternationalHoliday();
		boolean destinationPickerPageStatus = destinationPickerPage.verifyUserIsOnDestinationPickerPage();
		AssertJUnit.assertTrue(destinationPickerPageStatus);

		// Step 4 :- Set "To" as "Dubai."
		destinationPickerPage.selectToCityFromDestination(Destination);

		// Step 5 & 6 :- Click on "DATES&DURATION." and Select "December 2024" and
		// adjust the duration to 10 days.
		destinationPickerPage.selectDepartureMonth(TripType, DepartureMonth);

		// Step 7 :- Initiate the search.
		destinationPickerPage.clickOnSearchButton();

		// Step 8 :- Retrieve all the dates where the flight price is lower than the
		// median price for that month.
		List<Double> flightPrices = destinationPickerPage.getFlightPrices();
		List<String> flightDates = destinationPickerPage.getFlightDates();

		double medianPrice = destinationPickerPage.getMedianPrice(flightPrices);
		List<String> datesBelowMedian = destinationPickerPage.getDatesWithPricesBelowMedian(flightPrices, flightDates,
				medianPrice);

		// Step 9 & 10:- select a weekend date (Saturday or Sunday), if available. If a
		// weekend date is not available, choose the date with the lowest price.
		String selectedDate = destinationPickerPage.selectWeekendOrLowestPriceDate(datesBelowMedian, flightPrices);

		// Step 11 :- Verify that at least one flight is available for the selected
		// date.
		boolean isFlightAvailable = destinationPickerPage.isFlightAvailableForDate(selectedDate);
		softAssert.assertTrue(isFlightAvailable);
		softAssert.assertAll();
	}
	
	@DataProvider(name = "SearchTestData")
	public Object[][] getApplicationCreationData() {
		Object[][] applicationData = UtilityClass.getTestDataFromExcel("Sheet1");
		return applicationData;
	}
}
