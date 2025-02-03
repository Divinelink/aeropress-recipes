package aeropresscipe.divinelink.aeropress.settings.models

import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.PreferenceModel
import aeropresscipe.divinelink.aeropress.util.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.util.mapping.MappingAdapter
import aeropresscipe.divinelink.aeropress.util.mapping.MappingViewHolder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.divinelink.aeropress.recipes.databinding.DslTextPreferenceBinding

/**
 * A Text without any padding, allowing for exact padding to be handed in at runtime.
 */
data class Text(
    val text: DSLSettingsText,
) {

    companion object {
        fun register(adapter: MappingAdapter) {
            adapter.registerFactory(LayoutFactory({ i: LayoutInflater, r: ViewGroup -> ViewHolder(DslTextPreferenceBinding.inflate(i, r, false)) }))
        }
    }

    class Model(val paddableText: Text) : PreferenceModel<Model>() {
        override fun areItemsTheSame(newItem: Any): Boolean {
            return true
        }

        override fun areContentsTheSame(newItem: Any): Boolean {
            newItem as Model
            return super.areContentsTheSame(newItem) && newItem.paddableText == paddableText
        }
    }

    class ViewHolder(binding: DslTextPreferenceBinding) : MappingViewHolder<Model>(binding.root) {
        private val text: TextView = binding.title

        override fun bind(model: Model) {
            text.text = model.paddableText.text.resolve(context)

            val clickableSpans = (text.text as? Spanned)?.getSpans(0, text.text.length, ClickableSpan::class.java)
            if (clickableSpans?.isEmpty() == false) {
                text.movementMethod = LinkMovementMethod.getInstance()
            } else {
                text.movementMethod = null
            }
        }
    }
}
