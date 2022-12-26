package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import androidx.annotation.StringRes

sealed class AddBeanViewState(
    open val showLoading: Boolean = true,
    open val bean: Bean = emptyBean(),
    @StringRes open val submitButtonText: Int = R.string.save,
    @StringRes open val title: Int = R.string.AddBeans__add_title,
) {

    object Initial : AddBeanViewState()

    data class InsertBean(
        override val bean: Bean = emptyBean(),
    ) : AddBeanViewState(
        submitButtonText = R.string.save,
        bean = bean,
        showLoading = false,
        title = R.string.AddBeans__add_title,
    )

    data class UpdateBean(
        override val bean: Bean,
    ) : AddBeanViewState(
        bean = bean,
        submitButtonText = R.string.update,
        showLoading = false,
        title = R.string.AddBeans__update_title,
    )

    data class Completed(
        override val submitButtonText: Int = R.string.save,
        override val title: Int = R.string.AddBeans__add_title,
    ) : AddBeanViewState(
        showLoading = false,
        submitButtonText = submitButtonText,
        title = title,
    )

    data class Error(
        override val bean: Bean,
        override val submitButtonText: Int = R.string.save,
        override val title: Int = R.string.AddBeans__add_title,
    ) : AddBeanViewState(
        bean = bean,
        showLoading = false,
        submitButtonText = submitButtonText,
        title = title,
    )
}

private fun emptyBean(): Bean {
    return Bean(
        id = "",
        name = "",
        roasterName = "",
        origin = "",
        roastDate = null,
        roastLevel = null,
        process = null,
        rating = 0,
        tastingNotes = "",
        additionalNotes = ""
    )
}
