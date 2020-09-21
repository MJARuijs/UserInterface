package userinterface.svg

import math.vectors.Vector2
import resources.Loader
import util.File

class SVGLoader : Loader<SVGFile> {
    
    override fun load(path: String): SVGFile {
        val file = File(path)
        val content = file.getContent()
    
        var vertices = FloatArray(0)
        
        val height = getValueOfAttribute("height", content)
        val width = getValueOfAttribute("width", content)
        
        val pathValues = getValueOfAttribute("path", content)
        var formattedPathValues = ""
        for (char in pathValues) {
            when {
                char == '-' -> {
                    formattedPathValues += " "
                    formattedPathValues += char
                }
                char == ',' -> {
                    formattedPathValues += " "
                }
                char.isLetter() -> {
                    formattedPathValues += " $char"
                }
                else -> {
                    formattedPathValues += char
                }
            }
        }
        
        val values = formattedPathValues.split(" ")
        
        val operations = ArrayList<SVGOperation>()
        var operationValues = FloatArray(0)
        var currentOperation: SVGOperationType? = null
        
        for (value in values) {
            if (value.isBlank()) {
                continue
            }
            println(value)
            if (value[0].isLetter()) {
                if (currentOperation != null) {
                    operations += SVGOperation(currentOperation, operationValues)
                }
                currentOperation = SVGOperationType.fromId(value[0])
                if (currentOperation != SVGOperationType.CLOSE_PATH) {
                    operationValues += value.substring(1).toFloat()
                } else {
                    operations += SVGOperation(SVGOperationType.CLOSE_PATH)
                }
            } else {
                operationValues += value.toFloat()
            }
        }
        
        var point = Vector2()
        
        for (operation in operations) {
            point = operation.computeValues(point)
            val scaledPoint = Vector2(point.x / width.toFloat(), point.y / height.toFloat())
            vertices += scaledPoint.toArray()
        }
        
        val mesh = SVGMesh(vertices)
        
        return SVGFile(mesh)
    }
    
    private fun getValueOfAttribute(attributeName: String, line: String): String {
        val startIndex = line.indexOf('"', line.indexOf(attributeName)) + 1
        val endIndex = line.indexOf('"', startIndex)
        return line.substring(startIndex, endIndex)
    }
}