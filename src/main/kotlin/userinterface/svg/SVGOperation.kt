package userinterface.svg

import math.vectors.Vector2

class SVGOperation(private val type: SVGOperationType, val values: FloatArray = floatArrayOf()) {
    
    fun computeValues(startPoint: Vector2): FloatArray {
        val points = FloatArray(0)
        
        when (type) {
            SVGOperationType.MOVE -> {
                Vector2(startPoint[0] + values[0], startPoint[1] + values[1])
            }
            SVGOperationType.HORIZONTAL_LINE -> {
                Vector2(startPoint[0] + values[0], startPoint[1])
            }
            SVGOperationType.VERTICAL_LINE -> {
                Vector2(startPoint[0], startPoint[1] + values[1])
            }
            else -> Vector2()
        }
        
        
    }
}