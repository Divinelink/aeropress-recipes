package aeropresscipe.divinelink.aeropress.base.mvi

interface IBaseViewModel {
  fun initUI()
  fun showLoader(show: Boolean)
  fun showDialog(
    dialogStrings: DialogStrings?,
    yesAction: (() -> Unit)? = null,
    noAction: (() -> Unit)? = null,
  )

  fun showError(error: String)
//    fun showError(error: ErrorWrapperModel)  // Replace String with ErrorWrapperModel when implemented.
}

data class DialogStrings(
  var title: String = "",
  var message: String? = null,
  var positiveButtonText: String? = null,
  var negativeButtonText: String? = null,
)
