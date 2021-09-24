package userinterface.text.line

import userinterface.text.MetaData

class Character(val id: Char, val x: Float, val y: Float, val width: Float, val height: Float, val xOffset: Float, val yOffset: Float, val quadWidth: Float, val quadHeight: Float, val advance: Float) {

    val xMaxTexCoord = x + width

    val yMaxTexCoord = y + height

    companion object {
        private const val DESIRED_PADDING = 8.0f
        const val LINE_HEIGHT = 0.03f
    
        fun fromLine(line: String, metaData: MetaData, aspectRatio: Float): Character {
            val textureSize = metaData.scaleW

            val lineHeightPixels = metaData.lineHeight - metaData.paddingHeight
            val verticalSize = LINE_HEIGHT / lineHeightPixels
            val horizontalSize = verticalSize / aspectRatio
            val values = line.split(" ")

            val id = values[0].toInt().toChar()
//            val x = (values[1].toFloat() ) / textureSize
//            val y = (values[2].toFloat() ) / textureSize
//
//            val width = values[3].toFloat()
//            val height = values[4].toFloat()
    
            val x = (values[1].toFloat() + metaData.paddingLeft - DESIRED_PADDING) / textureSize
            val y = (values[2].toFloat() + metaData.paddingTop - DESIRED_PADDING) / textureSize

            val width = values[3].toFloat() - (metaData.paddingWidth - 2.0f * DESIRED_PADDING)
            val height = values[4].toFloat() - (metaData.paddingHeight - 2.0f * DESIRED_PADDING)
            
            val quadWidth = width * horizontalSize
            val quadHeight = height * verticalSize

            val xTextureSize = width / textureSize
            val yTextureSize = height / textureSize

            val xOffset = (values[5].toInt()) * horizontalSize
            val yOffset = (values[6].toInt()) * verticalSize
            val advance = (values[7].toInt() - metaData.paddingWidth) * horizontalSize

            return Character(id, x, y, xTextureSize, yTextureSize, xOffset, yOffset, quadWidth, quadHeight, advance)
        }
    }
}