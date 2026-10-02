package com.paliy.composenotes.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "content",
    primaryKeys = ["noteId", "order"],
    foreignKeys = [
        ForeignKey(
            entity = NoteDbModel::class,
            parentColumns = ["id"],
            childColumns =  ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ContentItemDbModel(
    val noteId: Int,
    val contentType: ContentType,
    val content: String,
    val order: Int
)

enum class ContentType{

    TEXT,IMAGE
}




/*@Serializable
sealed interface ContentItemDbModel {

    @Serializable
    data class Text(val content: String): ContentItemDbModel

    @Serializable
    data class Image(val uri: String): ContentItemDbModel
}*/