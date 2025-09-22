package screens

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import org.isoron.uhabits.R // сгенерированный R из вашего приложения

/** Главный экран со списком привычек */
object ListHabitScreen : KScreen<ListHabitScreen>() {
    override val viewClass: Class<*>? = null
    override val layoutId: Int? = null// можно просто не указывать
    val addHabitBtn = KView { withId(R.id.actionCreateHabit) }
    val yesNoTemplate = KButton { withId(R.id.buttonYesNo) }
    val habitName1 = KTextView { withText("Habit1") }
    val habitName2 = KTextView { withText("Change Habit 2") }
}