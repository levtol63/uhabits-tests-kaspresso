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
import io.qameta.allure.kotlin.junit4.DisplayName
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screens.EditHabitScreen
import screens.ListHabitsScreen
import screens.ViewHabitScreen

@RunWith(AndroidJUnit4::class)
@Epic("Habits")
@Feature("Delete Habit")
class DeleteHabitTest : TestCase(
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
    @DisplayName("Удаление привычки без цели")
    @Description("Удаляем привычку без цели и проверяем, что она отсутствует в списке")
    @Severity(SeverityLevel.CRITICAL)
    fun deleteHabitWithoutTarget_verifyRemoved() = run {
        step("Постусловие: Удаление привычки") {
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
        step("Предусловие: Создание привычки без цели") {
            openCreateHabitWithYesNoTemplate()
        }
        EditHabitScreen {
            nameInput.replaceText("Habit1")
            saveBtn.click()
        }
        step("Удаление привычки без цели") {
            ListHabitsScreen {
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

        step("Проверить, что привычка без цели удалена из списка привычек") {
            ListHabitsScreen {
                habitName1.doesNotExist()
            }
        }
    }

    @Test
    @DisplayName("Удаление привычки с целью")
    @Description("Удаляем привычку с целью и проверяем, что она отсутствует в списке")
    @Severity(SeverityLevel.CRITICAL)
    fun deleteHabitWithTarget_verifyRemoved() = run {
        step("Предусловие: Создание привычки с целью") {
            openCreateHabitWithTarget()
        }
        EditHabitScreen {
            nameInput.replaceText("Test")
            targetInput.replaceText("55")
            unitInput.replaceText("M")
            saveBtn.click()
        }
        step("Удаление привычки с целью") {
            ListHabitsScreen {
                habitName3.click()
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

        step("Проверить, что привычка с целью удалена из списка привычек") {
            ListHabitsScreen {
                habitName3.doesNotExist()
            }
        }
    }
}
