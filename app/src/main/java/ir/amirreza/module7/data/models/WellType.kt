package ir.amirreza.module7.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    "well_type",
)
data class WellType(
    @PrimaryKey(autoGenerate = true)
    val wellTypeId: Int = 0,
    val name: String
)
