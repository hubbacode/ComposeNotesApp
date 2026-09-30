package com.paliy.composenotes.domain

data class Note(
    val id: Int,
    val title : String,
    val content : List<ContentItem>,
    val updatedAt: Long,
    val isPinned: Boolean
)
