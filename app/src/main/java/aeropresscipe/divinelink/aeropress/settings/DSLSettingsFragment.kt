package aeropresscipe.divinelink.aeropress.settings

import aeropresscipe.divinelink.aeropress.R
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class DSLSettingsFragment(
    @StringRes private val titleId: Int = -1,
    @MenuRes private val menuId: Int = -1,
    @LayoutRes layoutId: Int = R.layout.dsl_settings_fragment,
    protected var layoutManagerProducer: (Context) -> RecyclerView.LayoutManager = { context -> LinearLayoutManager(context) }
) : Fragment(layoutId) {

    protected var recyclerView: RecyclerView? = null
        private set

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar: Toolbar? = view.findViewById(R.id.toolbar)

        if (titleId != -1) {
            toolbar?.setTitle(titleId)
        }

        toolbar?.setNavigationOnClickListener {
            onToolbarNavigationClicked()
        }

        if (menuId != -1) {
            toolbar?.inflateMenu(menuId)
            toolbar?.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        }

        val settingsAdapter = DSLSettingsAdapter()

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = layoutManagerProducer(requireContext())
            adapter = settingsAdapter
        }

        bindAdapter(settingsAdapter)
    }

    open fun onToolbarNavigationClicked() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }

    abstract fun bindAdapter(adapter: DSLSettingsAdapter)
}
