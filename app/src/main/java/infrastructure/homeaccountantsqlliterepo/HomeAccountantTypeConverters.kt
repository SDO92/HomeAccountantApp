package infrastructure.homeaccountantsqlliterepo

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

private fun Date.toSimpleString(): String {
    return dateFormat.format(this)
}

internal class HomeAccountantTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): String? = date?.toSimpleString()

    @TypeConverter
    fun toDate(dateString: String?): Date? = dateString?.let {
            dateFormat.parse(it)
        }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? = UUID.fromString(uuid)

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

}