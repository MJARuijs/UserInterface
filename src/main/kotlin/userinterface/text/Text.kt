package userinterface.text

import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.samplers.ClampMode
import graphics.samplers.Sampler
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.text.font.Font

class Text(val text: String, fontSize: Float, private val font: Font) {

    private val sampler = Sampler(0, clamping = ClampMode.EDGE)

    private val textQuad: TextQuad

    init {
        var cursor = 0.0f
        var vertices = FloatArray(0)
        var texCoords = FloatArray(0)

        for (letter in text) {
            val char = font.characters.find { character -> character.id == letter } ?: throw IllegalArgumentException("Found unknown character: $letter")

            val x = cursor + char.xOffset * fontSize
            val y = char.yOffset * fontSize

            val maxX = x + (char.quadWidth * fontSize)
            val maxY = y + (char.quadHeight * fontSize)

            val scaledX = (2.0f * x) - 1.0f
            val scaledY = (-2.0f * y) + 1.0f

            val scaledMaxX = (2.0f * maxX) - 1.0f
            val scaledMaxY = (-2.0f * maxY) + 1.0f

            vertices += floatArrayOf(
                scaledX, scaledY,
                scaledX, scaledMaxY,
                scaledMaxX, scaledMaxY,
                scaledMaxX, scaledMaxY,
                scaledMaxX, scaledY,
                scaledX, scaledY
            )

            texCoords += floatArrayOf(
                char.x, char.y,
                char.x, char.yMaxTexCoord,
                char.xMaxTexCoord, char.yMaxTexCoord,
                char.xMaxTexCoord, char.yMaxTexCoord,
                char.xMaxTexCoord, char.y,
                char.x, char.y
            )

            cursor += char.advance * fontSize
        }

        textQuad = TextQuad(vertices, texCoords)
    }

    fun render(shaderProgram: ShaderProgram) {
        shaderProgram.start()
        shaderProgram.set("textureAtlas", sampler.index)
        shaderProgram.set("scale", Vector2(1.0f, 1.0f))
        shaderProgram.set("translation", Vector2(0.0f, 0.0f))

        GraphicsContext.enable(GraphicsOption.ALPHA_BLENDING)
        GraphicsContext.disable(GraphicsOption.DEPTH_TESTING)

        sampler.bind(font.textureAtlas)
        textQuad.draw()

        shaderProgram.stop()
        GraphicsContext.disable(GraphicsOption.ALPHA_BLENDING)
        GraphicsContext.enable(GraphicsOption.DEPTH_TESTING)
    }

    fun destroy() {
        textQuad.destroy()
    }

}