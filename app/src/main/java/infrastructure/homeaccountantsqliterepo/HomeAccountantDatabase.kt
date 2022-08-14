package infrastructure.homeaccountantsqliterepo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [
    HomeDbModel::class,
    HomeDeviceDbModel::class,
    OneHomeManyDevicesDbModel::class,
    HomeDeviceValueDbmodel::class,
    OneDeviceManyValuesDbModel::class,
    ], version = 1)
@TypeConverters(HomeAccountantTypeConverters::class)
internal abstract class HomeAccountantDatabase : RoomDatabase() {

    abstract fun homeDao(): HomeAccountantDao

}