package aeropresscipe.divinelink.aeropress.finish

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.recipecard.RecipeCard
import aeropresscipe.divinelink.aeropress.databinding.ActivityFinishBinding
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.model.KeyPath
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.changeLayersColor
import gr.divinelink.core.util.extensions.getSerializable
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

    private var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setNavigationBarColor(this, ContextCompat.getColor(this, R.color.colorBackground))

        recipe = intent?.getSerializable<Recipe>(EXTRA_RECIPE)

        val viewModelFactory = FinishViewModelFactory(assistedFactory, WeakReference<IFinishViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory)[(FinishViewModel::class.java)]
        viewModel.delegate = WeakReference(this)

        viewModel.init(recipe)
    }

    override fun updateState(state: FinishState) {
        when (state) {
            is FinishState.InitialState -> handleInitialState()
            is FinishState.CloseState -> handleCloseState()
            is FinishState.SetupRecipeState -> handleSetupRecipeState(state)
        }
    }

    override fun handleInitialState() {
        binding.apply {
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
        }
    }

    override fun handleSetupRecipeState(state: FinishState.SetupRecipeState) {
        binding.card.setRecipe(RecipeCard.FinishCard(recipe = state.recipe))
        binding.likeButtonCardLayout.recipe = state.recipe
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
