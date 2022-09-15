package aeropresscipe.divinelink.aeropress.settings.models

import aeropresscipe.divinelink.aeropress.databinding.DslAsyncSwitchPreferenceItemBinding
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.PreferenceModel
import aeropresscipe.divinelink.aeropress.settings.PreferenceViewHolder
import aeropresscipe.divinelink.aeropress.util.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.util.mapping.MappingAdapter
import android.view.LayoutInflater
import android.view.ViewGroup

object AsyncSwitch {

    fun register(adapter: MappingAdapter) {
        adapter.registerFactory(LayoutFactory({ inflater: LayoutInflater, parent: ViewGroup -> ViewHolder(DslAsyncSwitchPreferenceItemBinding.inflate(inflater, parent, false)) }))
    }

    class Model(
        override val title: DSLSettingsText,
        override val isEnabled: Boolean,
        val isChecked: Boolean,
        val isProcessing: Boolean,
        val onClick: () -> Unit
    ) : PreferenceModel<Model>() {
        override fun areContentsTheSame(newItem: Any): Boolean {
            newItem as Model
            return super.areContentsTheSame(newItem) && isChecked == newItem.isChecked && isProcessing == newItem.isProcessing
        }
    }

    class ViewHolder(binding: DslAsyncSwitchPreferenceItemBinding) : PreferenceViewHolder<Model>(binding) {
        private val switchWidget = binding.switchWidget
        private val switcher = binding.switcher

        override fun bind(model: Model) {
            super.bind(model)
            switchWidget.isEnabled = model.isEnabled
            switchWidget.isChecked = model.isChecked
            binding.root.isEnabled = !model.isProcessing && model.isEnabled
            switcher.displayedChild = if (model.isProcessing) 1 else 0

            binding.root.setOnClickListener {
                if (!model.isProcessing) {
                    binding.root.isEnabled = false
                    switcher.displayedChild = 1
                    model.onClick()
                }
            }
        }
    }
}