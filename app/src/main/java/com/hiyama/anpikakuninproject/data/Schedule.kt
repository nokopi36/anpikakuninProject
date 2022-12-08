package com.hiyama.anpikakuninproject.data

import androidx.room.*

@Entity
data class Lecture(
    @PrimaryKey val lectureName: String,
    @ColumnInfo val dayOfWeek: String,
    @ColumnInfo val lectureTime: String,
    @ColumnInfo val lectureLocation: String?
)

@Dao
interface LecturesDao {
    @Query("SELECT * FROM lecture")
    suspend fun getAllLectures(): List<Lecture>

    @Insert
    suspend fun insert(vararg lecturesInfo: Lecture)

    @Delete
    suspend fun delete(lectureInfo: Lecture)
}

@Database(entities = [Lecture::class], version = 1)
abstract class ScheduleDB : RoomDatabase(){
    abstract fun lecturesDao(): LecturesDao
}
