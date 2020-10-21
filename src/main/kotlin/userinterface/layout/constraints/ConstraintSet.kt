package userinterface.layout.constraints

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.constrainttypes.*

class ConstraintSet(val constraints: ArrayList<Constraint> = ArrayList()) {

    constructor(vararg constraints: Constraint) : this() {
        this.constraints.addAll(constraints)
    }

//    private var dimensions = ItemDimensions()
    
    private var translation = Vector2()
    private var scale = Vector2(1f, 1f)

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

    fun add(constraint: Constraint, parent: MovableUIContainer? = null, parentTranslation: Vector2? = null, parentScale: Vector2? = null) {
        constraints += constraint
        apply(parent, parentTranslation, parentScale)
    }
    
    fun add(constraints: ArrayList<Constraint>, parent: MovableUIContainer? = null, parentTranslation: Vector2? = null, parentScale: Vector2? = null) {
        this.constraints += constraints
        apply(parent, parentTranslation, parentScale)
    }
    
    fun add(constraints: ConstraintSet, parent: MovableUIContainer? = null, parentTranslation: Vector2? = null, parentScale: Vector2? = null) {
        this.constraints += constraints.constraints
        apply(parent, parentTranslation, parentScale)
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

    fun getTranslation() = translation

    fun getScale() = scale

    fun translate(translation: Vector2) {
        this.translation += translation
    }
    
    fun place(placement: Vector2) {
        this.translation = placement
    }
    
    fun setScale(scale: Vector2) {
        this.scale = scale
    }

    fun getScale(scale: Vector2) {
        this.scale *= scale
    }
    
    fun addToScale(scale: Vector2) {
        this.scale += scale
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
    
    fun apply(parent: MovableUIContainer? = null, parentTranslation: Vector2? = null, parentScale: Vector2? = null) {
        constraints.sortWith(compareBy {
            return@compareBy when (it) {
                is PixelConstraint -> 1
                else -> -1
            }
        })
        
        for (constraint in constraints) {
            val newDimensions = constraint.apply(translation, scale, parentTranslation, parentScale, parent)
            translation = newDimensions.first
            scale = newDimensions.second
        }
    }
    
    fun computeResult(parentTranslation: Vector2?, parentScale: Vector2?, parent: MovableUIContainer?): Pair<Vector2, Vector2> {
        constraints.sortWith(compareBy {
            return@compareBy when (it) {
                is PixelConstraint -> 1
                else -> -1
            }
        })
        
        var resultTranslation = Vector2()
        var resultScale = Vector2()
        for (constraint in constraints) {
            val newDimensions = constraint.apply(resultTranslation, resultScale, parentTranslation, parentScale, parent)
            resultTranslation = newDimensions.first
            resultScale = newDimensions.second
        }
        
        return Pair(resultTranslation, resultScale)
    }
    
}