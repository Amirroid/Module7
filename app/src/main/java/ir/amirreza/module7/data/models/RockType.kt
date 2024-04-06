package ir.amirreza.module7.data.models

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("rock_type")
data class RockType(
    @PrimaryKey(autoGenerate = true)
    val rockTypeId: Int = 0,
    val name: String,
    val color: String
) {
    fun getComposeColor() = Color(android.graphics.Color.parseColor(color))
}
