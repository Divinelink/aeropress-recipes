package aeropresscipe.divinelink.aeropress.settings.app

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsActivity
import aeropresscipe.divinelink.aeropress.settings.SettingsViewModel
import aeropresscipe.divinelink.aeropress.util.updateForTheme
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.setNavigationBarColor
import kotlinx.coroutines.launch

private const val START_LOCATION = "app.settings.start.location"

@AndroidEntryPoint
class AppSettingsActivity :
    DSLSettingsActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.theme.collect { theme ->
                        updateForTheme(theme)
                    }
                }
            }
        }

        intent = intent.putExtra(START_LOCATION, StartLocation.HOME)
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        finish()
        startActivity(intent)
    }

    private enum class StartLocation(val code: Int) {
        HOME(0),
        HELP(1),
//        PROXY(3),
//        NOTIFICATIONS(4),
//        CHANGE_NUMBER(5),
//        SUBSCRIPTIONS(6),
//        BOOST(7),
//        MANAGE_SUBSCRIPTIONS(8),
//        NOTIFICATION_PROFILES(9),
//        CREATE_NOTIFICATION_PROFILE(10),
//        NOTIFICATION_PROFILE_DETAILS(11);
    }
}
