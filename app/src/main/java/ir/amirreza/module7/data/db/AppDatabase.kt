package ir.amirreza.module7.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ir.amirreza.module7.data.models.RockType
import ir.amirreza.module7.data.models.Well
import ir.amirreza.module7.data.models.WellLayer
import ir.amirreza.module7.data.models.WellType

@Database(
    version = 1,
    entities = [
        Well::class,
        WellLayer::class,
        WellType::class,
        RockType::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: AppDao

    companion object {
        var INSTANT: AppDatabase? = null
        fun init(context: Context) {
            INSTANT = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "my_db"
            ).allowMainThreadQueries().addCallback(DatabaseCallback())
                .fallbackToDestructiveMigrationFrom().build()
        }
    }

    class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            addData(db)
            super.onCreate(db)
        }

        private fun addData(db: SupportSQLiteDatabase) {
            db.execSQL(
                "INSERT INTO `well_type` VALUES (1,'Well'),(2,'Section');"
            )
            db.execSQL(
                "INSERT INTO `well` VALUES (1,1,'Yolka #12 ',4500,980000000),(2,1,'Kazan  #12',4230,1080000000),(3,1,'Kazan  #13',4830,780000000);"
            )
            db.execSQL(
                "INSERT INTO `rock_type` VALUES (1,'Argillite','#E52B50'),(2,'Breccia','#FFBF00'),(3,'Chalk','#9966CC'),(4,'Chert','#FBCEB1'),(5,'Coal','#7FFFD4'),(6,'Conglomerate','#007FFF'),(7,'Dolomite','#0095B6'),(8,'Limestone','#800020'),(9,'Marl','#DE3163'),(10,'Mudstone','#F7E7CE'),(11,'Sandstone','#7FFF00'),(12,'Shale','#C8A2C8'),(13,'Tufa','#BFFF00'),(14,'Wackestone','#FFFF00');"
            )
            db.execSQL(
                "INSERT INTO `well_layer` VALUES (1,1,10,0,800),(2,1,3,800,1430),(3,1,2,1430,1982),(4,1,11,1982,2648),(5,1,6,2648,3312),(6,1,7,3312,3839),(7,1,1,3839,4450),(8,2,9,0,755),(9,2,11,755,1523),(10,2,3,1523,2280),(11,2,6,2280,2916),(12,2,10,2916,3727),(13,2,1,3727,4230),(14,3,10,0,808),(15,3,5,808,1605),(16,3,1,1605,2129),(17,3,6,2129,2770),(18,3,9,2770,3738),(19,3,8,3738,4670),(20,3,4,4670,4830);"
            )
        }
    }
}