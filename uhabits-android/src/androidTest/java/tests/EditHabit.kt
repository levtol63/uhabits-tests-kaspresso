package tests

import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screens.EditHabitScreen
import screens.ListHabitScreen
import screens.ViewHabitScreen
import utils.WelcomeSkipper
import java.io.File

@RunWith(AndroidJUnit4::class)
class EditHabit : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple()
) {
    @get:Rule
    val notifPermission: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS)

    @Before
    fun launchApp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val appId = "org.isoron.uhabits"

        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            component = ComponentName(appId, "$appId.MainActivity")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        instrumentation.startActivitySync(intent)
        instrumentation.waitForIdleSync()
    }

    @Before
    fun printAllureCandidates() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val cands = listOfNotNull(
            File(ctx.filesDir, "allure-results").absolutePath,
            ctx.getExternalFilesDir(null)?.let { File(it, "allure-results").absolutePath },
            "/sdcard/Android/data/${ctx.packageName}/files/allure-results",
            "/sdcard/Android/data/${ctx.packageName}.test/files/allure-results"
        )
        Log.i("ALLURE", "Candidates:\n" + cands.joinToString("\n"))
    }

    private fun openCreateHabitWithYesNoTemplate() {
        WelcomeSkipper.skipIfShown()
        ListHabitScreen {
            addHabitBtn.click()
            yesNoTemplate.click()
        }
    }

    @Test
    fun test01_editHabit_showsInList_andDetailsMatch() = run {
        step("Предусловие: Создание привычки") {
            openCreateHabitWithYesNoTemplate()
        }
        EditHabitScreen {
            nameInput.replaceText("Habit1")
            questionInput.replaceText("What?")
            frequencyPicker.click()
            threeTimePerWeek.click()
            buttonSaveFreqPicker.click()
            reminderTimePicker.click()
            reminderClearButton.click()
            notesInput.replaceText("Notes")
            saveBtn.click()
        }
        step("Открытие экрана редактирование и изменение привычки") {
            ListHabitScreen {
                habitName1.click()
            }
            ViewHabitScreen {
                editButton.click()
            }
            EditHabitScreen {
                nameInput.replaceText("Change Habit 2")
                questionInput.replaceText("How are you?")
                frequencyPicker.click()
                everyDayRadioButton.click()
                buttonSaveFreqPicker.click()
                reminderTimePicker.click()
                buttonDoneTimePicker.click()
                reminderDatePicker.isDisplayed()
                reminderDatePicker.click()
                reminderMonday.click()
                reminderSaturday.click()
                reminderThursday.click()
                reminderTuesday.click()
                reminderWednesday.click()
                reminderOkButton.click ()
                notesInput.replaceText("New notes update")
                saveBtn.click()
            }
        }

        step("Проверить новые данные привычки на экране просмотра") {
            ViewHabitScreen {
                screenTitle2.isDisplayed()
                screenTitle2.hasText("Change Habit 2")
                questionLabel.isDisplayed()
                questionLabel.hasText("How are you?")
                frequencyLabel.isDisplayed()
                frequencyLabel.hasText("Every day")
                reminderLabel.isDisplayed()
                reminderLabel.hasText("8:00 AM")
                habitNotes.isDisplayed()
                habitNotes.hasText("New notes update")
            }
        }
        step("Проверить новые данные привычки на экране изменения") {
            ViewHabitScreen {
                editButton.isDisplayed()
                editButton.click()
            }
            EditHabitScreen {
                nameInput.hasText("Change Habit 2")
                questionInput.hasText("How are you?")
                frequencyPicker.hasText("Every day")
                reminderTimePicker.hasText("8:00 AM")
                reminderDatePicker.hasText("Sun, Fri")
                notesInput.hasText("New notes update")
            }
        }
        step("Проверить название привычки на экране списке привычек") {
            EditHabitScreen {
                saveBtn.click()
            }
            device.uiDevice.pressBack()

            ListHabitScreen {
                habitName2.isDisplayed()
                habitName2.hasText("Change Habit 2")
                habitName2.click()
            }
        }
        step("Постусловие: Удаление привычки") {
            device.uiDevice.pressBack()
            ListHabitScreen {
                habitName2.click()
            }
            Espresso.onView(
                Matchers.allOf(
                    ViewMatchers.withContentDescription("More options")
                )
            ).perform(ViewActions.click())

            ViewHabitScreen {
                deleteButton.click()
                deleteDialogYes.click()
            }
        }
    }
}