package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.setNavigationBarColor

@AndroidEntryPoint
class AddBeanActivity : ComponentActivity() {

    private val viewModel: AddBeanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground))
        setContent {
            AeropressTheme {
                val viewState = viewModel.viewState.collectAsState()

                AddBeanContent(
                    onBeanNameChanged = {},
                    onRoasterNameChanged = {},
                    onOriginChanged = {},
                    onDateChanged = {},
                    onRoastLevelChanged = {},
                    onProcessChanged = {},
                    onRoastLevelClick = {},
                    onProcessClick = {}
                )
            }
        }
    }
}
