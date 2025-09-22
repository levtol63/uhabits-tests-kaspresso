package tests

import base.BaseTest
import io.appium.java_client.AppiumBy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class ListPageTests : BaseTest() {
    @Test
    fun shouldDisplayElementsOnHabitList() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))
        val element1 = driver.findElement(
            AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"You have no active habits\")"
        ))
        Assertions.assertTrue(element1.isDisplayed)
        val element2 = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/actionCreateHabit"))
        Assertions.assertTrue(element2.isDisplayed)
        val element3 = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/action_filter"))
        Assertions.assertTrue(element3.isDisplayed)
        val element4 = driver.findElement(AppiumBy.className("android.widget.ImageView"))
        Assertions.assertTrue(element4.isDisplayed)
        val element5= driver.findElement(AppiumBy.xpath("//android.widget.RelativeLayout/android.view.View[2]"))
        Assertions.assertTrue(element5.isDisplayed)
        val element6 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Habits\"]"))
        Assertions.assertTrue(element6.isDisplayed)
        val element7 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"\uF5C0\"]"))
        Assertions.assertTrue(element7.isDisplayed)
    }
    @Test
    fun shouldOpenHabitTypePickerFromToolbarAdd() {
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val add = wait.until(
            ExpectedConditions.elementToBeClickable(
                AppiumBy.id("org.isoron.uhabits:id/actionCreateHabit")
            )
        )
        add.click()
        val ques1 = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.id("org.isoron.uhabits:id/buttonYesNo")
            )
        )
        Assertions.assertTrue(ques1.isDisplayed, "Кнопка Yes/No должна быть видна")
        val ques2 = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.id("org.isoron.uhabits:id/buttonMeasurable")
            )
        )
        Assertions.assertTrue(ques2.isDisplayed, "Кнопка Measurable должна быть видна")

        driver.navigate().back()
    }
    @Test
    fun shouldShowFilterMenuItems() {
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))

        wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("org.isoron.uhabits:id/action_filter")
        )).click()

        val hideArchived = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='org.isoron.uhabits:id/title' and @text='Hide archived']")
        ))
        Assertions.assertTrue(hideArchived.isDisplayed, "Hide archived должен быть видим")

        val hideCompleted = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='org.isoron.uhabits:id/title' and @text='Hide completed']")
        ))
        Assertions.assertTrue(hideCompleted.isDisplayed, "Hide completed должен быть видим")

        val sort = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='org.isoron.uhabits:id/title' and @text='Sort']")
        ))
        Assertions.assertTrue(sort.isDisplayed, "Sort должен быть видим")
        driver.navigate().back()
    }

    @Test
    fun shouldShowOverflowMenuItems() {
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))

        val overflow = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.className("android.widget.ImageView")
        ))
        overflow.click()

        val darkTheme = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='org.isoron.uhabits:id/title' and @text='Dark theme']")
        ))
        Assertions.assertTrue(darkTheme.isDisplayed)

        val settings = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='org.isoron.uhabits:id/title' and @text='Settings']")
        ))
        Assertions.assertTrue(settings.isDisplayed)

        val faq = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='org.isoron.uhabits:id/title' and @text='Help & FAQ']")
        ))
        Assertions.assertTrue(faq.isDisplayed)

        val about = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='org.isoron.uhabits:id/title' and @text='About']")
        ))
        Assertions.assertTrue(about.isDisplayed)
        driver.navigate().back()
    }
    @Test
    fun shouldNavigateToAddHabitScreenFromListPage() {
        val add = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/actionCreateHabit"))
        add.click()
        val ques1 = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/buttonYesNo"))
        ques1.click()
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val habbitScreen = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.widget.TextView[@text=\"Frequency\"]")
            )
        )
        Assertions.assertTrue(habbitScreen.isDisplayed)
        driver.navigate().back()
    }


}