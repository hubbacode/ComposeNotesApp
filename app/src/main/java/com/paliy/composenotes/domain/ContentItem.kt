package com.paliy.composenotes.domain

sealed interface ContentItem {

    data class Text(val content: String): ContentItem

    data class Image(val uri: String): ContentItem
}