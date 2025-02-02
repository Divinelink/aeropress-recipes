package aeropresscipe.divinelink.aeropress.settings.models

import aeropresscipe.divinelink.aeropress.settings.PreferenceModel
import aeropresscipe.divinelink.aeropress.util.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.util.mapping.MappingAdapter
import aeropresscipe.divinelink.aeropress.util.mapping.MappingViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.updateLayoutParams
import com.divinelink.aeropress.recipes.databinding.DslSpacePreferenceBinding

/**
 * Adds extra space between elements in a DSL fragment
 */
data class Space(
    @Px val pixels: Int
) {

    companion object {
        fun register(mappingAdapter: MappingAdapter) {
            mappingAdapter.registerFactory(LayoutFactory({ i: LayoutInflater, r: ViewGroup -> ViewHolder(DslSpacePreferenceBinding.inflate(i, r, false)) }))
        }
    }

    class Model(val space: Space) : PreferenceModel<Model>() {
        override fun areItemsTheSame(newItem: Any): Boolean {
            return true
        }

        override fun areContentsTheSame(newItem: Any): Boolean {
            newItem as Model
            return super.areContentsTheSame(newItem) && newItem.space == space
        }
    }

    class ViewHolder(binding: DslSpacePreferenceBinding) : MappingViewHolder<Model>(binding.root) {
        override fun bind(model: Model) {
            itemView.updateLayoutParams {
                height = model.space.pixels
            }
        }
    }
}
