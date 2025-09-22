package utils

import android.util.Log
import androidx.test.espresso.AmbiguousViewMatcherException
import androidx.test.espresso.NoMatchingViewException
import io.github.kakaocup.kakao.text.KButton
import org.isoron.uhabits.R


object WelcomeSkipper {
    private const val TAG = "WelcomeSkipper"

    fun skipIfShown() {
        val skip = KButton { withId(R.id.skip) }
        try {
            skip.isDisplayed()  // попробуем найти и показать
            skip.click()        // и кликнуть
            Log.i(TAG, "Экран приветствия показан, нажали Skip")
        } catch (e: NoMatchingViewException) {
            Log.i(TAG, "Кнопки Skip нет — продолжаем без неё")
        } catch (e: AmbiguousViewMatcherException) {
            Log.w(TAG, "Нашлось несколько 'skip' — пропускаем клик")
        } catch (e: AssertionError) {
            Log.i(TAG, "Skip не отображается — продолжаем")
        }
    }
}


