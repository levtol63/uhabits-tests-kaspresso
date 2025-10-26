package screens



import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.isoron.uhabits.R


object ListHabitsScreen : KScreen<ListHabitsScreen>() {
    override val viewClass: Class<*>? = null
    override val layoutId: Int? = null
    val addHabitBtn = KView { withId(R.id.actionCreateHabit) }
    val yesNoTemplateBt = KButton { withId(R.id.buttonYesNo) }
    val MeasurableBt = KButton { withId(R.id.buttonMeasurable) }
    val habitName1 = KTextView { withText("Habit1") }
    val habitName2 = KTextView { withText("Change Habit 2") }
    val habitName3 = KTextView { withText("Test") }
    val habitName4 = KTextView { withText("Name Habit Test") }
    val filterBtn = KView { withId(R.id.action_filter) }
    val filterHideCompleted = KView { withText("Hide completed")}
    val filterHideAchieved = KView{ withText("Hide archived")}
    val sortByName = KView { withText("By name")}
    val archButton = KButton { withText("Archive")}
    val sort = KButton { withText("Sort")}
    val noHabits = KTextView { withText("You have no active habits")}
    val listScreenTitle = KTextView { withText("Habits")}
    val sortManually = KView { withText("Manually")}
    val sortByColor = KView { withText("By color")}
    val sortByScore = KView { withText("By score")}
    val sortByStatus = KView { withText("By status")}
    val darkTheme = KView { withText("Dark theme")}
    val settings= KView { withText("Settings")}
    val helpFaq = KView { withText("Help & FAQ")}
    val about = KView { withText("About")}

}