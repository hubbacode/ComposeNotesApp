package com.paliy.composenotes.presentation.ui.theme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


object CustomIcons {
    val AddPhoto: ImageVector
        get() {
            if (_MaterialIconsAddPhotoAlternate != null) return _MaterialIconsAddPhotoAlternate!!

            _MaterialIconsAddPhotoAlternate = ImageVector.Builder(
                name = "add_photo_alternate",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Transparent)
                ) {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(24f)
                    verticalLineToRelative(24f)
                    horizontalLineTo(0f)
                    verticalLineTo(0f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black)
                ) {
                    moveTo(18f, 20f)
                    horizontalLineTo(4f)
                    verticalLineTo(6f)
                    horizontalLineToRelative(9f)
                    verticalLineTo(4f)
                    horizontalLineTo(4f)
                    curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                    verticalLineToRelative(14f)
                    curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
                    horizontalLineToRelative(14f)
                    curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
                    verticalLineToRelative(-9f)
                    horizontalLineToRelative(-2f)
                    verticalLineToRelative(9f)
                    close()
                    moveToRelative(-7.79f, -3.17f)
                    lineToRelative(-1.96f, -2.36f)
                    lineTo(5.5f, 18f)
                    horizontalLineToRelative(11f)
                    lineToRelative(-3.54f, -4.71f)
                    close()
                    moveTo(20f, 4f)
                    verticalLineTo(1f)
                    horizontalLineToRelative(-2f)
                    verticalLineToRelative(3f)
                    horizontalLineToRelative(-3f)
                    curveToRelative(0.01f, 0.01f, 0f, 2f, 0f, 2f)
                    horizontalLineToRelative(3f)
                    verticalLineToRelative(2.99f)
                    curveToRelative(0.01f, 0.01f, 2f, 0f, 2f, 0f)
                    verticalLineTo(6f)
                    horizontalLineToRelative(3f)
                    verticalLineTo(4f)
                    horizontalLineToRelative(-3f)
                    close()
                }
            }.build()

            return _MaterialIconsAddPhotoAlternate!!
        }

    private var _MaterialIconsAddPhotoAlternate: ImageVector? = null
}