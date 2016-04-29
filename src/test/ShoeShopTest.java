package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
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
		driver = new FirefoxDriver();
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		
		//Loading Properties Files
		
		OR = new Properties();
		
		FileInputStream fistr = new FileInputStream(System.getProperty("user.dir")+"/src/resources/OR.properties");
		
		OR.load(fistr);
		
		
		CONFIG = new Properties();
		
		fistr = new FileInputStream(System.getProperty("user.dir")+"/src/resources/config.properties");
		
		CONFIG.load(fistr);
		
		fistr.close();
	}
	
	@Test(dataProvider="getMonthsData")
	public void testMonthlyShoeDisplays(String monthName) throws InterruptedException
	{
	    
		
		driver.get(CONFIG.getProperty("testSiteURL"));
		
		Thread.sleep(1000);
		
		driver.findElement(By.linkText(monthName)).click();
		
		Thread.sleep(1000);
		
	    WebElement listElm = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")));
	    
	    List<WebElement> subList = listElm.findElements(By.tagName(OR.getProperty("list_tagName")));
	    
	    System.out.println("Number of shoes displayed:"+subList.size());
	    
	    
	    for(int i=0;i<subList.size();i++)
	    {
	    	WebElement elm = (WebElement)subList.get(i);
	    	
	    	WebElement divElm = elm.findElement(By.className(OR.getProperty("shoeResult_div_class")));
	    	
	    	WebElement tableElm = divElm.findElement(By.xpath(OR.getProperty("tableBody_xpath")));
	    	
	    	List<WebElement> tableRows = tableElm.findElements(By.tagName(OR.getProperty("tableRow_tagName")));
	    	
	    	System.out.println("table rows:"+tableRows.size());
	    	
	    	for(int j=0;j<tableRows.size();j++)
	    	{
	    		if(j<5)
	    		{
	    			String name = tableElm.findElement(By.xpath("//tr["+(j+1)+"]/td[1]")).getText();
	    			String value = tableElm.findElement(By.xpath("//tr["+(j+1)+"]/td[2]")).getText();
	    			System.out.println(tableElm.findElement(By.xpath("//tr["+(j+1)+"]/td[2]")).getText());
	    			
	    			Assert.assertNotNull(value, name +" is null");
	    		}
	    		else if(j==5)
	    		{
	    			 WebElement imgElm = tableElm.findElement(By.xpath("//tr["+(j+1)+"]/td[1]")).findElement(By.tagName("img"));
	    					
	    			 Assert.assertNotNull(imgElm, "There is no Shoe image");
	    			 
	    			 String  imgSrc =	imgElm.getAttribute("src");
	    			
	    			System.out.println("Image source:"+imgSrc);
	    			
	    			Assert.assertNotNull(imgSrc, "Image source is null");
	    		}
	    		
	    	}
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
	
	
	public void verifyEmailSubscription(String email)
	{
		WebElement successDivElm = driver.findElement(By.xpath(OR.getProperty("subscription_success_div_xpath")));
		
		Assert.assertNotNull(successDivElm,"Subscription is not successful");
		
		Assert.assertEquals(successDivElm.getText(), OR.getProperty("subscription_successs_message")+" "+email);
	}
	
	
	@AfterTest
	public void closeBrowser()
	{
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
