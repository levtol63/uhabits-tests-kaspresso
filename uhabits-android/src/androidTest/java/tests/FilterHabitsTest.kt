package tests

import android.content.ComponentName
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject2
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
@Feature("Habit Filters")
class FilterHabitsTest : TestCase(
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

    private fun clickTopmostByDesc(contentDesc: String) {
        val uiDevice = device.uiDevice
        val nodes: List<UiObject2> = uiDevice.findObjects(By.desc(contentDesc))
        if (nodes.isEmpty()) {
            throw RuntimeException("Не найдено ни одной вью с contentDescription='$contentDesc'")
        }
        val topmost = nodes.minByOrNull { it.visibleBounds.top } ?: nodes.first()
        val r = topmost.visibleBounds
        uiDevice.click(r.centerX(), r.centerY())
    }

    @Test
    @DisplayName("Проверка фильтрации — отображаются только активные привычки")
    @Description(
        "Проверяем, что фильтр 'Скрыть архивные' исключает заархивированную привычку из списка."
    )
    @Severity(SeverityLevel.NORMAL)
    fun verifyFilterArchive() = run {
        step("Предусловие: Создание привычки1") {
            openCreateHabitWithYesNoTemplate()
        }

        EditHabitScreen {
            nameInput.replaceText("Habit1")
            saveBtn.click()
        }

        step("Предусловие: Создание привычки2") {
            openCreateHabitWithYesNoTemplate()
        }

        EditHabitScreen {
            nameInput.replaceText("Change Habit 2")
            saveBtn.click()
        }

        step("Архивация привычки через меню overflow") {
            ListHabitsScreen {
                habitName1.longClick()
            }
            clickTopmostByDesc("More options")
            Espresso.onView(withText("Archive"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(ViewActions.click())
        }

        step("Проверка фильтрации: скрытие заархивированных привычек") {
            ListHabitsScreen {
                filterBtn.click()
                filterHideAchieved.isDisplayed()
                filterHideAchieved.click()

                val uiDevice = device.uiDevice
                val centerX = uiDevice.displayWidth / 2
                val centerY = uiDevice.displayHeight / 2
                uiDevice.click(centerX, centerY)

                habitName2.isDisplayed()
                habitName1.doesNotExist()
            }
        }

        step("Постусловие: Удаление привычки2") {
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
}
