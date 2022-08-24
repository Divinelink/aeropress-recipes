package aeropresscipe.divinelink.aeropress.mapping

interface MappingModel {
    fun areItemsTheSame(newItem: Any): Boolean
    fun areContentsTheSame(newItem: Any): Boolean
    fun getChangePayload(newItem: Any): Any? {
        return null
    }
}
