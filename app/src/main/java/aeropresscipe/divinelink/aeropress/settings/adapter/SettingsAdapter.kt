package aeropresscipe.divinelink.aeropress.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

interface SettingsAdapterDelegate {
    fun onItemClick(item: Any)
}

class SettingsAdapter(
    private val delegate: SettingsAdapterDelegate,
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = compareItems(oldItem, newItem)

        private fun compareItems(oldItem: Any, newItem: Any): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
) {

    companion object {
        const val CategoryTitle = 0
        const val CategoryItem = 1
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item is ) {
            return CategoryTitle
        } else {
            return CategoryItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //TODO also create a layout xml with the name: list_item_(typeX name with LowerCase characters and underscores) e.g list_item_discover
        if (viewType == CategoryTitle) {
            val itemBinding = ListItemSettingsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SettingsItemViewHolder(itemBinding)
        } else {
            val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        /** Don't forget: for every IF there should be an equivelant ELSE */
        //TODO setup the UI based on the item's data.
        if (holder is SettingsItemViewHolder) {
            (holder as? SettingsItemViewHolder)?.updateView(item as? )
        } else {
            (holder as? ViewHolder)?.updateView(item as? )
        }

        holder.itemView?.setOnClickListener {
            delegate?.onItemClick(item = item)
        }
    }

    inner class SettingsItemViewHolder(var binding: ListItemSettingsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            //add init code if needed
        }

        fun updateView(item: ?) {
            //TODO
        }
    }

    inner class ViewHolder(var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            //add init code if needed
        }

        fun updateView(item: ?) {
            //TODO
        }
    }
}

