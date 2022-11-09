package aeropresscipe.divinelink.aeropress.favorites.adapter

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.util.mapping.EmptyMappingModel
import aeropresscipe.divinelink.aeropress.util.mapping.MappingModel
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class EmptyType<T : MappingModel>(
    @StringRes var text: Int,
    @DrawableRes var image: Int
) : EmptyMappingModel {
    object EmptyHistory : EmptyType<EmptyHistory>(text = R.string.empty_history_text, image = R.drawable.ic_history_fragment_image)
    object EmptyFavorites : EmptyType<EmptyFavorites>(text = R.string.empty_favorites_text, image = R.drawable.ic_heart)
}
