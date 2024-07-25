package com.sprinto.baseClass;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.sprinto.utils.UtilityClass;

public class BaseClass {

	WebDriver driver;
	public Properties prop;
	
	public BaseClass() {
		prop = new Properties();
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\java\\com\\sprinto\\configs\\config.properties");
		try {
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public WebDriver initilizeBrowser(String browserName) {
		if(browserName.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		}else if(browserName.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		}else if(browserName.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(UtilityClass.implicitWaitTime));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(UtilityClass.pageLoadTime));
		driver.manage().deleteAllCookies();
		driver.get(prop.getProperty("URL"));
		
		return driver;
	}
}
