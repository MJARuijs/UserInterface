package userinterface.layout.constraints

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.constrainttypes.*
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
    
        constraints.sortWith(compareBy {
            return@compareBy when (it) {
                is PixelConstraint -> 1
                else -> -1
            }
        })
    }

    fun determineRequiredIds(): ArrayList<String> {
        requiredIds.clear()
        
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

        return requiredIds
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
    
    fun setScale(scale: Vector2) {
        dimensions.scale = scale
    }

    fun scale(scale: Vector2) {
        dimensions.scale *= scale
    }
    
    fun addToScale(scale: Vector2) {
        dimensions.scale += scale
    }
    
    fun updateConstraint(type: ConstraintType, direction: ConstraintDirection, newValue: Float) {
        for (constraint in constraints) {
            if (constraint.direction == direction) {
                when (type) {
                    ConstraintType.PIXEL -> (constraint as PixelConstraint).offset = newValue
                    ConstraintType.RELATIVE -> (constraint as RelativeConstraint).percentage = newValue
                    ConstraintType.ASPECT_RATIO -> (constraint as AspectRatioConstraint).aspectRatio = newValue
                    else -> {}
                }
            }
        }
    }
    
    fun apply(parent: MovableUIContainer? = null, parentDimensions: ItemDimensions? = null) {
        for (constraint in constraints) {
            constraint.apply(dimensions, parentDimensions, parent)
        }
    }
    
    fun computeResult(parentDimensions: ItemDimensions?, parent: MovableUIContainer?): ItemDimensions {
        val result = ItemDimensions()
        for (constraint in constraints) {
            constraint.apply(result, parentDimensions, parent)
        }
        
        return result
    }
}