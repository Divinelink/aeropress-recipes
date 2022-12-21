package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean

data class AddBeanViewState(
    val showLoading: Boolean = true,
    val bean: Bean = emptyBean(),
)

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
