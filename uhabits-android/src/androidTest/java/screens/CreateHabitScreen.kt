package screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.isoron.uhabits.R // сгенерированный R из вашего приложения


/** Экран создания привычки */
object CreateHabitScreen : KScreen<CreateHabitScreen>() {
    override val viewClass: Class<*>? = null
    override val layoutId: Int? = null  // можно просто не указывать

    val titleCreate = KTextView { withText("Create habit") }
    val navigateUp = KView { withContentDescription("Navigate up") }

    val nameInput = KEditText { withId(R.id.nameInput) }
    val questionInput = KEditText { withId(R.id.questionInput) }
    val notesInput = KEditText { withId(R.id.notesInput) }
    val reminderPicker = KTextView { withId(R.id.reminderTimePicker) }
    val frequencyPicker = KTextView { withId(R.id.boolean_frequency_picker) }
    val threeTimePerWeek = KButton {withId(R.id.xTimesPerWeekRadioButton)}
    val buttonSaveFreqPicker = KTextView { withText("SAVE")}
    val buttonDoneTimePicker = KButton { withId(R.id.done_button)}
    val reminderDatePicker = KButton { withId(R.id.reminderDatePicker)}
    val saveBtn = KButton { withId(R.id.buttonSave) }
    val colorButtonStub = KTextView { withText("Create habit") }
}
