package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import androidx.annotation.StringRes

sealed class AddBeanViewState(
    open val showLoading: Boolean = true,
    open val bean: Bean = emptyBean(),
    @StringRes open val submitButtonText: Int = R.string.save,
) {

    object Initial : AddBeanViewState()

    data class InsertBean(
        override val bean: Bean = emptyBean(),
    ) : AddBeanViewState(
        submitButtonText = R.string.save,
        bean = bean,
        showLoading = false,
    )

    data class UpdateBean(
        override val bean: Bean,
    ) : AddBeanViewState(
        bean = bean,
        submitButtonText = R.string.update,
        showLoading = false,
    )
}

private fun emptyBean(): Bean {
    return Bean(
        id = "",
        name = "",
        roasterName = "",
        origin = "",
        roastDate = "",
        roastLevel = null,
        process = null,
        rating = 0,
        tastingNotes = "",
        additionalNotes = ""
    )
}
