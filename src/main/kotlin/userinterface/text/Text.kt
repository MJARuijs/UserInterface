package userinterface.text

import graphics.samplers.ClampMode
import graphics.samplers.Sampler
import graphics.shaders.ShaderProgram
import math.Color
import math.vectors.Vector2
import userinterface.UniversalParameters
import userinterface.text.font.Font
import userinterface.text.line.Character.Companion.LINE_HEIGHT
import userinterface.text.line.Line
import userinterface.text.line.Word
import kotlin.math.abs

class Text(val text: String, private val fontSize: Float, private val maxLineWidth: Float = 1.0f, private val font: Font, var color: Color, var translation: Vector2 = Vector2(), var scale: Float = 1.0f) {

    private val sampler = Sampler(0, clamping = ClampMode.EDGE)
    private val spaceWidth = font.getSpaceWidth() * fontSize

    private val textQuad: TextQuad
    
    private var center = Vector2()
    
    private var minX = Float.MAX_VALUE
    private var maxX = -Float.MAX_VALUE
    private var minY = Float.MAX_VALUE
    private var maxY = -Float.MAX_VALUE

    init {
        val lines = createLines()
        textQuad = createTextMesh(lines)
    }
    
    fun xSize() = abs(maxX - minX)
    
    fun ySize() = abs(maxY - minY)
    
    fun draw(shaderProgram: ShaderProgram, aspectRatio: Float) {
        shaderProgram.start()
        shaderProgram.set("textureAtlas", sampler.index)
        shaderProgram.set("translation", translation)
        shaderProgram.set("aspectRatio", aspectRatio)
        shaderProgram.set("scale", scale)
        shaderProgram.set("color", color)
        
        sampler.bind(font.textureAtlas)
        textQuad.draw()
        
        shaderProgram.stop()
    }
    
    fun alignWith(parentTranslation: Vector2, parentScale: Vector2, alignment: TextAlignment) {
        if (alignment.type == AlignmentType.CENTER) {
            translation = parentTranslation / Vector2(UniversalParameters.aspectRatio, 1.0f) - center * scale
        } else {
            translation.x = (parentTranslation.x - parentScale.x) / UniversalParameters.aspectRatio + alignment.offset
            translation.y = parentTranslation.y + (ySize() / 2.0f) * scale
        }
    }
    
    private fun createLines(): List<Line> {
        val lines = ArrayList<Line>()
        
        var currentLine = Line(maxLineWidth)
        var currentWord = Word(fontSize)
        
        for (character in text) {
            if (character == ' ') {
                if (currentLine.addWord(currentWord, spaceWidth)) {
                    currentWord = Word(fontSize)
                } else {
                    lines += currentLine
                    currentLine = Line(maxLineWidth, currentWord)
                    currentWord = Word(fontSize)
                }
            } else {
                currentWord += font.getCharacter(character)
            }
        }
        
        if (currentLine.addWord(currentWord, spaceWidth)) {
            lines += currentLine
        } else {
            lines += currentLine
            lines += Line(maxLineWidth, currentWord)
        }
        
        return lines
    }
    
    private fun createTextMesh(lines: List<Line>): TextQuad {
        var xCursor = 0.0f
        var yCursor = 0.0f
        var vertices = FloatArray(0)
        var texCoords = FloatArray(0)
        
        for (line in lines) {
            for (word in line.words) {
                for (character in word.characters) {
                    val x = xCursor + character.xOffset * fontSize
                    val y = yCursor + character.yOffset * fontSize
    
                    val letterMaxX = x + (character.quadWidth * fontSize)
                    val letterMaxY = y + (character.quadHeight * fontSize)
    
                    if (x < minX) {
                        minX = x
                    }
                    if (y < minY) {
                        minY = y
                    }
                    if (letterMaxX > maxX) {
                        maxX = letterMaxX
                    }
                    if (letterMaxY > maxY) {
                        maxY = letterMaxY
                    }
                    
                    vertices += floatArrayOf(
                        x, -y,
                        x, -letterMaxY,
                        letterMaxX, -letterMaxY,
                        letterMaxX, -letterMaxY,
                        letterMaxX, -y,
                        x, -y
                    )
                    
                    texCoords += floatArrayOf(
                        character.x, character.y,
                        character.x, character.yMaxTexCoord,
                        character.xMaxTexCoord, character.yMaxTexCoord,
                        character.xMaxTexCoord, character.yMaxTexCoord,
                        character.xMaxTexCoord, character.y,
                        character.x, character.y
                    )
    
                    xCursor += character.advance * fontSize
                }
                xCursor += spaceWidth
            }

            xCursor = 0.0f
            yCursor += LINE_HEIGHT * fontSize
        }
        
        center = Vector2((maxX - minX) / 2.0f, (minY - maxY) / 2.0f)

        return TextQuad(vertices, texCoords)
    }

    fun destroy() {
        textQuad.destroy()
    }

}