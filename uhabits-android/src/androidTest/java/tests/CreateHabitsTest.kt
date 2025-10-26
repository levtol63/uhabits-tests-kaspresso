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
import io.qameta.allure.kotlin.Description
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.Severity
import io.qameta.allure.kotlin.SeverityLevel
import io.qameta.allure.kotlin.junit4.DisplayName
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screens.EditHabitScreen
import screens.ListHabitsScreen
import screens.ViewHabitScreen
import utils.WelcomeSkipper
import java.io.File

@RunWith(AndroidJUnit4::class)
@Epic("Habits")
@Feature("Create Habit")
class CreateHabitsTest : TestCase(
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

    private fun openCreateHabitWithoutTarget() {
        ListHabitsScreen {
            addHabitBtn.click()
            yesNoTemplateBt.click()
        }
    }

    private fun openCreateHabitWithTarget() {
        WelcomeSkipper.skipIfShown()
        ListHabitsScreen {
            addHabitBtn.click()
            MeasurableBt.click()
        }
    }

    @Test
    @DisplayName("Создание привычки без цели — проверка отображения данных")
    @Description("Создаём привычку без цели, проверяем её в списке, на экране просмотра и редактирования")
    @Severity(SeverityLevel.CRITICAL)
    fun createHabitWithoutTarget_verifyListAndDetails() = run {
        step("Открыть экран создания привычки без цели") {
            openCreateHabitWithoutTarget()
        }

        step("Заполнить поля и сохранить привычку") {
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
            ListHabitsScreen {
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
                questionLabel.hasText("What")
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
            ListHabitsScreen {
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

    @Test
    @DisplayName("Создание привычки с целью — проверка отображения данных")
    @Description("Создаём привычку с целью, проверяем наличие в списке, корректность данных на экране просмотра и редактирования")
    @Severity(SeverityLevel.CRITICAL)
    fun createHabitWithTarget_verifyListAndDetails() = run {
        step("Открыть экран создания привычки с целью") {
            openCreateHabitWithTarget()
        }

        step("Заполнить поля и сохранить привычку") {
            EditHabitScreen {
                nameInput.replaceText("Test")
                questionInput.replaceText("How?")
                targetInput.replaceText("55")
                unitInput.replaceText("M")
                numFrequencyPicker.click()
                everyWeek.click()
                targetType.click()
                atMost.click()
                reminderTimePicker.click()
                reminderClearButton.click()
                notesInput.replaceText("New test")
                saveBtn.click()
            }
        }

        step("Проверить, что новая привычка появилась в списке") {
            ListHabitsScreen {
                habitName3.isDisplayed()
                habitName3.hasText("Test")
                habitName3.click()
            }
        }

        step("Проверить данные новой привычки на экране просмотра") {
            ViewHabitScreen {
                screenTitle3.isDisplayed()
                screenTitle3.hasText("Test")
                questionLabel.isDisplayed()
                questionLabel.hasText("How?")
                target.isDisplayed()
                target.hasText("55 M")
                reminderLabel.isDisplayed()
                reminderLabel.hasText("Off")
                habitNotes.hasText("New test")
                frequencyLabel.isDisplayed()
                frequencyLabel.hasText("Every week")
            }
        }

        step("Проверить данные новой привычки на экране изменения") {
            ViewHabitScreen {
                editButton.click()
            }
            EditHabitScreen {
                nameInput.hasText("Test")
                questionInput.hasText("How?")
                reminderTimePicker.hasText("Off")
                notesInput.hasText("New test")
                targetInput.hasText("55.0")
                unitInput.hasText("M")
                numFrequencyPicker.hasText("Every week")
                targetType.hasText("At most")
            }
        }

        step("Постусловие: Удаление привычки") {
            device.uiDevice.pressBack()
            ListHabitsScreen {
                habitName3.click()
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
