package aeropresscipe.divinelink.aeropress.beans.domain.model

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.StringRes

enum class ProcessMethod(
    @StringRes var stringRes: Int,
    val value: String,
) {
    Washed(
        stringRes = R.string.ProcessMethod__washed,
        value = "Washed",
    ),
    Natural(
        stringRes = R.string.ProcessMethod__natural,
        value = "Natural"
    ),
    Honey(
        stringRes = R.string.ProcessMethod__honey,
        value = "Honey"
    ),
    Anaerobic(
        stringRes = R.string.ProcessMethod__anaerobic,
        value = "Anaerobic"
    ),
    CarbonicMaceration(
        stringRes = R.string.ProcessMethod__carbonicMaceration,
        value = "Carbonic Maceration"
    ),
    GilingBasah(
        stringRes = R.string.ProcessMethod__gilingBasah,
        value = "Giling Basah"
    ),
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
