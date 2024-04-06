package ir.amirreza.module7.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    "well",
)
data class Well(
    @PrimaryKey(autoGenerate = true)
    val wellId: Int = 0,
    val wellTypeId: Int,
    val wellName: String,
    val gasOilDepth: Int,
    val capacity: Int
)
