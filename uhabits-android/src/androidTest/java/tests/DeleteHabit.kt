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
import io.qameta.allure.kotlin.Allure
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screens.EditHabitScreen
import screens.ListHabitScreen
import screens.ViewHabitScreen
import utils.WelcomeSkipper
import java.io.File

@RunWith(AndroidJUnit4::class)
class DeleteHabit :TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple()
)  { companion object {
    @JvmStatic
    @BeforeClass
    fun initAllure() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext

        val resultsDir = File(ctx.getExternalFilesDir(null), "allure-results").apply { mkdirs() }

        File(ctx.filesDir, "original_screenshots").mkdirs()

        Log.i("ALLURE", "📂 Allure results dir = ${resultsDir.absolutePath}")

        Allure.lifecycle = io.qameta.allure.android.AllureAndroidLifecycle(
            io.qameta.allure.kotlin.FileSystemResultsWriter { resultsDir }
        )
    }
}

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
        step("Удаление привычки") {
            ListHabitScreen {
                habitName1.click()
            }
            Espresso.onView(
                Matchers.allOf(
                    ViewMatchers.withContentDescription("More options")
                )
            ).perform(ViewActions.click())

            ViewHabitScreen {
                deleteButton.isDisplayed()
                deleteButton.click()
                deleteDialogYes.isDisplayed()
                deleteDialogYes.click()
            }
        }

        step("Проверить, что привычка удалена из списка привычек") {
            ListHabitScreen {
                habitName1.doesNotExist()
            }
        }


    }
}