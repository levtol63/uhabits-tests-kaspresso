package org.isoron.uhabits.acceptance

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.UiSelector
import org.isoron.uhabits.BaseUserInterfaceTest
import org.isoron.uhabits.acceptance.steps.CommonSteps.clickText
import org.isoron.uhabits.acceptance.steps.CommonSteps.launchApp
import org.isoron.uhabits.acceptance.steps.CommonSteps.pressBack
import org.isoron.uhabits.acceptance.steps.CommonSteps.verifyDisplaysCheckmarks
import org.isoron.uhabits.acceptance.steps.ListHabitsSteps.MenuItem.SETTINGS
import org.isoron.uhabits.acceptance.steps.ListHabitsSteps.clickMenu
import org.isoron.uhabits.acceptance.steps.ListHabitsSteps.pressCheckmarks
import org.isoron.uhabits.core.models.Entry.Companion.UNKNOWN
import org.isoron.uhabits.core.models.Entry.Companion.YES_MANUAL
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SettingsTest : BaseUserInterfaceTest() {

    @Test
    fun shouldToggleShortPress() {
        launchApp()

        clickMenu(SETTINGS)
        clickText("Settings")
        clickText("Toggle with short press")

       // device.findObject(UiSelector().description("Navigate up")).click()

        pressBack()

        verifyDisplaysCheckmarks("Wake up early", listOf(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN))
        pressCheckmarks("Wake up early", count = 1, false)
        verifyDisplaysCheckmarks("Wake up early", listOf(YES_MANUAL, UNKNOWN, UNKNOWN, UNKNOWN))
    }

    @Test
    fun shouldToggleDefaultLongPress() {
        launchApp()
        verifyDisplaysCheckmarks("Wake up early", listOf(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN))
        pressCheckmarks("Wake up early", count = 1)
        verifyDisplaysCheckmarks("Wake up early", listOf(YES_MANUAL, UNKNOWN, UNKNOWN, UNKNOWN))
    }
}