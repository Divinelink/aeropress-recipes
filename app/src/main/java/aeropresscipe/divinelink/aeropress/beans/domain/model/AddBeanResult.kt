package aeropresscipe.divinelink.aeropress.beans.domain.model

/**
 * A collection of possible results for an add a new bean to the bean tracker feature.
 */
sealed class AddBeanResult {
    object Success : AddBeanResult()
    object Failure : AddBeanResult()
}
