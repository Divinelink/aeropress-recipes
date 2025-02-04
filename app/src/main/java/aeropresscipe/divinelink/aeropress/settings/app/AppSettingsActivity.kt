package aeropresscipe.divinelink.aeropress.settings.app

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
import com.divinelink.aeropress.recipes.R
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
  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    finish()
    startActivity(intent)
  }

  private enum class StartLocation(val code: Int) {
    HOME(0),
    HELP(1),
  }
}
