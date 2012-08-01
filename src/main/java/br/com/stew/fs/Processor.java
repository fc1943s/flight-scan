package br.com.stew.fs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.google.gson.Gson;

public abstract class Processor
{
	private WebDriver				browser;
	private  FirefoxProfile	profile;

	class CustomHtmlUnitDriver extends HtmlUnitDriver
	{
		public CustomHtmlUnitDriver()
		{
			super();
			getWebClient().getOptions().setThrowExceptionOnScriptError(false);
			getWebClient().getOptions().setCssEnabled(false);
			getWebClient().getOptions().setAppletEnabled(false);
			
		}
		
		 
	}
	
	public class EventHandler implements WebDriverEventListener{
		 
		@Override
		public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
			
	 
			System.out.println("inside method afterChangeValueOf on " + arg0.toString());
		}
		@Override
		public void afterClickOn(WebElement arg0, WebDriver arg1) {
			
			System.out.println("inside method afterClickOn on " + arg0.toString());
		}
		@Override
		public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
			
			System.out.println("Find happened on " + arg1.toString() 
					+ " Using method " + arg0.toString());
		}
		@Override
		public void afterNavigateBack(WebDriver arg0) {
			
	 
			System.out.println("Inside the after navigateback to " + arg0.getCurrentUrl());
		}
		@Override
		public void afterNavigateForward(WebDriver arg0) {
			
			System.out.println("Inside the afterNavigateForward to " + arg0.getCurrentUrl());
		}
		@Override
		public void afterNavigateTo(String arg0, WebDriver arg1) {
			
			System.out.println("Inside the afterNavigateTo to " + arg0);
		}
		@Override
		public void afterScript(String arg0, WebDriver arg1) {
			
			System.out.println("Inside the afterScript to, Script is " + arg0);
		}
		@Override
		public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
			
	 
			System.out.println("Inside the beforeChangeValueOf method");
		}
		@Override
		public void beforeClickOn(WebElement arg0, WebDriver arg1) {
			
			System.out.println("About to click on the " + arg0.toString());
	 
		}
		@Override
		public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
			
			System.out.println("Just before finding element " + arg1.toString());
	 
		}
		@Override
		public void beforeNavigateBack(WebDriver arg0) {
			
			System.out.println("Just before beforeNavigateBack " + arg0.getCurrentUrl());
	 
		}
		@Override
		public void beforeNavigateForward(WebDriver arg0) {
			
			System.out.println("Just before beforeNavigateForward " + arg0.getCurrentUrl());
	 
		}
		@Override
		public void beforeNavigateTo(String arg0, WebDriver arg1) {
			
			System.out.println("Just before beforeNavigateTo " + arg0);
		}
		@Override
		public void beforeScript(String arg0, WebDriver arg1) {
			
			System.out.println("Just before beforeScript " + arg0);
		}
		@Override
		public void onException(Throwable arg0, WebDriver arg1) {
			
			System.out.println("Exception occured at " + arg0.getMessage());
	 
		}
	 
	}

	EventFiringWebDriver eventDriver;
	
	public Processor(Schema schema)
	{
		Properties props = System.getProperties();
		props.setProperty("webdriver.chrome.driver", "F:\\Stew\\My Documents\\Downloads\\chromedriver_win32\\chromedriver.exe");
		
		//
		
		
		
		ChromeOptions options = new ChromeOptions();
		//options.addExtensions(new File("F:\\Stew\\My Documents\\Downloads\\Block-image_v1.1.crx"));
		options.addArguments("enable-low-res-tiling");
		
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.images", 2);
		options.setExperimentalOption("prefs", prefs);
		 
		
		
		
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		
		browser = new ChromeDriver(capabilities);
		
		browser.manage().timeouts().setScriptTimeout(50, TimeUnit.SECONDS);
		
	}

	public abstract String getUrl(Airport from, Airport to, DateTime fromDate, DateTime toDate);

	public abstract String getScript(String params);
	
	public class FlightInfo
	{
		public Long price;
		public String info;
	}
	
	public FlightInfo process(Airport from, Airport to, DateTime fromDate, DateTime toDate, String params)
	{
		
		String url = getUrl(from, to, fromDate, toDate);
		
		FlightInfo result = new FlightInfo();
		result.price = -1L;
		result.info = "";
				
		do
		{
			try
			{
				System.out.println("\n " + hashCode() + " - navigating...");
				
				getBrowser().navigate().to(url);
				
				result = new Gson().fromJson((String)((JavascriptExecutor)(getBrowser())).executeAsyncScript(getScript(params)), FlightInfo.class);
			}
			catch(TimeoutException e)
			{
				System.err.println(e);
				break;
			}
		}while(result.price == -20);
			
		System.out.print("\n " + hashCode() + " - " + new Gson().toJson(result) + "");
		return result;
	}

	public final WebDriver getBrowser()
	{
		return browser;
	}
}
