package com.sprinto.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LandingPage {

	WebDriver driver;
	
	public LandingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//span[@class='commonModal__close']")
	private WebElement closeButton;
	
	@FindBy(xpath="(//span[text()='Flights'])[1]")
	private WebElement flights;
	
	public void clickOnCloseButton() {
		closeButton.click();
	}
	
	public FlightsPage navigateToFlightsPage() {
		flights.click();
		return new FlightsPage(driver);
	}
}
