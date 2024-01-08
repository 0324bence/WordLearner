package hu.delibence.wordlearner.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey()
    val id: Int = 0,
    @ColumnInfo(name = "dark_mode")
    val darkMode: Boolean = true,
    @ColumnInfo(name = "use_system_theme")
    val useSystemTheme: Boolean = true,
    @ColumnInfo(name = "use_playset")
    val usePlayset: Boolean = true,
    @ColumnInfo(name = "positive_priority_mod")
    val positivePriorityMod: String = "5",
    @ColumnInfo(name = "negative_priority_mod")
    val negativePriorityMod: String = "1"
)
