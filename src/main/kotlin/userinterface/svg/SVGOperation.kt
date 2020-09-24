package userinterface.svg

import math.vectors.Vector2
import kotlin.math.pow

class SVGOperation(val type: SVGOperationType, private val isAbsolute: Boolean, val values: FloatArray = floatArrayOf()) {
    
    private val smoothness = 100
    
    fun computeValues(startPoint: Vector2, computedPoints: ArrayList<Vector2>): Pair<FloatArray, Vector2> {
        var points = FloatArray(0)
        var point = if (isAbsolute) {
            Vector2()
        } else {
            startPoint
        }
        
        if (type == SVGOperationType.CLOSE_PATH) {
            point = computedPoints[0]
//            println("Close: $point")
//            points += point.toArray()
            return Pair(points, point)
        }
        
        if (type == SVGOperationType.HORIZONTAL_LINE) {
            val delta = values[0]
            if (computedPoints.isEmpty() || computedPoints.last().x != point.x || computedPoints.last().y != point.y) {
                points += point.toArray()
            }
            point.x += delta
            points += point.toArray()
            return Pair(points, point)
        }
    
        if (type == SVGOperationType.VERTICAL_LINE) {
            val delta = values[0]
            point.y -= delta
            points += point.toArray()
            return Pair(points, point)
        }
        
        if (type == SVGOperationType.BEZIER_CURVE) {
            val factor = 1.0f
            val controlPoint1 = Vector2(values[0], factor * values[1]) + point
            val controlPoint2 = Vector2(values[2], factor * values[3]) + point
            val endPoint = Vector2(values[4], factor * values[5]) + point
    
//            println(point)
//            println(controlPoint1)
//            println(controlPoint2)
//            println("End: $endPoint")
    
            var newPoint = Vector2()
//            points += point.toArray()
            for (j in 0 until smoothness) {
                val smoothness = j.toFloat() / smoothness.toFloat()
                newPoint = point * (1 - smoothness).pow(3) +
                        controlPoint1 * 3.0f * (1.0f - smoothness).pow(2) * smoothness +
                        controlPoint2 * 3.0f * (1.0f - smoothness) * smoothness.pow(2) +
                        endPoint * smoothness.pow(3)
                
//                points += newPoint.toArray()
            }
            points += endPoint.toArray()

//            println("Actual end: $newPoint")
            return Pair(points, newPoint)
        }
        
        for (i in values.indices step 2) {
            val x = values[i]
            val y = values[i + 1]
    
            when (type) {
                SVGOperationType.MOVE -> {
                    point.x += x
                    point.y += y
//                    if (i > 0) {
                        points += point.toArray()
//                    }
                }
                SVGOperationType.LINE -> {
                    println(point)
                    point.x += x
                    point.y += y
                    println(point)
                    points += point.toArray()
                }

            }
        }
        
        return Pair(points, point)
    }
}