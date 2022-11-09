package aeropresscipe.divinelink.aeropress.settings.adapter

import aeropresscipe.divinelink.aeropress.databinding.BaseSettingsProgressItemBinding
import aeropresscipe.divinelink.aeropress.util.mapping.MappingModel
import aeropresscipe.divinelink.aeropress.util.mapping.MappingViewHolder

class SettingProgress {

    class ViewHolder(binding: BaseSettingsProgressItemBinding) : MappingViewHolder.SimpleViewHolder<Item>(binding.root)

    data class Item(val id: Int = 0) : MappingModel {
        override fun areItemsTheSame(newItem: Any): Boolean {
            newItem as Item
            return id == newItem.id
        }

        override fun areContentsTheSame(newItem: Any): Boolean {
            return areItemsTheSame(newItem)
        }
    }
}
