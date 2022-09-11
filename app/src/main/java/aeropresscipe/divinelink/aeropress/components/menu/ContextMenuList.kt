package aeropresscipe.divinelink.aeropress.components.menu

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.ContextMenuItemBinding
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Handles the setup and display of actions shown in a context menu.
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ContextMenuAdapter(
    private val onItemClick: () -> Unit
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = compareItems(oldItem, newItem)

        private fun compareItems(oldItem: Any, newItem: Any): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
) {

    companion object {
        const val Type_Item = 0
    }

    override fun getItemViewType(position: Int): Int {
        return Type_Item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ContextMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemBinding, onItemClick)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        (holder as? ItemViewHolder)?.bind(item as ContextMenuList.DisplayItem)

        holder.itemView.setOnClickListener {
            onItemClick
        }
    }

    private class ItemViewHolder(
        var binding: ContextMenuItemBinding,
        private val onItemClick: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        val icon: ImageView = binding.contextMenuItemIcon
        val title: TextView = binding.contextMenuItemTitle

        fun bind(model: ContextMenuList.DisplayItem) {
            icon.setImageResource(model.item.iconRes)
            title.text = model.item.title
            binding.root.setOnClickListener {
                model.item.action.run()
                onItemClick()
            }

            val tintColor = ContextCompat.getColor(binding.root.context, model.item.tintRes)
            icon.setColorFilter(tintColor)
            title.setTextColor(tintColor)

            when (model.displayType) {
                ContextMenuList.DisplayType.TOP -> binding.root.setBackgroundResource(R.drawable.context_menu_item_background_top)
                ContextMenuList.DisplayType.BOTTOM -> binding.root.setBackgroundResource(R.drawable.context_menu_item_background_bottom)
                ContextMenuList.DisplayType.MIDDLE -> binding.root.setBackgroundResource(R.drawable.context_menu_item_background_middle)
                ContextMenuList.DisplayType.ONLY -> binding.root.setBackgroundResource(R.drawable.context_menu_item_background_only)
            }
        }
    }
}


class ContextMenuList(recyclerView: RecyclerView, onItemClick: () -> Unit) {

//    private val mappingAdapter = MappingAdapter().apply {
//        registerFactory(DisplayItem::class.java, LayoutFactory({ ItemViewHolder(it, onItemClick) }, R.layout.signal_context_menu_item))
//    }

    private val contextMenuAdapter = ContextMenuAdapter(onItemClick = onItemClick)

    init {
        recyclerView.apply {
            adapter = contextMenuAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
        }
    }

    fun setItems(items: List<ActionItem>) {
        contextMenuAdapter.submitList(items.toAdapterItems())
    }

    private fun List<ActionItem>.toAdapterItems(): List<DisplayItem> {
        return this.mapIndexed { index, item ->
            val displayType: DisplayType = when {
                this.size == 1 -> DisplayType.ONLY
                index == 0 -> DisplayType.TOP
                index == this.size - 1 -> DisplayType.BOTTOM
                else -> DisplayType.MIDDLE
            }

            DisplayItem(item, displayType)
        }
    }

    data class DisplayItem(
        val item: ActionItem,
        val displayType: DisplayType
    )

    enum class DisplayType {
        TOP, BOTTOM, MIDDLE, ONLY
    }


}
