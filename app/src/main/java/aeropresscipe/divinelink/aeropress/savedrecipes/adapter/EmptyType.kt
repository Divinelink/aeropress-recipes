package aeropresscipe.divinelink.aeropress.savedrecipes.adapter

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class EmptyType(
    @StringRes var text: Int,
    @DrawableRes var image: Int
) {
    object EmptyHistory : EmptyType(text = R.string.empty_history_text, image = R.drawable.ic_history_fragment_image)
    object EmptyFavorites : EmptyType(text = R.string.empty_favorites_text, image = R.drawable.ic_heart)
}
