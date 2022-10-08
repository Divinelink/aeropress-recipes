package aeropresscipe.divinelink.aeropress.settings.app

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsActivity
import aeropresscipe.divinelink.aeropress.settings.ISettingsViewModel
import aeropresscipe.divinelink.aeropress.settings.SettingsState
import aeropresscipe.divinelink.aeropress.settings.SettingsStateHandler
import aeropresscipe.divinelink.aeropress.settings.SettingsViewModel
import aeropresscipe.divinelink.aeropress.settings.SettingsViewModelAssistedFactory
import aeropresscipe.divinelink.aeropress.settings.SettingsViewModelFactory
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.setNavigationBarColor
import java.lang.ref.WeakReference
import javax.inject.Inject

private const val START_LOCATION = "app.settings.start.location"

@AndroidEntryPoint
class AppSettingsActivity :
    DSLSettingsActivity(),
    ISettingsViewModel,
    SettingsStateHandler {

    @Inject
    lateinit var assistedFactory: SettingsViewModelAssistedFactory
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground))
        val viewModelFactory = SettingsViewModelFactory(assistedFactory, WeakReference<ISettingsViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory)[(SettingsViewModel::class.java)]

        intent = intent.putExtra(START_LOCATION, StartLocation.HOME)
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        finish()
        startActivity(intent)
    }

    override fun updateState(state: SettingsState) {
        when (state) {
            is SettingsState.ErrorState -> {
                // Intentionally Blank.
            }
            is SettingsState.InitialState -> {
                // Intentionally Blank.
            }
            is SettingsState.LoadingState -> {
                // Intentionally Blank.
            }
        }
    }

    override fun handleInitialState() {
        // Intentionally Blank.
    }

    override fun handleLoadingState() {
        // Intentionally Blank.
    }

    override fun handleErrorState() {
        // Intentionally Blank.
    }

    override fun onDestroy() {
        super.onDestroy()
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
