package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.base.TimerViewCallback
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.DimensionUnit

@AndroidEntryPoint
class BeansTrackerFragment :
    Fragment(),
    TimerViewCallback {

    private lateinit var composeView: ComposeView

    private val viewModel: BeansTrackerViewModel by viewModels()

    private var bottomPadding: Dp = 0.dp

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {
            AeropressTheme {
                val viewState = viewModel.viewState.collectAsState()

                BeansContent(
                    viewState = viewState.value,
                    onAddButtonClicked = viewModel::onAddButtonClicked,
                    onBeanClicked = viewModel::onBeanClicked,
                    onAddBeanOpened = viewModel::onAddBeanOpened,
                    bottomPadding = bottomPadding,
                )
            }
        }
    }

    override fun updateBottomPadding(bottomPadding: Int) {
        this.bottomPadding = DimensionUnit.PIXELS.toDp(bottomPadding.toFloat()).dp
    }
}
