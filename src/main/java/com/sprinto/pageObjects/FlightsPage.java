package com.sprinto.pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sprinto.utils.UtilityClass;

public class FlightsPage {
	
	public WebDriver driver;
	public WebDriverWait wait;

	public FlightsPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(UtilityClass.webDriverTime));
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//label[@for='fromCity']")
	private WebElement fromCity;
	
	@FindBy(xpath="//input[@placeholder='From']")
	private WebElement fromTextField;
	
	@FindBy(xpath="//div[@id='react-autowhatever-1']//li[@role='option']")
	private List<WebElement> fromCityList;
	
	@FindBy(xpath="//label[@for='toCity']")
	private WebElement toCity;
	
	@FindBy(xpath="//p[@class='latoBold']")
	private WebElement internationalHolidayLink;
	
	public boolean verifyUserIsOnFlightsPage(String expectedURL) {
		return driver.getCurrentUrl().equals(expectedURL);
	}
	
	public void selectFromCityFromDorpDown(String from) {
		fromCity.click();
		fromTextField.clear();
		fromTextField.sendKeys(from);
		
		wait.until(ExpectedConditions.visibilityOfAllElements(fromCityList));
		
		for(WebElement fromCity : fromCityList) {
			if(fromCity.getText().contains(from)) {
				fromCity.click();
				break;
			}
		}
	}
	
	public DestinationPickerPage selectPlanningAnInternationalHoliday() {
		toCity.click();
		internationalHolidayLink.click();
		return new DestinationPickerPage(driver);
	}
}
