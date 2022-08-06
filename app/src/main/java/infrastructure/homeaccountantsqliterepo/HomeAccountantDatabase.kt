package infrastructure.homeaccountantsqliterepo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [HomeDbModel::class, HomeDeviceDbModel::class, HomeDeviceValueDbModel::class], version = 1)
@TypeConverters(HomeAccountantTypeConverters::class)
internal abstract class HomeAccountantDatabase : RoomDatabase() {

    abstract fun homeDao(): HomeAccountantDao

}