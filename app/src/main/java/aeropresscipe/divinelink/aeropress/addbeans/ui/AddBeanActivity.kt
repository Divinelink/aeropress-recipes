package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import com.divinelink.aeropress.recipes.R
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.getSerializable
import gr.divinelink.core.util.utils.setNavigationBarColor

@AndroidEntryPoint
class AddBeanActivity : AppCompatActivity() {

  private val viewModel: AddBeanViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground))

    val bean = intent.getSerializable<Bean?>(BEAN)
    enableEdgeToEdge()
    setContent {
      AeropressTheme {
        val viewState = viewModel.viewState.collectAsState()
        viewModel.setBean(bean)
        Surface {
          AddBeanContent(
            viewState = viewState.value,
            onBeanNameChanged = viewModel::onBeanNameChanged,
            onRoasterNameChanged = viewModel::onRoasterNameChanged,
            onOriginChanged = viewModel::onOriginChanged,
            onDateChanged = viewModel::onDateChanged,
            onRoastLevelClick = viewModel::onRoastLevelClicked,
            onProcessClick = viewModel::onProcessClicked,
            onSubmitClicked = viewModel::onSubmitClicked,
            onDeleteClicked = viewModel::onDeleteBeanClicked,
            onOptionSelectedFromBottomSheet = viewModel::onSelectFromBottomSheet,
            onRatingChanged = viewModel::onRatingChanged,
            navigateUp = { finish() },
          )
        }
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
