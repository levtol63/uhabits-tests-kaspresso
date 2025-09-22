package tests

import base.BaseTest
import io.appium.java_client.AppiumBy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import kotlin.test.assertEquals

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CreateHabitPageScreenTheFirst : BaseTest() {


    @Test
    @Order(1)
    fun shouldOpenCreateHabitScreenAndShowMainFields() {
        val add = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/actionCreateHabit"))
        add.click()
        val ques1 = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/buttonYesNo"))
        ques1.click()
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val pickerdFreq = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.widget.TextView[@text=\"Frequency\"]")
            )
        )
        Assertions.assertTrue(pickerdFreq.isDisplayed)
        val title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Create habit\"]"))
        title.click()
        val buttonSave = driver.findElement(AppiumBy.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]"))
        Assertions.assertTrue(buttonSave.isDisplayed)
        val fieldName = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id=\"org.isoron.uhabits:id/nameInput\"]"))
        Assertions.assertTrue(fieldName.isDisplayed)
        val fieldQuestion = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id=\"org.isoron.uhabits:id/questionInput\"]"))
        Assertions.assertTrue(fieldQuestion.isDisplayed)
        val pickerRemember = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"org.isoron.uhabits:id/reminderTimePicker\"]"))
        Assertions.assertTrue(pickerRemember.isDisplayed)
        val fieldNotes = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id=\"org.isoron.uhabits:id/notesInput\"]"))
        Assertions.assertTrue(fieldNotes.isDisplayed)
        val buttonColor = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Create habit\"]"))
        Assertions.assertTrue(buttonColor.isDisplayed)
        val buttonBack = driver.findElement(AppiumBy.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]"))
        Assertions.assertTrue(buttonBack.isDisplayed)

    }

    @Test
    @Order(2)
    fun shouldShowValidationWhenSavingEmptyForm() {
        val save = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/buttonSave"))
        save.click()
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val errorIcon = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.widget.TextView[@text=\"Frequency\"]")
            )
        )
        Assertions.assertTrue(errorIcon.isDisplayed)
        println(driver.currentActivity())
    }

    @Test
    @Order(3)
    fun shouldEnterNameAndSeeItInField() {
        val inputName = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/nameInput"))
        inputName.sendKeys("Habit1")
        assertEquals("Habit1", inputName.text)
    }

    @Test
    @Order(4)
    fun shouldEnterQuestionAndSeeItInField() {
        val inputQues = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/questionInput"))
        inputQues.sendKeys("What?")
        assertEquals("What?", inputQues.text)
    }

    @Test
    @Order(5)
    fun shouldSetFrequencyAndSeeItInField() {
        val pickerFreq =
            driver.findElement(AppiumBy.id("org.isoron.uhabits:id/boolean_frequency_picker"))
        pickerFreq.click()
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val radioButFreq = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.id("org.isoron.uhabits:id/everyXDaysRadioButton")
            )
        )
        radioButFreq.click()
        val saveFreq = driver.findElement(AppiumBy.id("android:id/button1"))
        saveFreq.click()
        val testAfter = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath(" //android.widget.TextView[@resource-id=\"org.isoron.uhabits:id/boolean_frequency_picker\"]")
            )
        )
        assertEquals("Every 3 days", testAfter.text)
    }

    @Test
    @Order(6)
    fun shouldSetReminderTimeAndSeeItInField() {
        val pickerTime = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/reminderTimePicker"))
        pickerTime.click()
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val done = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.id("org.isoron.uhabits:id/done_button")
            )
        )
        done.click()
        val textAfter = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath(" //android.widget.TextView[@resource-id=\"org.isoron.uhabits:id/reminderTimePicker\"]")
            )
        )
        assertEquals("8:00 AM", textAfter.text)
    }

    @Test
    @Order(7)
    fun shouldEnterNotesAndSeeThemInField() {
        val inputNotes = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/notesInput"))
        inputNotes.sendKeys("Hello123")
        assertEquals("Hello123", inputNotes.text)


    }

    @Test
    @Order(8)
    fun shouldChangeColorViaColorPicker() {
        val buttonColor =
            driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id=\"org.isoron.uhabits:id/colorButton\"]"))
        buttonColor.click()
        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val done = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("(//android.widget.ImageView[@resource-id=\"org.isoron.uhabits:id/color_picker_swatch\"])[3]")
            )
        )
        done.click()
        Assertions.assertTrue(buttonColor.isDisplayed)

    }

    @Test
    @Order(9)
    fun shouldSaveHabitAndSeeItInList() {
        val save = driver.findElement(AppiumBy.id("org.isoron.uhabits:id/buttonSave"))
        save.click()

        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val newHabit = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath(" //android.widget.TextView[@text=\"Habit1\"]")
            )
        )
        assertEquals("Habit1", newHabit.text)


    }
}
