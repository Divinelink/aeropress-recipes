package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.ListRecipeItemBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView

class GenerateRecipeListView(
    var steps: MutableList<RecipeStep>,
    context: Context,
) : ArrayAdapter<RecipeStep>(context,
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

        val stepNumber = position + 1
        viewHolder.stepNumber.text = stepNumber.toString()
        viewHolder.stepText.text = context.getText(steps[position].stepText)

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
