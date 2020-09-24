package userinterface.svg

import math.vectors.Vector2
import resources.Loader
import util.File

class SVGLoader : Loader<SVGFile> {
    
    private val operationsPattern = "\\s*(?<operation>[a-zA-Z]\\s*,?\\s*(-|[0-9]|\\.|\\s)*)".toRegex().toPattern()
    private val pointPattern = "(?<x>-?[0-9]+(\\.[0-9]+)?)\\s*,?\\s*(?<y>-?[0-9]+(\\.[0-9]+)?)?\\s*".toRegex().toPattern()
    private val triangulation = Triangulation()
    private val childVertices = ArrayList<ArrayList<Vector2>>()
    
    override fun load(path: String): SVGFile {
        val file = File(path)
        val content = file.getContent()
        val pointData = one(content)
        
        val verticesToBeTriangulated = pointData.first
        val triangulatedVertices = pointData.second
        val triangulatedPoints = triangulation.process(verticesToBeTriangulated) ?: throw IllegalArgumentException("")
    
        var vertices = FloatArray(0)
    
//        verticesToBeTriangulated.forEach { point -> vertices += point.toArray() }
        triangulatedPoints.forEach { point -> vertices += point.toArray() }
//        triangulatedVertices.forEach { point -> vertices += point.toArray() }
        
        val mesh = SVGMesh(vertices)
        for (childVertex in childVertices) {
            mesh += childVertex
        }
        
        return SVGFile(mesh)
    }
    
    private fun one(content: String): Pair<ArrayList<Vector2>, ArrayList<Vector2>> {
        val viewBox = getValueOfAttribute("viewBox", content)
        val viewBoxValues = viewBox.split(' ')
        val height: Float
        val width: Float
    
        if (viewBoxValues.size < 4) {
            height = getValueOfAttribute("height", content).toFloat()
            width = getValueOfAttribute("width", content).toFloat()
        } else {
            height = viewBoxValues[2].toFloat()
            width = viewBoxValues[3].toFloat()
        }
        
        var pathValues = getValueOfAttribute("path", content)
        
        var operationsMatcher = operationsPattern.matcher(pathValues)
        val operations = ArrayList<SVGOperation>()
        
        while (operationsMatcher.find()) {
            val operation = operationsMatcher.group("operation")
            operations += parseOperation(operation, width, height)
            
            pathValues = pathValues.removePrefix(operation)
            operationsMatcher = operationsPattern.matcher(pathValues)
        }
        
        val verticesToBeTriangulated = ArrayList<Vector2>()
        val triangulatedVertices = ArrayList<Vector2>()
        
        var currentPoint = Vector2()
        
        for (operation in operations) {
            val operationData = operation.computeValues(currentPoint, verticesToBeTriangulated)
            currentPoint = operationData.second
            val points = operationData.first

            if (operation.type == SVGOperationType.BEZIER_CURVE) {
                verticesToBeTriangulated += Vector2(currentPoint)
//                println("Adding $currentPoint")
                val pointsToBeTriangulated = ArrayList<Vector2>()
                for (point in points) {
                    pointsToBeTriangulated += point
                }
                
                    val triangulatedPoints = triangulation.process(pointsToBeTriangulated) ?: throw IllegalArgumentException("")

                    triangulatedPoints.forEach { point -> triangulatedVertices += point }
//                childVertices += triangulatedPoints
//                }
                
            } else {
                for (point in points) {
                    verticesToBeTriangulated += Vector2(point)
                }
            }
        }
        
        return Pair(verticesToBeTriangulated, triangulatedVertices)
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
            
            points += (pointX.toFloat())/ width / 1.77f
            if (pointY != null) {
                points += -(pointY.toFloat() ) / height
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
//            println("Ending with $operationPoints")
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