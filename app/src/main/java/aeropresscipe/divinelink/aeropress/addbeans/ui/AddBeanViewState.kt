package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.ui.UIText

sealed class AddBeanViewState(
    open val showLoading: Boolean = true,
    open val bean: Bean = emptyBean(),
    open val submitButtonText: UIText = UIText.ResourceText(R.string.save),
    open val title: UIText = UIText.ResourceText(R.string.AddBeans__add_title),
    open val openRoastLevelDrawer: Boolean = false,
    open val openProcessMethodDrawer: Boolean = false,
) {

    object Initial : AddBeanViewState()

    data class InsertBean(
        override val bean: Bean = emptyBean(),
        override val openProcessMethodDrawer: Boolean = false,
        override val openRoastLevelDrawer: Boolean = false,
    ) : AddBeanViewState(
        submitButtonText = UIText.ResourceText(R.string.save),
        bean = bean,
        showLoading = false,
        title = UIText.ResourceText(R.string.AddBeans__add_title),
        openProcessMethodDrawer = openProcessMethodDrawer,
        openRoastLevelDrawer = openRoastLevelDrawer,
    )

    data class UpdateBean(
        override val bean: Bean,
        override val openProcessMethodDrawer: Boolean = false,
        override val openRoastLevelDrawer: Boolean = false,
    ) : AddBeanViewState(
        bean = bean,
        submitButtonText = UIText.ResourceText(R.string.update),
        showLoading = false,
        title = UIText.ResourceText(R.string.AddBeans__update_title),
        openProcessMethodDrawer = openProcessMethodDrawer,
        openRoastLevelDrawer = openRoastLevelDrawer,
    )

    data class Completed(
        override val submitButtonText: UIText = UIText.ResourceText(R.string.save),
        override val title: UIText = UIText.ResourceText(R.string.AddBeans__add_title),
    ) : AddBeanViewState(
        showLoading = false,
        submitButtonText = submitButtonText,
        title = title,
    )

    data class Error(
        override val bean: Bean,
        override val submitButtonText: UIText = UIText.ResourceText(R.string.save),
        override val title: UIText = UIText.ResourceText(R.string.AddBeans__add_title),
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
