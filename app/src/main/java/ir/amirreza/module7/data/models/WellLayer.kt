package ir.amirreza.module7.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    "well_layer"
)
data class WellLayer(
    @PrimaryKey(autoGenerate = true) val wellLayerId: Int = 0,
    val wellId: Int,
    val rockTypeId: Int,
    val startPoint: Int,
    val endPoint: Int,
)
