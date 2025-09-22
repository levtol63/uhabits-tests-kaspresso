package screens



import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.isoron.uhabits.R


object ListHabitScreen : KScreen<ListHabitScreen>() {
    override val viewClass: Class<*>? = null
    override val layoutId: Int? = null
    val addHabitBtn = KView { withId(R.id.actionCreateHabit) }
    val yesNoTemplate = KButton { withId(R.id.buttonYesNo) }
    val habitName1 = KTextView { withText("Habit1") }
    val habitName2 = KTextView { withText("Change Habit 2") }
}