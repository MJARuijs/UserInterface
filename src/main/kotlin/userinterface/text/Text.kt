package userinterface.text

import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.samplers.ClampMode
import graphics.samplers.Sampler
import graphics.shaders.ShaderProgram
import math.Color
import math.vectors.Vector2
import userinterface.text.font.Font
import userinterface.text.line.Character.Companion.LINE_HEIGHT
import userinterface.text.line.Line
import userinterface.text.line.Word

class Text(val text: String, private val fontSize: Float, private val maxLineWidth: Float = 1.0f, private val font: Font, var color: Color, private val aspectRatio: Float, var translation: Vector2 = Vector2(), var scale: Float = 1.0f) {

    private val sampler = Sampler(0, clamping = ClampMode.EDGE)
    private val spaceWidth = font.getSpaceWidth() * fontSize

    private val textQuad: TextQuad

    init {
        val lines = createLines()
        textQuad = createTextMesh(lines)
    }
    
    fun draw(shaderProgram: ShaderProgram, aspectRatio: Float) {
        shaderProgram.start()
        shaderProgram.set("textureAtlas", sampler.index)
        shaderProgram.set("translation", translation)
        shaderProgram.set("aspectRatio", aspectRatio)
        shaderProgram.set("scale", scale)
        shaderProgram.set("color", color)
        
        GraphicsContext.enable(GraphicsOption.ALPHA_BLENDING)
        GraphicsContext.disable(GraphicsOption.DEPTH_TESTING)
        
        sampler.bind(font.textureAtlas)
        textQuad.draw()
        
        shaderProgram.stop()
        GraphicsContext.disable(GraphicsOption.ALPHA_BLENDING)
        GraphicsContext.enable(GraphicsOption.DEPTH_TESTING)
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
                    val x = (xCursor + character.xOffset * fontSize) * aspectRatio
                    val y = (yCursor + character.yOffset * fontSize) * aspectRatio
    
                    val letterMaxX = x + (character.quadWidth * fontSize) * aspectRatio
                    val letterMaxY = y + (character.quadHeight * fontSize) * aspectRatio
    
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
        
        var minX = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var minY = Float.MAX_VALUE
        var maxY = Float.MIN_VALUE
        
        for (i in vertices.indices step 2) {
            val x = vertices[i]
            val y = vertices[i + 1]
            
            if (x < minX) {
                minX = x
            }
            if (x > maxX) {
                maxX = x
            }
            if (y < minY) {
                minY = y
            }
            if (y > maxY) {
                maxY = y
            }
        }
        val xDifference = (maxX - minX)
        val yDifference = (minY - maxY)
//        println("$minX, $maxX, $minY, $maxY, $xDifference, $yDifference")
    
        var newVertices = FloatArray(0)
    
        for (i in vertices.indices step 2) {
            val x = vertices[i]
            val y = vertices[i + 1]
            
            newVertices += x - xDifference / 2.0f
            newVertices += y - yDifference / 2.0f
        }
        minX = Float.MAX_VALUE
        maxX = Float.MIN_VALUE
        minY = Float.MAX_VALUE
        maxY = Float.MIN_VALUE
        for (i in newVertices.indices step 2) {
            val x = newVertices[i]
            val y = newVertices[i + 1]
        
            if (x < minX) {
                minX = x
            }
            if (x > maxX) {
                maxX = x
            }
            if (y < minY) {
                minY = y
            }
            if (y > maxY) {
                maxY = y
            }
        }
    
//        println("$minX, $maxX, $minY, $maxY")
    
        return TextQuad(newVertices, texCoords)
    }

    fun destroy() {
        textQuad.destroy()
    }

}