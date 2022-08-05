package aeropresscipe.divinelink.aeropress.finish

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.customviews.Notification
import aeropresscipe.divinelink.aeropress.customviews.RecipeCard
import aeropresscipe.divinelink.aeropress.databinding.ActivityFinishBinding
import aeropresscipe.divinelink.aeropress.helpers.LottieHelper
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.model.KeyPath
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.changeLayersColor
import gr.divinelink.core.util.utils.WindowUtil.setNavigationBarColor
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class FinishActivity :
    AppCompatActivity(),
    IFinishViewModel,
    FinishStateHandler {
    private lateinit var binding: ActivityFinishBinding

    @Inject
    lateinit var assistedFactory: FinishViewModelAssistedFactory
    private lateinit var viewModel: FinishViewModel

    var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setNavigationBarColor(this, ContextCompat.getColor(this, R.color.colorBackground))

        recipe = intent?.getSerializableExtra(EXTRA_RECIPE) as Recipe?

        val viewModelFactory = FinishViewModelFactory(assistedFactory, WeakReference<IFinishViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(FinishViewModel::class.java)
    }

    override fun updateState(state: FinishState) {
        when (state) {
            is FinishState.ErrorState -> handleErrorState(state)
            is FinishState.InitialState -> handleInitialState()
            is FinishState.LoadingState -> handleLoadingState()
            is FinishState.CloseState -> handleCloseState()
            is FinishState.RecipeRemovedState -> handleRecipeRemovedState()
            is FinishState.RecipeSavedState -> handleRecipeSavedState()
            is FinishState.UpdateSavedIndicator -> handleUpdateSavedIndicator(state)
            is FinishState.ShowSnackBar -> handleShowSnackBar(state)
        }
    }

    override fun handleInitialState() {
        binding.apply {
            LottieHelper.updateLikeButton(binding.likeButton)
            finishAnimation.changeLayersColor(R.color.colorOnBackground, KeyPath("Layer 1", "**"))
            finishAnimation.changeLayersColor(R.color.colorOnBackground, KeyPath("Layer 2", "**"))
            finishAnimation.changeLayersColor(R.color.colorOnBackground, KeyPath("Layer 4", "**"))
            finishAnimation.changeLayersColor(R.color.colorOnBackground, KeyPath("Layer 5", "**"))
            finishAnimation.changeLayersColor(R.color.coffeeColor, KeyPath("Layer 9", "**"))
            finishAnimation.changeLayersColor(R.color.coffeeColor, KeyPath("Layer 8", "**"))
            finishAnimation.changeLayersColor(R.color.coffeeColor, KeyPath("Layer 7", "**"))
            finishAnimation.changeLayersColor(R.color.coffeeColor, KeyPath("Layer 6", "**"))
            finishAnimation.changeLayersColor(R.color.coffeeColor, KeyPath("Layer 3", "**"))

            close.setOnClickListener { viewModel.closeButtonClicked() }
            toolbar.setNavigationOnClickListener { viewModel.closeButtonClicked() }

            recipe?.let {
                binding.card.setRecipe(RecipeCard.FinishCard(recipe = it))
            }

        }
    }

    override fun handleLoadingState() {
        TODO("Not yet implemented")
    }

    override fun handleErrorState(state: FinishState.ErrorState) {
        TODO("Not yet implemented")
    }

    override fun handleRecipeSavedState() {
        binding.apply {
            likeButton.setMinAndMaxFrame(TimerFragment.LIKE_MIN_FRAME, TimerFragment.LIKE_MAX_FRAME)
            likeButton.playAnimation()
        }
    }

    override fun handleRecipeRemovedState() {
        binding.apply {
            likeButton.setMinAndMaxFrame(TimerFragment.DISLIKE_MIN_FRAME, TimerFragment.DISLIKE_MAX_FRAME)
            likeButton.playAnimation()
        }
    }

    override fun handleShowSnackBar(state: FinishState.ShowSnackBar) {
        Notification
            .make(binding.likeButton, resources.getString(state.value.string, getString(state.value.favorites)))
            .show()
    }

    override fun handleUpdateSavedIndicator(state: FinishState.UpdateSavedIndicator) {
        binding.likeButton.frame = state.frame
    }

    override fun handleCloseState() {
        finish()
    }

    companion object {
        private const val EXTRA_RECIPE = "EXTRA_RECIPE"

        @JvmStatic
        fun newIntent(
            context: Context?,
            recipe: Recipe?,
        ): Intent {
            val intent = Intent(context, FinishActivity::class.java)
            intent.putExtra(EXTRA_RECIPE, recipe)
            return intent
        }
    }

}
