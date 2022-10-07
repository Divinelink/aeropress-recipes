package aeropresscipe.divinelink.aeropress.settings.adapter

import aeropresscipe.divinelink.aeropress.databinding.SingleSelectItemBinding
import aeropresscipe.divinelink.aeropress.util.mapping.MappingModel
import aeropresscipe.divinelink.aeropress.util.mapping.MappingViewHolder
import android.text.TextUtils
import android.view.View

/**
 * Single select (radio) setting option
 */

typealias onSelectionChanged = (selection: Any) -> Unit

class SingleSelectSetting {
    class ViewHolder(val binding: SingleSelectItemBinding, val onSelectionChanged: onSelectionChanged) : MappingViewHolder<Item>(binding.root) {
        override fun bind(model: Item) {
            binding.singleSelectItemRadio.isChecked = model.isSelected
            binding.singleSelectItemText.text = model.text
            if (!TextUtils.isEmpty(model.summaryText)) {
                binding.singleSelectItemSummary.text = model.summaryText
                binding.singleSelectItemSummary.visibility = View.VISIBLE
            } else {
                binding.singleSelectItemSummary.visibility = View.GONE
            }
            itemView.setOnClickListener { onSelectionChanged(model.item) }
        }
    }

    data class Item(val item: Any, val text: String?, val summaryText: String?, val isSelected: Boolean) : MappingModel {
        override fun areItemsTheSame(newItem: Any): Boolean {
            newItem as Item
            return item == newItem.item
        }

        override fun areContentsTheSame(newItem: Any): Boolean {
            newItem as Item
            return text == newItem.text && summaryText == newItem.summaryText && isSelected == newItem.isSelected
        }
    }
}
