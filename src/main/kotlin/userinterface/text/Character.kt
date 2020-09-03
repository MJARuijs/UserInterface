package userinterface.text

class Character(val id: Char, val x: Float, val y: Float, val width: Float, val height: Float, val xOffset: Float, val yOffset: Float, val quadWidth: Float, val quadHeight: Float, val advance: Float) {

    val xMaxTexCoord = x + width

    val yMaxTexCoord = y + height

    companion object {
        fun fromLine(line: String, metaData: MetaData, aspectRatio: Float): Character {
            val textureSize = metaData.scaleW

            val lineHeightPixels = metaData.lineHeight - metaData.paddingHeight
            val verticalSize = 0.03f / lineHeightPixels
            val horizontalSize = verticalSize / aspectRatio

            val values = line.split(" ")

            val id = values[0].toInt().toChar()
            val x = (values[1].toFloat() + metaData.paddingLeft) / textureSize
            val y = (values[2].toFloat() + metaData.paddingTop) / textureSize

            val width = values[3].toInt() - metaData.paddingWidth
            val height = values[4].toInt() - metaData.paddingHeight

            val quadWidth = width * horizontalSize
            val quadHeight = height * verticalSize

            val xTextureSize = width / textureSize
            val yTextureSize = height / textureSize

            val xOffset = (values[5].toInt() + metaData.paddingLeft) * horizontalSize
            val yOffset = (values[6].toInt() + metaData.paddingTop) * verticalSize
            val advance = (values[7].toInt() - metaData.paddingWidth) * horizontalSize

            return Character(id, x, y, xTextureSize, yTextureSize, xOffset, yOffset, quadWidth, quadHeight, advance)
        }
    }
}