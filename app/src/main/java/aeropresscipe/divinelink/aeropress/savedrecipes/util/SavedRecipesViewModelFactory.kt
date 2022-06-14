package aeropresscipe.divinelink.aeropress.savedrecipes.util

import aeropresscipe.divinelink.aeropress.savedrecipes.ISavedRecipesViewModel
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesRepository
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.ref.WeakReference

class SavedRecipesViewModelFactory(
    private val app: Application,
    private var delegate: WeakReference<ISavedRecipesViewModel>?,
    private val repository: SavedRecipesRepository
) :
    ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SavedRecipesViewModel::class.java)) {
            return SavedRecipesViewModel(app, delegate, repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
