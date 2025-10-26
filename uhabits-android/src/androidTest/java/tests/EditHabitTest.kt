package tests

import android.content.ComponentName
import android.content.Intent
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
import io.qameta.allure.kotlin.Story
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

@RunWith(AndroidJUnit4::class)
@Epic("Habits")
@Feature("Edit Habit")
class EditHabitTest :TestCase(
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

    private fun openCreateHabitWithYesNoTemplate() {
        ListHabitsScreen {
            addHabitBtn.click()
            yesNoTemplateBt.click()
        }
    }
    private fun openCreateHabitWithTarget() {
        ListHabitsScreen {
            addHabitBtn.click()
            MeasurableBt.click()
        }
    }

    @Test
    @DisplayName("Изменение привычки без цели — проверка отображения данных")
    @Story("Edit habit (yes/no)")
    @Description("Редактируем привычку без цели, проверяем данные на экранах просмотра, редактирования и в списке")
    @Severity(SeverityLevel.CRITICAL)
    fun editHabitWithoutTarget_updateAndVerify() = run {
        step("Предусловие: Создание привычки без цели") {
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
        step("Открытие экрана редактирование и изменение привычки без цели") {
            ListHabitsScreen {
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

        step("Проверить новые данные привычки без цели на экране просмотра") {
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
        step("Проверить новые данные привычки без цели на экране изменения") {
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
        step("Проверить название привычки без цели на экране списке привычек") {
            EditHabitScreen {
                saveBtn.click()
            }
            device.uiDevice.pressBack()

            ListHabitsScreen {
                habitName2.isDisplayed()
                habitName2.hasText("Change Habit 2")
                habitName2.click()
            }
        }
        step("Постусловие: Удаление привычки без цели") {
            device.uiDevice.pressBack()
            ListHabitsScreen {
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

    @Test
    @DisplayName("Изменение привычки c целью — проверка отображения данных")
    @Story("Edit habit (yes/no)")
    @Description("Редактируем привычку c целью, проверяем данные на экранах просмотра, редактирования и в списке")
    @Severity(SeverityLevel.CRITICAL)
    fun editHabitWithTarget_updateAndVerify()= run {
        step("Открыть экран создания привычки с целью") {
            openCreateHabitWithTarget()
        }
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
        step("Открытие экрана редактирование и изменение привычки с целью") {
            ListHabitsScreen {
                habitName3.click()
            }
            ViewHabitScreen {
                editButton.click()
            }
            EditHabitScreen {
                nameInput.replaceText("Name Habit Test")
                questionInput.replaceText("Ok?")
                targetInput.replaceText("100")
                unitInput.replaceText("H")
                numFrequencyPicker.click()
                everyDay.click()
                targetType.click()
                atLeast.click()
                reminderTimePicker.click()
                buttonDoneTimePicker.click()
                reminderDatePicker.isDisplayed()
                reminderDatePicker.click()
                reminderMonday.click()
                reminderSaturday.click()
                reminderThursday.click()
                reminderOkButton.click()
                notesInput.replaceText("Long notes")
                saveBtn.click()
            }
        }

        step("Проверить новые данные привычки c целью на экране просмотра") {
            ViewHabitScreen {
                screenTitle4.isDisplayed()
                screenTitle4.hasText("Name Habit Test")
                questionLabel.isDisplayed()
                questionLabel.hasText("Ok?")
                target.isDisplayed()
                target.hasText("100 H")
                reminderLabel.isDisplayed()
                reminderLabel.hasText("8:00 AM")
                frequencyLabel.isDisplayed()
                frequencyLabel.hasText("Every day")
                habitNotes.isDisplayed()
                habitNotes.hasText("Long notes")
            }
        }
        step("Проверить новые данные привычки c целью на экране изменения") {
            ViewHabitScreen {
                editButton.isDisplayed()
                editButton.click()
            }
            EditHabitScreen {
                nameInput.hasText("Name Habit Test")
                questionInput.hasText("Ok?")
                numFrequencyPicker.hasText("Every day")
                reminderTimePicker.hasText("8:00 AM")
                reminderDatePicker.hasText("Sun, Tue, Wed, Fri")
                notesInput.hasText("Long notes")
            }
        }
        step("Проверить название привычки c целью на экране списке привычек") {
            EditHabitScreen {
                saveBtn.click()
            }
            device.uiDevice.pressBack()

            ListHabitsScreen {
                habitName4.isDisplayed()
                habitName4.hasText("Name Habit Test")
                habitName4.click()
            }
        }
        step("Постусловие: Удаление привычки c целью") {
            device.uiDevice.pressBack()
            ListHabitsScreen {
                habitName4.click()
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
