package userinterface.layout.constraints

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.constrainttypes.*
import userinterface.items.ItemDimensions

class ConstraintSet(val constraints: ArrayList<Constraint> = ArrayList()) {

    constructor(vararg constraints: Constraint) : this() {
        this.constraints.addAll(constraints)
    }

    private var dimensions = ItemDimensions()

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

    fun add(constraint: Constraint, parent: MovableUIContainer? = null, parentDimensions: ItemDimensions? = null) {
        constraints += constraint
        apply(parent, parentDimensions)
    }
    
    fun add(constraints: ArrayList<Constraint>, parent: MovableUIContainer? = null, parentDimensions: ItemDimensions? = null) {
        this.constraints += constraints
        apply(parent, parentDimensions)
    }
    
    fun add(constraints: ConstraintSet, parent: MovableUIContainer? = null, parentDimensions: ItemDimensions? = null) {
        this.constraints += constraints.constraints
        apply(parent, parentDimensions)
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
    
    fun copy(): ConstraintSet {
        println(translation())
        val copiedConstraints = ArrayList<Constraint>()
        for (constraint in constraints) {
            copiedConstraints += constraint
        }
        val copiedConstraintSet = ConstraintSet(copiedConstraints)
//        copiedConstraintSet.dimensions = this.dimensions
        copiedConstraintSet.dimensions = this.dimensions.copy()
        println(translation())
        println(copiedConstraintSet.translation())
        println()
        return copiedConstraintSet
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
        constraints.sortWith(compareBy {
            return@compareBy when (it) {
                is PixelConstraint -> 1
                else -> -1
            }
        })
        
        for (constraint in constraints) {
            constraint.apply(dimensions, parentDimensions, parent)
        }
    }
    
    fun computeResult(parentDimensions: ItemDimensions?, parent: MovableUIContainer?): ItemDimensions {
        constraints.sortWith(compareBy {
            return@compareBy when (it) {
                is PixelConstraint -> 1
                else -> -1
            }
        })
        
        val result = ItemDimensions()
        for (constraint in constraints) {
            constraint.apply(result, parentDimensions, parent)
        }
        
        return result
    }
}