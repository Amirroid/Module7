package ir.amirreza.module7.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class WellData(
    @Embedded val well: Well,
    @Relation(
        entity = WellLayer::class,
        parentColumn = "wellId",
        entityColumn = "wellId"
    )
    val layers: List<WellLayersWithRockType>,
    @Relation(
        entity = WellType::class,
        entityColumn = "wellTypeId",
        parentColumn = "wellTypeId"
    )
    val type: WellType
)


data class WellLayersWithRockType(
    @Embedded val layer: WellLayer,
    @Relation(
        entity = RockType::class,
        parentColumn = "rockTypeId",
        entityColumn = "rockTypeId",
    )
    val rockType: RockType,
)