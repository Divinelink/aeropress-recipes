package aeropresscipe.divinelink.aeropress.beans.domain.model

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.UIText

/**
 * A collection of possible results for an add a new bean to the bean tracker feature.
 */
sealed class AddBeanResult {
    object Success : AddBeanResult()

    sealed class Failure(
        error: UIText,
    ) : AddBeanResult() {

        object Unknown : Failure(
            error = UIText.ResourceText(R.string.general_error_message),
        )

        object EmptyName : Failure(
            error = UIText.ResourceText(R.string.AddBeans__error_empty_bean_name),
        )
    }
}
