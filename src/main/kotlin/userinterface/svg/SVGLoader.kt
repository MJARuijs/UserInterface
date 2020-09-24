package userinterface.svg

import math.vectors.Vector2
import resources.Loader
import util.File

class SVGLoader : Loader<SVGFile> {
    
    private val operationsPattern = "\\s*(?<operation>[a-zA-Z]\\s*,?\\s*(-|[0-9]|\\.|\\s)*)".toRegex().toPattern()
    private val pointPattern = "(?<x>-?[0-9]+(\\.[0-9]+)?)\\s*,?\\s*(?<y>-?[0-9]+(\\.[0-9]+)?)?\\s*".toRegex().toPattern()
    private val triangulation = Triangulation()
    
    
    override fun load(path: String): SVGFile {
        val file = File(path)
        val content = file.getContent()
        val points = two(content)
        val triangulatedPoints = triangulation.process(points) ?: throw IllegalArgumentException("")
    
        var vertices = FloatArray(0)
        triangulatedPoints.forEach { point -> vertices += point.toArray() }
//        points.forEach { point -> vertices += point.toArray() }

        return SVGFile(SVGMesh(vertices))
    }
    
    private fun two(content: String): ArrayList<Vector2> {
        val height = getValueOfAttribute("height", content).removeSuffix("pt").toFloat()
        val width = getValueOfAttribute("width", content).removeSuffix("pt").toFloat()
        var pathValues = getValueOfAttribute("path", content)
    
        var operationsMatcher = operationsPattern.matcher(pathValues)
        val operations = ArrayList<SVGOperation>()
    
        while (operationsMatcher.find()) {
            val operation = operationsMatcher.group("operation")
//            operations += parseOperation(operation, 1f, 1f)
            operations += parseOperation(operation, width, height)
        
            pathValues = pathValues.removePrefix(operation)
            operationsMatcher = operationsPattern.matcher(pathValues)
        }
    
        val vertices = ArrayList<Vector2>()
    
        var currentPoint = Vector2()
        
        var firstX = Float.MAX_VALUE
        var firstY = Float.MAX_VALUE
        
        for (operation in operations) {
//            println(operation.type)
            val operationData = operation.computeValues(currentPoint, vertices)
            currentPoint = operationData.second
            val points = operationData.first
            
            for (i in points.indices step 2) {
                val x = points[i]
                val y = points[i + 1]
//                println("(${x * width}, ${-y * height})")
                vertices += Vector2(x, y)
                if (firstX == Float.MAX_VALUE) {
                    firstX = x
                }
                if (firstY == Float.MAX_VALUE) {
                    firstY = y
                }
            }
        }
        return vertices
    }
    
    private fun parseOperation(input: String, width: Float, height: Float): SVGOperation {
        val operationId = input.first { char -> char.isLetter() }
        
        val operationType = SVGOperationType.fromId(operationId)
        
        var operationPoints = input.removePrefix(operationId.toString())
        var pointMatcher = pointPattern.matcher(operationPoints)
        var backup = operationPoints
        var points = FloatArray(0)
        
        while (pointMatcher.find()) {
            val pointX = pointMatcher.group("x")
            val pointY = pointMatcher.group("y")
            
            points += (pointX.toFloat() / width / 1.77f)
            if (pointY != null) {
                points += -pointY.toFloat() / height
            }
//            println("Input: $operationPoints")
//            println("${pointX}, $pointY")
    
            operationPoints = operationPoints.trim().removePrefix(pointX).trim()
//            println("Input 2: $operationPoints")

            if (pointY != null) {
                operationPoints = operationPoints.trim().removePrefix(pointY).trim()
            }
//            println("Input 3: $operationPoints")
    
            operationPoints = operationPoints.trim()

//            println("Remaining: $operationPoints")
            if (operationPoints == backup) {
                println("Breaking: $operationPoints")
                break
            }
    
            backup = operationPoints
            pointMatcher = pointPattern.matcher(operationPoints)
        }
        
//        println(points.size)
        return SVGOperation(operationType, operationId.isUpperCase(), points)
    }
    
    private fun getValueOfAttribute(attributeName: String, line: String): String {
        val startIndex = line.indexOf('"', line.indexOf(attributeName)) + 1
        val endIndex = line.indexOf('"', startIndex)
        return line.substring(startIndex, endIndex)
    }
}