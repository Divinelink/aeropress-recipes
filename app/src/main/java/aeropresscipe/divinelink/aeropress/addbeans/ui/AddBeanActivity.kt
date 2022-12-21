package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.getSerializable
import gr.divinelink.core.util.utils.setNavigationBarColor

@AndroidEntryPoint
class AddBeanActivity : ComponentActivity() {

    private val viewModel: AddBeanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bean = intent.getSerializable<Bean?>(BEAN)

        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground))
        setContent {
            AeropressTheme {
                val viewState = viewModel.viewState.collectAsState()

                viewModel.setBean(bean)

                AddBeanContent(
                    viewState = viewState.value,
                    onBeanNameChanged = viewModel::onBeanNameChanged,
                    onRoasterNameChanged = viewModel::onRoasterNameChanged,
                    onOriginChanged = viewModel::onOriginChanged,
                    onDateChanged = viewModel::onDateChanged,
                    onRoastLevelChanged = viewModel::onRoastLevelChanged,
                    onProcessChanged = viewModel::onProcessChanged,
                    onRoastLevelClick = viewModel::onRoastLevelClick,
                    onProcessClick = viewModel::onProcessClick,
                )
            }
        }
    }

    companion object {
        private const val BEAN = "BEAN"

        fun newIntent(
            context: Context?,
            bean: Bean?,
        ): Intent {
            val intent = Intent(context, AddBeanActivity::class.java)
            intent.putExtra(BEAN, bean)
            return intent
        }
    }
}
