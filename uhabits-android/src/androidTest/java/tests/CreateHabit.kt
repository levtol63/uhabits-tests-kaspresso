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
import com.kaspersky.components.alluresupport.withForcedAllureSupport
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
class CreateHabit : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withForcedAllureSupport()
)
{
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
    fun test01_createHabit_showsInList_andDetailsMatch() = run {
        step("Открыть экран создания привычки") {
            openCreateHabitWithYesNoTemplate()
        }

        step("Заполнить поля и сохранить привычку1") {
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
        }

        step("Проверить, что новая привычка появилась в списке") {
            ListHabitScreen {
                habitName1.isDisplayed()
                habitName1.hasText("Habit1")
                habitName1.click()
            }
        }
        step("Проверить данные новой привычки на экране просмотра") {
            ViewHabitScreen {
                screenTitle1.isDisplayed()
                screenTitle1.hasText("Habit1")
                questionLabel.isDisplayed()
                questionLabel.hasText("What?")
                frequencyLabel.isDisplayed()
                frequencyLabel.hasText("3 times per week")
                reminderLabel.isDisplayed()
                reminderLabel.hasText("Off")
                habitNotes.hasText("Notes")
            }
        }
        step("Проверить данные новой привычки на экране изменения") {
            ViewHabitScreen {
                editButton.click()
            }
            EditHabitScreen {
                nameInput.hasText("Habit1")
                questionInput.hasText("What?")
                frequencyPicker.hasText("3 times per week")
                reminderTimePicker.hasText("Off")
                notesInput.hasText("Notes")
            }
        }
        step("Постусловие: Удаление привычки") {
            device.uiDevice.pressBack()
            ListHabitScreen {
                habitName1.click()
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