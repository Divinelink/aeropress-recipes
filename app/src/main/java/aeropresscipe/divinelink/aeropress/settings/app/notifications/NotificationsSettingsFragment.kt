package aeropresscipe.divinelink.aeropress.settings.app.notifications

import aeropresscipe.divinelink.aeropress.settings.DSLConfiguration
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsAdapter
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.configure
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.divinelink.aeropress.recipes.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsSettingsFragment :
  DSLSettingsFragment(R.string.NotificationsSettingsFragment__notifications) {

  private val viewModel: NotificationsSettingsViewModel by viewModels()

  override fun bindAdapter(adapter: DSLSettingsAdapter) {
    lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { adapter.submitList(getConfiguration(it).toMappingModelList()) }
      }
    }
  }

  private fun getConfiguration(state: NotificationSettingsState): DSLConfiguration = configure {
    sectionHeaderPref(R.string.NotificationsSettingsFragment__sound)

    switchPref(
      title = DSLSettingsText.from(R.string.preferences__timer_sound),
      summary = DSLSettingsText.from(R.string.preferences__timer_sound_summary),
      isChecked = state.soundEnabled,
      onClick = {
        viewModel.setTimerSoundEnabled(!state.soundEnabled)
      },
    )
  }
}
