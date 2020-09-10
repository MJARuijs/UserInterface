package userinterface.constraints

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.constraints.constrainttypes.*
import userinterface.items.ItemDimensions

class ConstraintSet(vararg val constraints: Constraint) {

    val requiredIds = ArrayList<String>()

    init {
        for (constraint in constraints) {
            if (constraint is PixelConstraint) {
                if (constraint.anchorId != "parent") {
                    requiredIds += constraint.anchorId
                }
            } else if (constraint is RelativeConstraint) {
                if (constraint.anchorId != "parent") {
                    requiredIds += constraint.anchorId
                }
            }
        }
    }

    var dimensions = ItemDimensions()

    fun translation() = dimensions.translation

    fun scale() = dimensions.scale

    fun translate(translation: Vector2) {
        dimensions.translation += translation
    }
    
    fun place(placement: Vector2) {
        dimensions.translation = placement
    }

    fun findConstraint(type: ConstraintType, direction: ConstraintDirection): Constraint? {
        return constraints.find { constraint ->
            val isCorrectType = when (type) {
                ConstraintType.CENTER -> constraint is CenterConstraint
                ConstraintType.PIXEL -> constraint is PixelConstraint
                ConstraintType.ASPECT_RATIO -> constraint is AspectRatioConstraint
                ConstraintType.RELATIVE -> constraint is RelativeConstraint
            }

            isCorrectType && constraint.direction == direction
        }
    }
    
    fun apply(parent: MovableUIContainer? = null) {
//        var result = ItemDimensions()
        
        constraints.sortWith(compareBy {
            return@compareBy when (it) {
                is PixelConstraint -> 1
                else -> -1
            }
        })
    
        for (constraint in constraints) {
            constraint.apply(dimensions, parent)
        }
    }
    
    fun computeResult(parent: MovableUIContainer? = null): ItemDimensions {
        constraints.sortWith(compareBy {
            return@compareBy when (it) {
                is PixelConstraint -> 1
                else -> -1
            }
        })
    
        val result = ItemDimensions()
        for (constraint in constraints) {
            constraint.apply(result, parent)
        }
        return result
    }

}