package aeropresscipe.divinelink.aeropress.history

import androidx.annotation.StringRes
import com.divinelink.aeropress.recipes.R

sealed class LikeSnackBar(@StringRes val string: Int, @StringRes val favorites: Int) {
    object Like : LikeSnackBar(R.string.save_recipe_notification, R.string.favorites)
    object Remove : LikeSnackBar(R.string.remove_recipe_notification, R.string.favorites)
}
