package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.StringRes

sealed class LikeSnackBar(@StringRes val string: Int, @StringRes val favorites: Int) {
    object Like : LikeSnackBar(R.string.save_recipe_notification, R.string.favorites)
    object Remove : LikeSnackBar(R.string.remove_recipe_notification, R.string.favorites)
}
