package com.sprinto.pageObjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sprinto.utils.UtilityClass;

public class DestinationPickerPage {

    public WebDriver driver;
    public WebDriverWait wait;

    public DestinationPickerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(UtilityClass.webDriverTime));
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath="(//div[@class='makeFlex']//span)[1]")
    private WebElement tripTypeField;

    @FindBy(xpath="//ul[@class='options']//li")
    private List<WebElement> tripTypeList;

    @FindBy(xpath="(//span[text()='ANYWHERE'])[1]")
    private WebElement toField;

    @FindBy(xpath="(//input[@placeholder='Enter City'])[2]")
    private WebElement toTextField;

    @FindBy(xpath="//ul[@class='flex-v']//li")
    private List<WebElement> toFieldList;

    @FindBy(xpath="(//span[@class='value'])[4]")
    private WebElement DepartureField;

    @FindBy(xpath="//ul[@class='monthList']//li")
    private List<WebElement> monthOfTravel;

    @FindBy(xpath="//div[@class='rangeslider__fill']")
    private WebElement tripDurationSlider;

    @FindBy(xpath="//button[text()='Apply']")
    private WebElement applyButton;

    @FindBy(xpath="//button[text()='Search']")
    private WebElement searchButton;

    @FindBy(xpath="//ul[@class='tripFareCalDates']//p[@class='calPrice']")
    private List<WebElement> flightPrices;

    @FindBy(xpath="//ul[@class='tripFareCalDates']//p[contains(@class,'appendBottom3')]")
    private List<WebElement> flightDates;

    public boolean verifyUserIsOnDestinationPickerPage() {
        return driver.getCurrentUrl().contains("destinationPicker");
    }

    public void selectToCityFromDestination(String destination) throws InterruptedException {
        toField.click();
        toTextField.clear();
        toTextField.sendKeys(destination);
        Thread.sleep(1000);

        for (WebElement toCity : toFieldList) {
            if (toCity.getText().contains(destination)) {
                toCity.click();
                break;
            }
        }
    }

    public void selectDepartureMonth(String TripType, String departureMonth) throws InterruptedException {
        // To select trip type
        tripTypeField.click();
        for (WebElement tripType : tripTypeList) {
            if (tripType.getText().contains(TripType)) {
                tripType.click();
                break;
            }
        }
        
        DepartureField.click();
        Thread.sleep(1000);

        // To select departure month
        //wait.until(ExpectedConditions.visibilityOfAllElements(monthOfTravel));
        for (WebElement month : monthOfTravel) {
            if (month.getText().contains(departureMonth)) {
                month.click();
                break;
            }
        }

        // To select number of days
        Actions actions = new Actions(driver);
        actions.clickAndHold(tripDurationSlider).moveByOffset(100, 0).release().perform();

        applyButton.click();
    }

    public void clickOnSearchButton() {
        searchButton.click();
    }

    public List<Double> getFlightPrices() {
        List<Double> prices = new ArrayList<>();
        for (WebElement priceElement : flightPrices) {
            String priceText = priceElement.getText().replace("₹", "").replace(",", "").trim();
            if (!priceText.isEmpty() && priceText.matches("\\d+")) {
                try {
                    prices.add(Double.parseDouble(priceText));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price text: " + priceText);
                }
            }
        }
        return prices;
    }

    public List<String> getFlightDates() {
        List<String> dates = new ArrayList<>();
        for (WebElement dateElement : flightDates) {
            String dateText = dateElement.getText().trim();
            if (!dateText.isEmpty() && dateText.matches("\\d+")) {
                dates.add(dateText);
            }
        }
        return dates;
    }

    public double getMedianPrice(List<Double> prices) {
        Collections.sort(prices);
        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            return prices.get(size / 2);
        }
    }

    public List<String> getDatesWithPricesBelowMedian(List<Double> prices, List<String> dates, double medianPrice) {
        List<String> filteredDates = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            if (prices.get(i) < medianPrice) {
                filteredDates.add(dates.get(i));
            }
        }
        return filteredDates;
    }

    public String selectWeekendOrLowestPriceDate(List<String> dates, List<Double> prices) {
        if (dates.isEmpty() || prices.isEmpty()) {
            System.err.println("Error: Dates or prices list is empty.");
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        List<String> weekendDates = new ArrayList<>();
        for (String date : dates) {
            try {
                Date parsedDate = dateFormat.parse("07-" + date + "-2024");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    weekendDates.add(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!weekendDates.isEmpty()) {
            return weekendDates.get(0);
        } else {
            int minPriceIndex = prices.indexOf(Collections.min(prices));
            return dates.get(minPriceIndex);
        }
    }

    public boolean isFlightAvailableForDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date targetDate;
        try {
            targetDate = dateFormat.parse("07-" + date + "-2024");
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetDate);
        String formattedDate = new SimpleDateFormat("MMM dd, yyyy").format(calendar.getTime());

        List<WebElement> flightsForDate = driver.findElements(By.xpath("//div[@class='flight-details' and contains(., '" + formattedDate + "')]"));

        return !flightsForDate.isEmpty();
    }
}
