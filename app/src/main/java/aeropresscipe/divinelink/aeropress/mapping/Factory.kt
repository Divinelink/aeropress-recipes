package aeropresscipe.divinelink.aeropress.mapping

import android.view.ViewGroup

interface Factory<T : MappingModel> {
    fun createViewHolder(parent: ViewGroup): MappingViewHolder<T>
}
