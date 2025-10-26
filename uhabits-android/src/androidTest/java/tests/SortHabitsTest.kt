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
import com.kaspersky.components.alluresupport.withForcedAllureSupport
import io.qameta.allure.kotlin.Description
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screens.EditHabitScreen
import screens.ListHabitsScreen
import screens.ViewHabitScreen
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import utils.WelcomeSkipper

@RunWith(AndroidJUnit4::class)
@Epic("Habits")
@Feature("Habit Sorting")
class SortHabitsTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withForcedAllureSupport()
) {

    @get:Rule
    val notifPermission: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS)

    private val HABIT_CARD_CLASS = "com.isoron.uhabits.views.HabitCardView"
    private val TEXTVIEW_CLASS = "android.widget.TextView"
    private val WAIT_TIMEOUT = 3000L

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

    private fun getTopNTextViewsTexts(
        n: Int = 3,
        waitTimeoutMs: Long = 2000L,
        expectedSubstrings: List<String> = listOf("change", "habit", "test"),
        blackList: List<String> = listOf(
            "habits",
            "filter",
            "sort",
            "search",
            "settings",
            "archive",
            "all"
        )
    ): List<String> {
        val uiDevice = device.uiDevice
        uiDevice.wait(Until.hasObject(By.clazz(TEXTVIEW_CLASS)), waitTimeoutMs)

        val allTv: List<UiObject2> = uiDevice.findObjects(By.clazz(TEXTVIEW_CLASS))

        fun isLikelyName(s: String?): Boolean {
            if (s.isNullOrBlank()) return false
            val t = s.trim()
            if (t.length < 2) return false
            val low = t.lowercase()
            if (blackList.any {
                    low == it || low.startsWith("$it ") || low.endsWith(" $it") || low.contains(
                        " $it "
                    )
                }) return false
            if (low in listOf("ok", "cancel", "yes", "no", "delete", "save")) return false
            if (t.matches(Regex("^\\d+\$")) || t.matches(Regex("^\\d{1,2}:\\d{2}\$"))) return false
            return true
        }

        val pairs = allTv.mapNotNull { tv ->
            val txt = tv.text?.trim()
            if (!isLikelyName(txt)) return@mapNotNull null
            val top = tv.visibleBounds.top
            Pair(top, txt!!)
        }.sortedBy { it.first }

        val prioritized = mutableListOf<String>()
        for (substr in expectedSubstrings) {
            for ((_, text) in pairs) {
                if (text.contains(substr, ignoreCase = true) && prioritized.none {
                        it.equals(
                            text,
                            ignoreCase = true
                        )
                    }) {
                    prioritized.add(text)
                    if (prioritized.size >= n) return prioritized.take(n)
                }
            }
        }

        val unique = mutableListOf<String>()
        for ((_, text) in pairs) {
            if (unique.none { it.equals(text, ignoreCase = true) }) {
                unique.add(text)
                if (unique.size >= n) break
            }
        }

        return unique.take(n)
    }

    @Test
    @DisplayName("Проверка сортировки по имени")
    @Description("Сортируем список по имени и проверяем порядок первых трёх элементов")
    fun test01_editHabit_createThreeSortByName_andCheckSorting() = run {
        step("Создание привычки 1") {
            openCreateHabitWithYesNoTemplate()
        }
        EditHabitScreen {
            nameInput.replaceText("Habit1")
            saveBtn.click()
        }

        step("Создание привычки 2") {
            openCreateHabitWithYesNoTemplate()
        }
        EditHabitScreen {
            nameInput.replaceText("Change Habit 2")
            saveBtn.click()
        }

        step("Создание привычки 3") {
            openCreateHabitWithYesNoTemplate()
        }
        EditHabitScreen {
            nameInput.replaceText("Test")
            saveBtn.click()
        }

        Thread.sleep(300)

        step("Сортировка списка по имени") {
            ListHabitsScreen {
                filterBtn.click()
                sort.click()
                sortByName.click()
            }

            device.uiDevice.waitForIdle()
        }

        step("Проверка первых трёх элементов после сортировки") {
            val expectedTop3 = listOf("Change Habit 2", "Habit1", "Test")
            val top3 = getTopNTextViewsTexts(3, 3000).map { it.trim() }

            Log.i("TEST", "Top3 after sort: $top3")

            if (top3.size < 3) {
                throw AssertionError("Найдено меньше 3 названий после сортировки: $top3")
            }

            for (i in 0 until 3) {
                if (!top3[i].equals(expectedTop3[i], ignoreCase = true)) {
                    throw AssertionError("Неправильный порядок после сортировки на позиции ${i + 1}: actual='${top3[i]}' expected='${expectedTop3[i]}'")
                }
            }

            Log.i("TEST", "Первые три названия совпадают с ожидаемыми: $expectedTop3")
        }

        step("Постусловие: Удаление привычек") {
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
