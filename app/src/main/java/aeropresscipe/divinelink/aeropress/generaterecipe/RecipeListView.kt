package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.ListRecipeItemBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.models.RecipeStep
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.divinelink.core.util.extensions.toFahrenheit
import gr.divinelink.core.util.extensions.toSpanned

class RecipeListView(
    private var steps: MutableList<RecipeStep>,
    context: Context,
) : ArrayAdapter<RecipeStep>(
    context,
    R.layout.list_recipe_item,
    steps) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: RecipeViewHolder
        var view = convertView
        if (view == null) {
            val binding = ListRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            viewHolder = RecipeViewHolder(binding)
            view = binding.root
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as RecipeViewHolder
        }
        val stepText = when (val step = steps[position]) {
            is RecipeStep.HeatWaterStep -> context.getString(step.stepText, step.waterAmount, step.temperature, step.temperature.toFahrenheit())
            is RecipeStep.CoffeeGrindStep -> context.getString(step.stepText, step.coffeeAmount, step.grindSize.size)
            is RecipeStep.StandardMethodStep -> context.getString(step.stepText)
            is RecipeStep.InvertedMethodStep -> context.getString(step.stepText)
            is RecipeStep.PourGroundCoffeeStep -> context.getString(step.stepText)
            is RecipeStep.BloomStep -> context.getString(step.stepText, step.bloomWater, step.bloomTime)
            is RecipeStep.RemainingWaterStep -> context.getString(step.stepText, step.remainingWater)
            is RecipeStep.PourWaterStep -> context.getString(step.stepText, step.waterAmount)
            is RecipeStep.WaitToBrewStep -> context.getString(step.stepText, step.minutes, step.seconds,
                context.resources.getQuantityString(R.plurals.minutes, step.minutes.toInt() + step.seconds.toInt()))
            is RecipeStep.FlipToNormalOrientation -> context.getString(step.stepText)
            is RecipeStep.PressStep -> context.getString(step.stepText)
        }

        val stepNumber = position + 1
        viewHolder.stepNumber.text = stepNumber.toString()
        viewHolder.stepText.text = stepText.toSpanned()
        return view
    }

    override fun getCount(): Int {
        return steps.size
    }

    inner class RecipeViewHolder(binding: ListRecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val stepNumber = binding.stepNumber
        val stepText = binding.stepText
    }
}
