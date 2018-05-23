package edu.uclm.esi.tysweb.laoca.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

public class PruebaPartida {
	
  private WebDriver driver;
  private WebDriver driver2;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	  System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
    driver = new ChromeDriver();
    driver2 = new ChromeDriver();
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
   driver2.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testPruebaPartida() throws Exception {
	  long pos;
     
      
	  //Primer jugador
    driver.get("http://localhost:8080/LaOca2017/");
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).clear();
    driver.findElement(By.id("email")).sendKeys("@A");
    driver.findElement(By.cssSelector("button.login100-form-btn")).click();
    driver.findElement(By.id("njugadores")).clear();
    driver.findElement(By.id("njugadores")).sendKeys("2");
    driver.findElement(By.id("njugadores")).click();
    driver.findElement(By.id("btnCrear")).click();
    Thread.sleep(3000);
    //Segundo jugador
    driver2.get("http://localhost:8080/LaOca2017/");
    driver2.findElement(By.id("email")).click();
    driver2.findElement(By.id("email")).clear();
    driver2.findElement(By.id("email")).sendKeys("@B");
    driver2.findElement(By.cssSelector("button.login100-form-btn")).click();
    driver2.findElement(By.id("njugadores")).clear();
    driver2.findElement(By.id("btnUnirse")).click();
    Thread.sleep(3000);
    //JavaScript a ejecutar
    JavascriptExecutor jsA=(JavascriptExecutor)driver;
    JavascriptExecutor jsB=(JavascriptExecutor)driver2;
    
    //Empiezan a lanzar el dado
    jsA.executeScript("lanzarDado2(4);");
    Thread.sleep(3000);
    jsA.executeScript("lanzarDado2(3);");
    Thread.sleep(3000);
    jsA.executeScript("lanzarDado2(3);");
    Thread.sleep(3000);
    jsA.executeScript("lanzarDado2(1);");
    Thread.sleep(3000);
    //Turno de B
   jsB.executeScript("lanzarDado2(1);");
    Thread.sleep(3000);
    //Turno de A
    jsA.executeScript("lanzarDado2(4);");
    Thread.sleep(3000);
    pos=(long)jsA.executeScript("return getPosFinal();");
    assertTrue(pos==18);
    //Turno de B
    jsB.executeScript("lanzarDado2(4);");
    Thread.sleep(3000);
    jsB.executeScript("lanzarDado2(1);");
    Thread.sleep(3000);
    
    jsB.executeScript("lanzarDado2(5);");
    Thread.sleep(3000);
    jsB.executeScript("lanzarDado2(3);");
    Thread.sleep(3000);
    jsB.executeScript("lanzarDado2(2);");
    Thread.sleep(3000);
    jsB.executeScript("lanzarDado2(1);");
    Thread.sleep(3000);
   //Turno de A
    jsA.executeScript("lanzarDado2(4);");
    Thread.sleep(3000);
    jsA.executeScript("lanzarDado2(5);");
    Thread.sleep(3000);
    jsA.executeScript("lanzarDado2(5);");
    Thread.sleep(3000);
    jsA.executeScript("lanzarDado2(5);");
    Thread.sleep(3000);
    jsA.executeScript("lanzarDado2(1);");
    Thread.sleep(3000);
    //Turno de B
    jsB.executeScript("lanzarDado2(6);");
    Thread.sleep(3000);
    //Turno de A
    jsA.executeScript("lanzarDado2(1);");
    Thread.sleep(3000);
    //Turno de B
    jsB.executeScript("lanzarDado2(2);");
    Thread.sleep(3000);
    pos=(long)jsB.executeScript("return getPosFinal();");
    assertTrue(pos==61);
    //Turno de A
    jsA.executeScript("lanzarDado2(4);");
    Thread.sleep(3000);
    //Turno de B
    jsB.executeScript("lanzarDado2(1);");
    Thread.sleep(3000);
    pos=(long)jsB.executeScript("return getPosFinal();");
    assertTrue(pos==62);
    
  }

  @After
  public void tearDown() throws Exception {
    //driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
