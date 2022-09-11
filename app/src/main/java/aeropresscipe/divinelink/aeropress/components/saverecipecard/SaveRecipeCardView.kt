package aeropresscipe.divinelink.aeropress.components.saverecipecard

import aeropresscipe.divinelink.aeropress.components.snackbar.Notification
import aeropresscipe.divinelink.aeropress.databinding.ViewSaveRecipeCardBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.helpers.LottieHelper
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class SaveRecipeCardView :
    CardView,
    ISaveRecipeCardViewModel,
    SaveRecipeCardStateHandler {
    var binding: ViewSaveRecipeCardBinding = ViewSaveRecipeCardBinding.inflate(LayoutInflater.from(context), this, false)

    @Inject
    lateinit var assistedFactory: SaveRecipeCardViewModelAssistedFactory
    private lateinit var viewModel: SaveRecipeCardViewModel

    var recipe: Recipe? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        addView(binding.root)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val viewModelFactory = SaveRecipeCardViewModelFactory(assistedFactory, WeakReference<ISaveRecipeCardViewModel>(this))
        viewModel = ViewModelProvider(ViewTreeViewModelStoreOwner.get(this)!!, viewModelFactory)[SaveRecipeCardViewModel::class.java]
        viewModel.delegate = WeakReference(this)
        viewModel.init(recipe)
    }

    override fun updateState(state: SaveRecipeCardState) {
        when (state) {
            is SaveRecipeCardState.InitialState -> handleInitialState()
            is SaveRecipeCardState.RecipeRemovedState -> handleRecipeRemovedState()
            is SaveRecipeCardState.RecipeSavedState -> handleRecipeSavedState()
            is SaveRecipeCardState.ShowSnackBar -> handleShowSnackBar(state)
            is SaveRecipeCardState.UpdateSavedIndicator -> handleUpdateSavedIndicator(state)
        }
    }

    override fun handleInitialState() {
        binding.saveRecipeButton.setOnClickListener {
            viewModel.likeRecipe(recipe)
        }
        LottieHelper.updateLikeButton(binding.saveRecipeButton)
    }

    override fun handleRecipeSavedState() {
        binding.apply {
            saveRecipeButton.setMinAndMaxFrame(LIKE_MIN_FRAME, LIKE_MAX_FRAME)
            saveRecipeButton.playAnimation()
        }
    }

    override fun handleRecipeRemovedState() {
        binding.apply {
            saveRecipeButton.setMinAndMaxFrame(DISLIKE_MIN_FRAME, DISLIKE_MAX_FRAME)
            saveRecipeButton.playAnimation()
        }
    }

    override fun handleShowSnackBar(state: SaveRecipeCardState.ShowSnackBar) {
        Notification
            .make(binding.saveRecipeButton, resources.getString(state.value.string, context.getString(state.value.favorites)))
            .show()
    }

    override fun handleUpdateSavedIndicator(state: SaveRecipeCardState.UpdateSavedIndicator) {
        binding.saveRecipeButton.frame = state.frame
    }

    companion object {
        const val LIKE_MIN_FRAME = 0
        const val LIKE_MAX_FRAME = 80

        const val DISLIKE_MIN_FRAME = 80
        const val DISLIKE_MAX_FRAME = 130
    }
}
