package aeropresscipe.divinelink.aeropress.beans.domain.model

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.StringRes

enum class ProcessMethod(
    @StringRes var stringRes: Int,
) {
    Washed(stringRes = R.string.ProcessMethod__washed),
    Natural(stringRes = R.string.ProcessMethod__natural),
    Honey(stringRes = R.string.ProcessMethod__honey),
    Anaerobic(stringRes = R.string.ProcessMethod__anaerobic),
    CarbonicMaceration(stringRes = R.string.ProcessMethod__carbonicMaceration),
    GilingBasah(stringRes = R.string.ProcessMethod__gilingBasah),
}

fun String.toProcessMethod(): ProcessMethod {
    return when (this) {
        "Washed" -> ProcessMethod.Washed
        "Natural" -> ProcessMethod.Natural
        "Honey" -> ProcessMethod.Honey
        "Anaerobic" -> ProcessMethod.Anaerobic
        "CarbonicMaceration" -> ProcessMethod.CarbonicMaceration
        "GilingBasah" -> ProcessMethod.GilingBasah
        else -> throw IllegalArgumentException("Unknown Process Method.")
    }
}
