@file:Suppress("MaxLineLength")

package aeropresscipe.divinelink.aeropress.settings.models

import aeropresscipe.divinelink.aeropress.settings.DSLSettingsIcon
import aeropresscipe.divinelink.aeropress.settings.DSLSettingsText
import aeropresscipe.divinelink.aeropress.settings.PreferenceModel
import aeropresscipe.divinelink.aeropress.util.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.util.mapping.MappingAdapter
import aeropresscipe.divinelink.aeropress.util.mapping.MappingViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.divinelink.aeropress.recipes.R
import com.divinelink.aeropress.recipes.databinding.DslButtonPrimaryBinding
import com.divinelink.aeropress.recipes.databinding.DslButtonSecondaryBinding
import com.divinelink.aeropress.recipes.databinding.DslButtonTonalBinding
import com.google.android.material.button.MaterialButton

object Button {

  fun register(mappingAdapter: MappingAdapter) {
    mappingAdapter.registerFactory<Model.Primary>(
      LayoutFactory(
        { inflater: LayoutInflater, parent: ViewGroup ->
          ViewHolder(
            DslButtonPrimaryBinding.inflate(
              inflater,
              parent,
              false,
            ),
          )
        },
      ),
    )
    mappingAdapter.registerFactory<Model.Tonal>(
      LayoutFactory(
        { inflater: LayoutInflater, parent: ViewGroup ->
          ViewHolder(
            DslButtonTonalBinding.inflate(
              inflater,
              parent,
              false,
            ),
          )
        },
      ),
    )
    mappingAdapter.registerFactory<Model.SecondaryNoOutline>(
      LayoutFactory(
        { inflater: LayoutInflater, parent: ViewGroup ->
          ViewHolder(
            DslButtonSecondaryBinding.inflate(
              inflater,
              parent,
              false,
            ),
          )
        },
      ),
    )
  }

  sealed class Model<T : Model<T>>(
    title: DSLSettingsText?,
    icon: DSLSettingsIcon?,
    isEnabled: Boolean,
    val onClick: () -> Unit,
  ) : PreferenceModel<T>(
    title = title,
    icon = icon,
    isEnabled = isEnabled,
  ) {
    class Primary(
      title: DSLSettingsText?,
      icon: DSLSettingsIcon?,
      isEnabled: Boolean,
      onClick: () -> Unit,
    ) : Model<Primary>(title, icon, isEnabled, onClick)

    class Tonal(
      title: DSLSettingsText?,
      icon: DSLSettingsIcon?,
      isEnabled: Boolean,
      onClick: () -> Unit,
    ) : Model<Tonal>(title, icon, isEnabled, onClick)

    class SecondaryNoOutline(
      title: DSLSettingsText?,
      icon: DSLSettingsIcon?,
      isEnabled: Boolean,
      onClick: () -> Unit,
    ) : Model<SecondaryNoOutline>(title, icon, isEnabled, onClick)
  }

  class ViewHolder<T : Model<T>>(binding: ViewBinding) : MappingViewHolder<T>(binding.root) {
    private val button: MaterialButton = itemView.findViewById(R.id.button)

    override fun bind(model: T) {
      button.text = model.title?.resolve(context)
      button.setOnClickListener {
        model.onClick()
      }
      button.icon = model.icon?.resolve(context)
      button.isEnabled = model.isEnabled
    }
  }
}
