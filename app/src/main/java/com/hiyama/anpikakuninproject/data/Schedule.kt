package com.hiyama.anpikakuninproject.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity
data class Lecture(
    @PrimaryKey(autoGenerate = true) val lectureID: Int = 0,
    @ColumnInfo val lectureName: String,
    @ColumnInfo val dayOfWeek: String,
    @ColumnInfo val lectureTime: Int,
    @ColumnInfo val lectureLocation: String?
)

@Dao
interface LecturesDao {
    @Query("SELECT * FROM lecture " +
            "ORDER BY dayOfWeek ASC, lectureTime ASC")
    fun getAllLectures(): Flow<List<Lecture>>

    @Insert
    suspend fun insert(vararg lecturesInfo: Lecture)

//    @Delete
//    suspend fun delete(lectureInfo: Lecture)

    @Query("DELETE FROM lecture")
    suspend fun deleteAll()

    @Query("DELETE FROM lecture where dayOfWeek = :dayOfWeek and lectureTime = :lectureTime")
    suspend fun deleteClass(dayOfWeek: String, lectureTime: Int)

}

@Database(entities = [Lecture::class], version = 1)
abstract class ScheduleDB : RoomDatabase(){
    abstract fun lecturesDao(): LecturesDao
}
