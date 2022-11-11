package aeropresscipe.divinelink.aeropress.beans.domain.model

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.StringRes

sealed class ProcessMethod(
    @StringRes open var stringRes: Int
) {
    object Washed : ProcessMethod(stringRes = R.string.ProcessMethod__washed)
    object Natural : ProcessMethod(stringRes = R.string.ProcessMethod__natural)
    object Honey : ProcessMethod(stringRes = R.string.ProcessMethod__honey)
    object Anaerobic : ProcessMethod(stringRes = R.string.ProcessMethod__anaerobic)
    object CarbonicMaceration : ProcessMethod(stringRes = R.string.ProcessMethod__carbonicMaceration)
    object GilingBasah : ProcessMethod(stringRes = R.string.ProcessMethod__gilingBasah)
}
