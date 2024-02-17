package hu.delibence.wordlearner.data.databases

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.delibence.wordlearner.data.daos.GroupDao
import hu.delibence.wordlearner.data.daos.SelectedGroupDao
import hu.delibence.wordlearner.data.daos.SettingDao
import hu.delibence.wordlearner.data.daos.WordDao
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.SelectedGroup
import hu.delibence.wordlearner.data.entities.Setting
import hu.delibence.wordlearner.data.entities.Word

@Database(
    entities = [Group::class, Word::class, SelectedGroup::class, Setting::class],
    version = 1,
    exportSchema = true,
    autoMigrations = [
        //AutoMigration(from = 1, to = 2)
    ]
)
abstract class WordLearnerDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun wordDao(): WordDao
    abstract fun selectedGroupDao(): SelectedGroupDao
    abstract fun settingDao(): SettingDao

    companion object {
        @Volatile
        private var Instance: WordLearnerDatabase? = null

        fun getDatabase(context: Context): WordLearnerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, WordLearnerDatabase::class.java, "wordlearner_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}