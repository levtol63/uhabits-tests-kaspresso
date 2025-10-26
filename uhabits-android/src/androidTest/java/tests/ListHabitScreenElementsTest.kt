package tests

import android.content.ComponentName
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
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
import io.qameta.allure.kotlin.junit4.DisplayName
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screens.ListHabitsScreen
import utils.WelcomeSkipper

@RunWith(AndroidJUnit4::class)
@Epic("Habits")
@Feature("List Habit Screen")
class ListHabitScreenElementsTest : TestCase(
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

    @Test
    @DisplayName("Проверка элементов экрана списка привычек")
    @Description(
        "Проверяем, что все элементы интерфейса отображаются корректно на экране списка привычек"
    )
    fun listHabitScreenElementsVisible() = run {
        step("Проверка элементов на экране списка привычек") {
            WelcomeSkipper.skipIfShown()
            ListHabitsScreen {
                listScreenTitle.hasText("Habits")
                addHabitBtn.isDisplayed()
                filterBtn.isDisplayed()
                Espresso.onView(
                    Matchers.allOf(
                        ViewMatchers.withContentDescription("More options")
                    )
                ).check(matches(ViewMatchers.isDisplayed()))
                noHabits.hasText("You have no active habits")
                addHabitBtn.click()
                yesNoTemplateBt.isDisplayed()
                MeasurableBt.isDisplayed()
                pressBack()
                filterBtn.click()
                filterHideAchieved.isDisplayed()
                filterHideCompleted.isDisplayed()
                sort.isDisplayed()
                sort.click()
                sortByName.isDisplayed()
                sortManually.isDisplayed()
                sortByColor.isDisplayed()
                sortByStatus.isDisplayed()
                sortByScore.isDisplayed()
                pressBack()
                Espresso.onView(
                    Matchers.allOf(
                        ViewMatchers.withContentDescription("More options")
                    )
                ).perform(ViewActions.click())
                darkTheme.isDisplayed()
                settings.isDisplayed()
                helpFaq.isDisplayed()
                about.isDisplayed()
            }
        }
    }
}
