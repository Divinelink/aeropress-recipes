package aeropresscipe.divinelink.aeropress.settings

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.settings.app.AppSettingsFragment
import aeropresscipe.divinelink.aeropress.util.DynamicNoActionBarTheme
import aeropresscipe.divinelink.aeropress.util.DynamicTheme
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import gr.divinelink.core.util.extensions.addBackPressCallback


open class DSLSettingsActivity : AppCompatActivity() {
    protected open val dynamicTheme: DynamicTheme = DynamicNoActionBarTheme()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dsl_settings_activity)

        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, AppSettingsFragment())
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()

        dynamicTheme.onCreate(this)

        onBackPressedDispatcher.addBackPressCallback(this) { onNavigateUp() }
    }

    override fun onResume() {
        super.onResume()
        dynamicTheme.onResume(this)
    }

    override fun onNavigateUp(): Boolean {
        return if (!supportFragmentManager.popBackStackImmediate()) {
            onWillFinish()
            finish()
            true
        } else {
            false
        }
    }

    protected open fun onWillFinish() {
        // Intentionally Blank.
    }
}
