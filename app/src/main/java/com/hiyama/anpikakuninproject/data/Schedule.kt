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
    fun getAllLectures(): List<Lecture>

    @Insert
    fun insert(vararg lecturesInfo: Lecture)

    @Delete
    fun delete(lectureInfo: Lecture)
}

@Database(entities = [Lecture::class], version = 1)
abstract class ScheduleDB : RoomDatabase(){
    abstract fun lecturesDao(): LecturesDao
}
