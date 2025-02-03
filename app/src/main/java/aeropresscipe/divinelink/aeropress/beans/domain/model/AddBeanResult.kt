package aeropresscipe.divinelink.aeropress.beans.domain.model

import aeropresscipe.divinelink.aeropress.ui.UIText
import com.divinelink.aeropress.recipes.R

/**
 * A collection of possible results for an add a new bean to the bean tracker feature.
 */
sealed class AddBeanResult {
  data object Success : AddBeanResult()

  sealed class Failure(
    error: UIText,
  ) : AddBeanResult() {

    data object Unknown : Failure(
      error = UIText.ResourceText(R.string.general_error_message),
    )

    data object EmptyName : Failure(
      error = UIText.ResourceText(R.string.AddBeans__error_empty_bean_name),
    )
  }
}
