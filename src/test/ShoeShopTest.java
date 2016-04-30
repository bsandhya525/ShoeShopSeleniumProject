package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ShoeShopTest {
	
	WebDriver driver= null;
	
	Properties OR = null;
	
	Properties CONFIG = null;
	
	
	@BeforeTest
	public void initialize() throws IOException
	{
		
		//Loading Properties Files
		
		OR = new Properties();
				
		FileInputStream fistr = new FileInputStream(System.getProperty("user.dir")+"/src/resources/OR.properties");
				
		OR.load(fistr);
				
				
		CONFIG = new Properties();
				
		fistr = new FileInputStream(System.getProperty("user.dir")+"/src/resources/config.properties");
				
		CONFIG.load(fistr);
				
		fistr.close();
		
		if(CONFIG.getProperty("browserName").equalsIgnoreCase("Mozilla"))
		{
			driver = new FirefoxDriver();
		}
		else if(CONFIG.getProperty("browserName").equalsIgnoreCase("Chrome"))
		{
			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/drivers/chromedriver.exe");
			
			driver = new ChromeDriver();
		}
		else if(CONFIG.getProperty("browserName").equalsIgnoreCase("IE"))
		{
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"/drivers/IEDriverServer.exe");
			
			driver = new InternetExplorerDriver();
		}
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		
		
	}
	
	@Test(dataProvider="getMonthsData")
	public void testMonthlyShoeDisplays(String monthName) throws InterruptedException
	{
	    
		
		driver.get(CONFIG.getProperty("testSiteURL"));
		
		Thread.sleep(1000);
		
		driver.findElement(By.linkText(monthName)).click();
		
		Thread.sleep(1000);
		
	    List<WebElement> listElms = driver.findElements(By.xpath(OR.getProperty("shoeList_xpath")));
	    
	    System.out.println("Number of shoes displayed:"+listElms.size());
	    
	    
	    for(int i=0;i<listElms.size();i++)
	    {
	    	
	    	String brand = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[1]/td[2]")).getText();
	    	String name = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[2]/td[2]")).getText();
	    	String price = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[3]/td[2]")).getText();
	    	String desc = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[4]/td[2]")).getText();
	    	String month = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[5]/td[2]")).getText();
	    	
	    	System.out.println("brand:"+brand);
	    	
	    	System.out.println("name:"+name);
	    	
	    	System.out.println("price:"+ price);
	    	
	    	System.out.println("desc:"+desc);
	    	
	    	System.out.println("month:"+month);
	    	
	    	Assert.assertNotNull(brand, "brand is null.No brand to display.");
	    	
	    	Assert.assertTrue(brand.length()>0, "There is no Brand of the shoe");
	    	
	    	Assert.assertNotNull(name, "name is null.No Name to display.");
	    	
	    	Assert.assertTrue(name.length()>0,"There is no name of the shoe");
	    	
	    	Assert.assertNotNull(price, "Price is null.No price to display.");
	    	
	    	Assert.assertTrue(price.length()>0,"There is no price of the shoe");
	    	
	    	Assert.assertNotNull(desc, "desc is null.No description to display.");
	    	
	    	Assert.assertTrue(desc.length()>0,"There is no description of the shoe");
	    	
	    	Assert.assertNotNull(month, "Release Month is null.No release month to display.");
	    	
	    	Assert.assertTrue(month.length()>0,"There is no release month of the shoe");
	    	
	    	WebElement imgTd = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[6]/td[1]"));
	    	
	    	List<WebElement> imgElms = imgTd.findElements(By.tagName("img"));
	    	
	    	System.out.println("Image Elements:"+imgElms.size());
	    	
	    	Assert.assertTrue(imgElms.size()>0,"There is no Shoe Image");
	    	
	    	String imgSrc = imgElms.get(0).getAttribute("src");
	    	
	    	System.out.println("image source:"+imgSrc);
	    	
	    	Assert.assertTrue(imgSrc.length()>0, "Image src is null.No Image to display.");
	    	
	    	
	    }
		
		
	}
	
	@Test(dataProvider="getEmailData")
	public void testUserEmailSubscription(String userEmail)
	{
		driver.get(CONFIG.getProperty("testSiteURL"));
		
		driver.findElement(By.xpath(OR.getProperty("remind_email_input_xpath"))).sendKeys(userEmail);
		
		driver.findElement(By.xpath(OR.getProperty("remind_email_submit_xpath"))).click();
		
		verifyEmailSubscription(userEmail);
	}
	
	
	private void verifyEmailSubscription(String email)
	{
		WebElement successDivElm = driver.findElement(By.xpath(OR.getProperty("subscription_success_div_xpath")));
		
		Assert.assertNotNull(successDivElm,"Subscription is not successful");
		
		Assert.assertEquals(successDivElm.getText(), OR.getProperty("subscription_successs_message")+" "+email);
	}
	
	
	@AfterTest
	public void closeBrowser()
	{
		if(driver != null)
			driver.close();
	}
	
	
	@DataProvider
	public Object[][] getMonthsData()
	{
		Object[][] data = new Object[13][1];
		

		
		data[0][0] ="January";
		data[1][0]="February";
		data[2][0]="March";
		data[3][0]="April";
		data[4][0]="May";
		data[5][0]="June";
		data[6][0]="July";
		data[7][0]="August";
		data[8][0]="September";
		data[9][0]="October";
		data[10][0]="November";
		data[11][0]="December";
		data[12][0]="All Shoes";
		
		return data;
	}

	@DataProvider 
	public Object[][] getEmailData()
	{
		Object[][] data = new Object[1][1];
		
		data[0][0] = "bsandhya525@rediffmail.com";
		
		return data;
	}

	
}
