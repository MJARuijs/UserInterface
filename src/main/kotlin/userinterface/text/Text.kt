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
import util.FloatUtils
import kotlin.math.abs

class Text(var text: String, private val fontSize: Float, private val maxLineWidth: Float = 1.0f, private val font: Font, var color: Color, var translation: Vector2 = Vector2(), var scale: Float = 1.0f) {

    private val sampler = Sampler(0, clamping = ClampMode.EDGE)
    private val spaceWidth = font.getSpaceWidth() * fontSize

    private var textMesh: TextMesh
    
    private var center = Vector2()
    
    private var minX = Float.MAX_VALUE
    private var maxX = -Float.MAX_VALUE
    private var minY = Float.MAX_VALUE
    private var maxY = -Float.MAX_VALUE
    
    private var characterVertices = arrayListOf<CharacterData>()
    private var characterTextureCoordinates = arrayListOf<FloatArray>()

    init {
        println("CREATING $text")
        val lines = createLines()
        textMesh = createTextMesh(lines)
    }
    
    fun xSize() = abs(maxX - minX)
    
    fun ySize() = abs(maxY - minY)
    
    fun length() = text.length
    
    fun isBlank() = text.isBlank()
    
    fun getPoints(): FloatArray {
        var points = FloatArray(0)
        
        for (arr in characterVertices) {
            for (i in arr.vertices.indices step 2) {
                val x = arr.vertices[i]
                val y = arr.vertices[i + 1]
        
                val point = Vector2(x, y)
                val scaledPoint = point * scale + translation
                points += scaledPoint.toArray()
            }
        }
        return points
    }
    
    fun addCharacter(i: Int, character: Char) {
        val stringBuilder = StringBuilder()
        if (i == 0) {
            stringBuilder.append(character)
            stringBuilder.append(text)
        } else {
            for (j in 0 until i) {
                stringBuilder.append(text[j])
            }
            stringBuilder.append(character)
            for (j in i until text.length) {
                stringBuilder.append(text[j])
            }
        }
        
        text = stringBuilder.toString()
        updateMesh()
    }
    
    fun removeCharacter(i: Int) {
        text = text.removeRange(i, i + 1)
        updateMesh()
    }
    
    fun draw(shaderProgram: ShaderProgram, aspectRatio: Float) {
        shaderProgram.start()
        shaderProgram.set("textureAtlas", sampler.index)
        shaderProgram.set("translation", translation)
        shaderProgram.set("aspectRatio", aspectRatio)
        shaderProgram.set("scale", scale)
        shaderProgram.set("color", color)
        
        sampler.bind(font.textureAtlas)
        textMesh.draw()
        
        shaderProgram.stop()
    }

    fun getCharacterInFrontOfCursor(xPosition: Float, aspectRatio: Float): Int {
        val firstCharacterDimensions = getCharacterXBounds(0)
        if (xPosition <= firstCharacterDimensions.first * aspectRatio) {
            return 0
        }
        
        val lastCharacterDimensions = getCharacterXBounds(text.length -  1)
        if (xPosition >= lastCharacterDimensions.second * aspectRatio) {
            return length()
        }

        for ((i, character) in characterVertices.withIndex()) {
            if (i != length() - 1) {
                val characterDimensions = getCharacterXBounds(character)
                val nextCharacterDimensions = getCharacterXBounds(characterVertices[i + 1])
                
                if (characterDimensions.second < nextCharacterDimensions.first) {
                    if (xPosition > characterDimensions.second * aspectRatio && xPosition < nextCharacterDimensions.first * aspectRatio) {
                        return i + 1
                    }
                } else if (characterDimensions.second > nextCharacterDimensions.first) {
                    if (xPosition < characterDimensions.second * aspectRatio && xPosition > nextCharacterDimensions.first * aspectRatio) {
                        return i + 1
                    }
                }
            }
        }
        return -1
    }
    
    fun getSelectedCharacter(xPosition: Float): Int {
        for ((i, character) in characterVertices.withIndex()) {
            val characterDimensions = getCharacterXBounds(character)
            if (xPosition > characterDimensions.first && xPosition < characterDimensions.second) {
                return i
            }
        }
        return -1
    }
    
    fun getCharacterXBounds(i: Int): Pair<Float, Float> {
        return getCharacterXBounds(characterVertices[i])
    }
    
    private fun getCharacterXBounds(characterData: CharacterData): Pair<Float, Float> {
        var minX = Float.MAX_VALUE
        var maxX = -Float.MAX_VALUE
        
        if (characterData.character == ' ') {
            return Pair(characterData.vertices[0] * scale + translation.x, characterData.vertices[1] * scale + translation.x)
        }
        
        for (i in characterData.vertices.indices step 2) {
            val x = characterData.vertices[i] * scale + translation.x
            
            if (x > maxX) {
                maxX = x
            }
            if (x < minX) {
                minX = x
            }
        }
        return Pair(FloatUtils.roundToDecimal(minX, 5), FloatUtils.roundToDecimal(maxX, 5))
    }
    
    fun alignWith(parentTranslation: Vector2, parentScale: Vector2, alignment: TextAlignment) {
        if (alignment.type == AlignmentType.CENTER) {
            translation = parentTranslation / Vector2(UniversalParameters.aspectRatio, 1.0f) - center * scale
        } else {
            if (length() == 0) {
                translation.x = (parentTranslation.x - parentScale.x) / UniversalParameters.aspectRatio + alignment.offset
                translation.y = parentTranslation.y + (ySize() / 2.0f) * scale
            } else {
                val firstLetterStart = getCharacterXBounds(0).first
                translation.x = (parentTranslation.x - parentScale.x) / UniversalParameters.aspectRatio - firstLetterStart * 2
                translation.y = parentTranslation.y + (ySize() / 2.0f) * scale
            }
        }
    }
    
    private fun updateMesh() {
        characterVertices.clear()
        characterTextureCoordinates.clear()
        
        val lines = createLines()
        textMesh = createTextMesh(lines)
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
    
    private fun createTextMesh(lines: List<Line>): TextMesh {
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
                    
                    val charVertices = floatArrayOf(
                        x, -y,
                        x, -letterMaxY,
                        letterMaxX, -letterMaxY,
                        letterMaxX, -letterMaxY,
                        letterMaxX, -y,
                        x, -y
                    )
                    
                    val charTextureCoordinates = floatArrayOf(
                        character.x, character.y,
                        character.x, character.yMaxTexCoord,
                        character.xMaxTexCoord, character.yMaxTexCoord,
                        character.xMaxTexCoord, character.yMaxTexCoord,
                        character.xMaxTexCoord, character.y,
                        character.x, character.y
                    )
                    
                    vertices += charVertices
                    texCoords += charTextureCoordinates
                    
                    characterVertices.add(CharacterData(character.id, charVertices))
                    characterTextureCoordinates.add(charTextureCoordinates)
    
                    xCursor += character.advance * fontSize
                }
                characterVertices.add(CharacterData(' ', floatArrayOf(xCursor, xCursor + spaceWidth)))
                xCursor += spaceWidth
            }

            xCursor = 0.0f
            yCursor += LINE_HEIGHT * fontSize
        }
        
        center = Vector2((maxX - minX) / 2.0f, (minY - maxY) / 2.0f)

        return TextMesh(vertices, texCoords)
    }

    fun destroy() {
        textMesh.destroy()
    }

}