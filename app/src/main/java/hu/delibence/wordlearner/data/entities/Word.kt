package hu.delibence.wordlearner.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "word1")
    val word1: String,

    @ColumnInfo(name = "word2")
    val word2: String,

    @ColumnInfo(name = "priority")
    val priority: Int = 100,

    @ColumnInfo(name = "flagged")
    val flagged: Boolean = false,

    @ColumnInfo(name = "inplay")
    val inplay: Boolean = true,

    @ColumnInfo(name = "group")
    val group: Int? = null
)
