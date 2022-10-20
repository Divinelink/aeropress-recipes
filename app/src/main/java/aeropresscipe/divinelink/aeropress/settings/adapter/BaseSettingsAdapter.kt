package aeropresscipe.divinelink.aeropress.settings.adapter

import aeropresscipe.divinelink.aeropress.databinding.BaseSettingsHeaderItemBinding
import aeropresscipe.divinelink.aeropress.databinding.BaseSettingsProgressItemBinding
import aeropresscipe.divinelink.aeropress.databinding.SingleSelectItemBinding
import aeropresscipe.divinelink.aeropress.util.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.util.mapping.MappingAdapter
import android.view.LayoutInflater
import android.view.ViewGroup

class BaseSettingsAdapter : MappingAdapter() {

    override fun register() {
        registerFactory(LayoutFactory({ i, r -> SettingHeader.ViewHolder(BaseSettingsHeaderItemBinding.inflate(i, r, false)) }))
        registerFactory(LayoutFactory({ i, r -> SettingProgress.ViewHolder(BaseSettingsProgressItemBinding.inflate(i, r, false)) }))
    }

    fun configureSingleSelect(onSelectionChanged: onSelectionChanged) {
        registerFactory(
            LayoutFactory({ layoutInflater: LayoutInflater, root: ViewGroup ->
                SingleSelectSetting.ViewHolder(
                    binding = SingleSelectItemBinding.inflate(layoutInflater, root, false),
                    onSelectionChanged = onSelectionChanged
                )
            })
        )
    }

//    fun configureCustomizableSingleSelect(selectionListener: CustomizableSingleSelectSetting.CustomizableSingleSelectionListener) {
//        registerFactory(CustomizableSingleSelectSetting.Item::class.java,
//            LayoutFactory({ v -> ViewHolder(v, selectionListener) }, R.layout.customizable_single_select_item))
//    }
}