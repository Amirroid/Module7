package ir.amirreza.module7.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ir.amirreza.module7.data.models.RockType
import ir.amirreza.module7.data.models.Well
import ir.amirreza.module7.data.models.WellData
import ir.amirreza.module7.data.models.WellLayer
import ir.amirreza.module7.data.models.WellType
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM well WHERE well.wellId = :wellId")
    @Transaction
    fun getWell(wellId: Int): Flow<WellData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWellLayer(obj: WellLayer): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWellLayers(obj: List<WellLayer>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWell(obj: Well): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWellType(obj: WellType): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRockType(obj: RockType): Long

    @Query("SELECT * FROM well")
    fun getWells(): Flow<List<Well>>

    @Query("SELECT * FROM rock_type")
    fun getRockTypes(): Flow<List<RockType>>

    @Delete
    fun deleteLayer(obj : List<WellLayer>)
}