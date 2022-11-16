package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.AddBeanContent
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeansTrackerFragment : Fragment() {

    private lateinit var composeView: ComposeView

    @Suppress("UnusedPrivateMember")
    private val viewModel: BeansTrackerViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {
            AeropressTheme {
                AddBeanContent(
                    onDateChanged = { },
                    onRoastLevelClick = { },
                    onProcessClick = { },
                    onBeanNameChanged = {},
                    onRoasterNameChanged = {},
                    onOriginChanged = {},
                    onRoastLevelChanged = {},
                    onProcessChanged = {},
                )
            }
        }
    }
}
