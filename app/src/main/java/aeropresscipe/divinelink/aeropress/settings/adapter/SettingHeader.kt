package aeropresscipe.divinelink.aeropress.settings.adapter

import aeropresscipe.divinelink.aeropress.databinding.BaseSettingsHeaderItemBinding
import aeropresscipe.divinelink.aeropress.util.mapping.MappingModel
import aeropresscipe.divinelink.aeropress.util.mapping.MappingViewHolder

class SettingHeader {

    class ViewHolder(val binding: BaseSettingsHeaderItemBinding) : MappingViewHolder<Item>(binding.root) {
        override fun bind(model: Item) {
            if (model.text != null) {
                binding.baseSettingsHeaderItemText.text = model.text
            } else {
                binding.baseSettingsHeaderItemText.setText(model.textRes)
            }
        }
    }

    data class Item(val textRes: Int, val text: String?) : MappingModel {
        override fun areItemsTheSame(newItem: Any): Boolean {
            newItem as Item
            return textRes == newItem.textRes && text == newItem.text
        }

        override fun areContentsTheSame(newItem: Any): Boolean {
            return areItemsTheSame(newItem)
        }
    }
}
