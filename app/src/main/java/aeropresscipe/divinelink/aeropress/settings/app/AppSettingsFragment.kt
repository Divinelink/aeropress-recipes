package aeropresscipe.divinelink.aeropress.settings.app

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.settings.DSLConfiguration
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsAdapter
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsIcon
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.app.help.HelpSettingsFragment
import aeropresscipe.divinelink.aeropress.settings.configure
import androidx.fragment.app.FragmentTransaction

class AppSettingsFragment : DSLSettingsFragment(R.string.settings) {

    override fun bindAdapter(adapter: DSLSettingsAdapter) {
        adapter.submitList(getConfiguration().toMappingModelList())
    }

    private fun getConfiguration(): DSLConfiguration {
        return configure {

            clickPref(
                title = DSLSettingsText.from(R.string.HelpSettingsFragment__help),
                icon = DSLSettingsIcon.from(R.drawable.ic_help_24),
                onClick = {
                    parentFragmentManager.beginTransaction()
                        .add(R.id.nav_host_fragment, HelpSettingsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(HelpSettingsFragment::class.java.name)
                        .commit()
                }
            )

            dividerPref()
        }
    }
}