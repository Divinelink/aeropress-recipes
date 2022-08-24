package aeropresscipe.divinelink.aeropress.mapping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

class LayoutFactory<T : MappingModel>(
    private val creator: (View) -> MappingViewHolder<T>,
    @LayoutRes private val layout: Int) : Factory<T> {
    override fun createViewHolder(parent: ViewGroup): MappingViewHolder<T> {
        return creator(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }
}
