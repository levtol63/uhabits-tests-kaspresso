package screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.isoron.uhabits.R

object ViewHabitScreen : KScreen<ViewHabitScreen>() {
    override val viewClass: Class<*>? = null
    override val layoutId: Int? = null
    val screenTitle1 = KTextView { withText("Habit1") }
    val screenTitle2 = KTextView { withText("Change Habit 2") }
    val screenTitle3 = KTextView { withText("Test") }
    val screenTitle4 = KTextView { withText("Name Habit Test") }

    val target = KTextView { withId(R.id.targetText) }

    val questionLabel = KTextView { withId(R.id.questionLabel) }
    val frequencyLabel = KTextView { withId(R.id.frequencyLabel) }
    val reminderLabel = KTextView { withId(R.id.reminderLabel) }
    val habitNotes = KTextView { withId(R.id.habitNotes) }

    val editButton = KButton {withId(R.id.action_edit_habit)}
    val randomizeButton = KButton {withId(R.id.action_randomize)}
    val deleteButton = KButton { withText("Delete")}
    val deleteDialogYes = KButton { withText("YES")}

}