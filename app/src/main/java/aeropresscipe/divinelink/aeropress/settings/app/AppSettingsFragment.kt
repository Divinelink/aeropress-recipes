package aeropresscipe.divinelink.aeropress.settings.app

import aeropresscipe.divinelink.aeropress.settings.DSLConfiguration
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsAdapter
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsIcon
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.app.appearance.AppearanceSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.app.help.HelpSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.app.notifications.NotificationsSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.configure
import androidx.fragment.app.FragmentTransaction
import com.divinelink.aeropress.recipes.R

class AppSettingsFragment : DSLSettingsFragment(R.string.settings) {

  override fun bindAdapter(adapter: DSLSettingsAdapter) {
    adapter.submitList(getConfiguration().toMappingModelList())
  }

  private fun getConfiguration(): DSLConfiguration = configure {
    clickPref(
      title = DSLSettingsText.from(R.string.preferences__appearance),
      icon = DSLSettingsIcon.from(R.drawable.ic_appearance_24),
      onClick = {
        updateFragment(AppearanceSettingsFragment())
      },
    )

    clickPref(
      title = DSLSettingsText.from(R.string.NotificationsSettingsFragment__notifications),
      icon = DSLSettingsIcon.from(R.drawable.ic_bell_24),
      onClick = {
        updateFragment(NotificationsSettingsFragment())
      },
    )

    dividerPref()

    clickPref(
      title = DSLSettingsText.from(R.string.HelpSettingsFragment__help),
      icon = DSLSettingsIcon.from(R.drawable.ic_help_24),
      onClick = {
        updateFragment(HelpSettingsFragment())
      },
    )

    dividerPref()
  }

  private fun updateFragment(fragment: DSLSettingsFragment) {
    parentFragmentManager.beginTransaction()
      .add(R.id.nav_host_fragment, fragment)
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
      .addToBackStack(fragment::class.java.name)
      .commit()
  }
}
