package aeropresscipe.divinelink.aeropress.generaterecipe.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import gr.divinelink.core.util.extensions.inMilliseconds
import java.io.Serializable

@Entity(tableName = "Recipe")
data class DiceDomain(
    var recipe: Recipe,
    var isBrewing: Boolean = false,
    var timeStartedMillis: Long = 0L,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun DiceDomain?.getBrewTimeLeft(): Pair<Long, Long> {
    val timeStartedMillis = this?.timeStartedMillis ?: 0
    val bloomTimeLeft = timeStartedMillis - System.currentTimeMillis() + this?.recipe?.bloomTime.inMilliseconds()
    val brewTimeLeft = timeStartedMillis - System.currentTimeMillis() + this?.recipe?.bloomTime.inMilliseconds() + this?.recipe?.brewTime.inMilliseconds()
    return Pair(bloomTimeLeft, brewTimeLeft)
}

