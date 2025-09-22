package screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView



object CreateHabitScreen : KScreen<CreateHabitScreen>() {
    override val viewClass: Class<*>? = null
    override val layoutId: Int? = null

    val titleCreate = KTextView { withText("Create habit") }
}
