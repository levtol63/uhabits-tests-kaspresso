package base
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import io.appium.java_client.AppiumBy
import org.junit.jupiter.api.*
import java.net.URL
import java.time.Duration
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {

    protected lateinit var driver: AndroidDriver
  //  protected val appPackage = "org.isoron.uhabits"
   // protected val listActivity = "org.isoron.uhabits.activities.habits.list.ListHabitsActivity"


    @BeforeAll
    fun setUp() {
        val options = UiAutomator2Options()
            .setPlatformName("Android")
            .setAutomationName("UiAutomator2")
            .setUdid("emulator-5554")
            .setApp("/Users/nikitasanin/Desktop/uhabits/uhabits-android/build/outputs/apk/debug/uhabits-android-debug.apk")
            .setDeviceName("Android Emulator")
            .setAppPackage("org.isoron.uhabits")
            .setAppActivity("org.isoron.uhabits.MainActivity")
            .setAutoGrantPermissions(true)

        driver = AndroidDriver(URL("http://127.0.0.1:4723"), options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3))
    }

    @AfterAll
    fun tearDown() {
        try {
            driver.terminateApp("org.isoron.uhabits")
        } catch (_: Exception) {}
        if (::driver.isInitialized) driver.quit()
    }

    @BeforeEach
    fun prepareApp() {
        skipWelcomeIfPresent()
     //   startActivityMobile(appPackage, listActivity)

    }

    /**
     * Пропускает экран приветствия, если он есть
     */
    protected fun skipWelcomeIfPresent() {
        try {
            val wait = WebDriverWait(driver, Duration.ofSeconds(2))

            val skipBtn = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.id("org.isoron.uhabits:id/skip")
                )
            )
            skipBtn.click()
            println("Welcome экран пропущен")
        } catch (e: Exception) {
            println("Welcome экран не показан, продолжаем тест")
        }
    }
   /* protected fun startActivityMobile(pkg: String, activity: String) {
        (driver as JavascriptExecutor).executeScript(
            "mobile: startActivity",
            mapOf("appPackage" to pkg, "appActivity" to activity)
        )
    } */
}
