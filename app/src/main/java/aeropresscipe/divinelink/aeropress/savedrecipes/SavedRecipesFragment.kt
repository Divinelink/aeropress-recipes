package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.HomeView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.FragmentSavedRecipesBinding
import gr.divinelink.core.util.swipe.SwipeAction
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class SavedRecipesFragment : Fragment(), SavedRecipesView {
    private var binding: FragmentSavedRecipesBinding? = null

    private var presenter: SavedRecipesPresenter? = null
    private var homeView: HomeView? = null
    private var mFadeAnimation: Animation? = null

    private val recipesAdapter by lazy {
        SavedRecipesAdapter(requireContext(),
            object : SavedRecipesAdapterDelegate {
                override fun brewItem(position: Int) {
                    presenter?.getSpecificRecipeToStartNewBrew(requireContext(), position)
                }

                override fun deleteItem(recipe: SavedRecipeDomain, position: Int) {
                    presenter?.deleteRecipe(recipe, requireContext())
                }
            }) { recipe: SavedRecipeDomain?, swipeAction: SwipeAction ->
            when (swipeAction.actionId) {
                R.id.delete -> showDeleteRecipeDialog(recipe)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)

        homeView = arguments?.getSerializable("home_view") as HomeView?

        val view = binding?.root


        val layoutManager = LinearLayoutManager(activity)
        binding?.savedRecipesRV?.layoutManager = layoutManager
        presenter = SavedRecipesPresenterImpl(this)
        presenter?.getSavedRecipes(context)

        return view

    }

    override fun showSavedRecipes(savedRecipes: List<SavedRecipeDomain>) {
        activity?.runOnUiThread {
            mFadeAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites)
            binding?.savedRecipesRV?.animation = mFadeAnimation
            binding?.savedRecipesRV?.adapter = recipesAdapter
            recipesAdapter.submitList(savedRecipes)
        }
    }

    override fun showSavedRecipesAfterDeletion(
        savedRecipes: List<SavedRecipeDomain>,
    ) {
        activity?.runOnUiThread {
            recipesAdapter.submitList(savedRecipes)
        }
    }

    override fun passData(bloomTime: Int, brewTime: Int, bloomWater: Int, brewWater: Int) {
        activity?.runOnUiThread {
            val diceUI = DiceUI(bloomTime, brewTime, bloomWater, brewWater)
            diceUI.isNewRecipe = true
            homeView?.startTimerActivity(diceUI)
        }
    }


    override fun showEmptyListMessage() {
        activity?.runOnUiThread {
            beginFading(
                binding?.savedRecipesRV,
                AnimationUtils.loadAnimation(activity, R.anim.fade_out_favourites),
                View.GONE
            )
            beginFading(
                binding?.emptyListLayout,
                AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites),
                View.VISIBLE
            )

        }
    }

    private fun beginFading(view: View?, animation: Animation, visibility: Int) {
        view?.startAnimation(animation)
        view?.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showDeleteRecipeDialog(recipe: SavedRecipeDomain?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.deleteRecipeDialogTitle)
            .setMessage(R.string.deleteRecipeDialogMessage)
            .setPositiveButton(R.string.delete) { _, _ ->
                presenter?.deleteRecipe(recipe, requireContext())
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(homeView: HomeView?): SavedRecipesFragment {
            val fragment = SavedRecipesFragment()
            val args = Bundle()
            args.putSerializable("home_view", homeView)
            fragment.arguments = args
            return fragment
        }
    }
}