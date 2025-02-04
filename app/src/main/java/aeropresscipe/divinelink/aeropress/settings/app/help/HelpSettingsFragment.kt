package aeropresscipe.divinelink.aeropress.settings.app.help

import aeropresscipe.divinelink.aeropress.settings.DSLConfiguration
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsAdapter
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.configure
import com.divinelink.aeropress.recipes.BuildConfig
import com.divinelink.aeropress.recipes.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpSettingsFragment : DSLSettingsFragment(R.string.HelpSettingsFragment__help) {
  override fun bindAdapter(adapter: DSLSettingsAdapter) {
    adapter.submitList(getConfiguration().toMappingModelList())
  }

  private fun getConfiguration(): DSLConfiguration = configure {
    textPref(
      title = DSLSettingsText.from(R.string.HelpSettingsFragment__version),
      summary = DSLSettingsText.from(BuildConfig.VERSION_NAME),
    )

    dividerPref()

    externalLinkPref(
      title = DSLSettingsText.from(R.string.HelpSettingsFragment__terms_amp_privacy_policy),
      linkId = R.string.terms_and_privacy_policy_url,
    )
  }
}
