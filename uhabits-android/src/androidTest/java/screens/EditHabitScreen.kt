package screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.isoron.uhabits.R


object EditHabitScreen: KScreen<EditHabitScreen>() {
    override val viewClass: Class<*>? = null
    override val layoutId: Int? = null
    val nameInput = KEditText { withId(R.id.nameInput) }
    val questionInput = KEditText { withId(R.id.questionInput) }
    val notesInput = KEditText { withId(R.id.notesInput) }
    val unitInput = KEditText { withId(R.id.unitInput) }
    val targetInput = KEditText { withId(R.id.targetInput) }
    val reminderTimePicker = KTextView { withId(R.id.reminderTimePicker) }
    val reminderHours = KEditText { withId(R.id.hours) }
    val reminderMinutes = KEditText { withId(R.id.minutes) }
    val reminderClearButton = KButton { withId(R.id.clear_button) }
    val frequencyPicker = KTextView { withId(R.id.boolean_frequency_picker) }
    val numFrequencyPicker = KTextView { withId(R.id.numericalFrequencyPicker) }
    val everyDayRadioButton = KButton {withId(R.id.everyDayRadioButton)}
    val everyDay = KTextView { withText("Every day")}
    val everyWeek = KTextView { withText("Every week")}
    val threeTimePerWeek = KButton {withId(R.id.xTimesPerWeekRadioButton)}
    val buttonSaveFreqPicker = KTextView { withText("SAVE")}

    val targetType = KButton {withId(R.id.targetTypePicker)}

    val atMost = KTextView { withText("At most")}
    val atLeast = KTextView { withText("At least")}

    val buttonDoneTimePicker = KButton { withId(R.id.done_button)}
    val reminderDatePicker = KTextView { withId(R.id.reminderDatePicker) }
    val reminderSaturday = KTextView { withText("Saturday")}
    val reminderMonday = KTextView { withText("Monday")}
    val reminderTuesday = KTextView { withText("Tuesday")}
    val reminderWednesday= KTextView { withText("Wednesday")}
    val reminderThursday= KTextView { withText("Thursday")}
    val reminderOkButton = KTextView { withText("OK")}
    val saveBtn = KButton { withId(R.id.buttonSave) }
    val colotBtn = KButton { withId(R.id.colorButton) }
    val CreateScreenTitle= KTextView { withText("Create habit")}

}