package tests

import android.content.ComponentName
import android.content.Intent
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screens.EditHabitScreen
import screens.ListHabitsScreen

@RunWith(AndroidJUnit4::class)
@Epic("Habits")
@Feature("Edit Habit Screen")
class EditHabitScreenElementsTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withForcedAllureSupport()
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

    private fun openCreateHabitWithoutTarget() {
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
    @DisplayName("Проверка UI элементов при создании привычки без цели")
    @Description("Проверяем, что все элементы интерфейса отображаются корректно на экране создания привычки без цели")
    @Severity(SeverityLevel.NORMAL)
    fun verifyCreateHabitWithoutTargetElements() = run {
        step("Открыть экран создания привычки без цели") {
            openCreateHabitWithoutTarget()
        }

        step("Проверить отображение элементов на экране создания привычки без цели") {
            EditHabitScreen {
                nameInput.isDisplayed()
                questionInput.isDisplayed()
                frequencyPicker.isDisplayed()
                reminderTimePicker.isDisplayed()
                notesInput.isDisplayed()
                colotBtn.isDisplayed()
                CreateScreenTitle.isDisplayed()
                CreateScreenTitle.hasText("Create habit")
                saveBtn.isDisplayed()
                saveBtn.hasText("SAVE")
                device.uiDevice.pressBack()
            }
        }
    }

    @Test
    @DisplayName("Проверка UI элементов при создании привычки с целью")
    @Description("Проверяем, что все элементы интерфейса отображаются корректно на экране создания привычки с целью")
    @Severity(SeverityLevel.NORMAL)
    fun verifyCreateHabitWithTargetElements() = run {
        step("Открыть экран создания привычки с целью") {
            openCreateHabitWithTarget()
        }

        step("Проверить отображение элементов на экране создания привычки с целью") {
            EditHabitScreen {
                nameInput.isDisplayed()
                questionInput.isDisplayed()
                unitInput.isDisplayed()
                targetInput.isDisplayed()
                numFrequencyPicker.isDisplayed()
                targetType.isDisplayed()
                notesInput.isDisplayed()
                colotBtn.isDisplayed()
                CreateScreenTitle.isDisplayed()
                CreateScreenTitle.hasText("Create Habit")
                saveBtn.isDisplayed()
                saveBtn.hasText("SAVE")
                device.uiDevice.pressBack()
            }
        }
    }
}
