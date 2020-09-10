package userinterface

import math.vectors.Vector2
import userinterface.constraints.ConstraintSet

open class MovableUIContainer(id: String, val constraints: ConstraintSet) : UIContainer(id) {
    
    fun getTranslation() = constraints.translation()
    
    fun getScale() = constraints.scale()
    
    open fun position(parent: MovableUIContainer? = null) {
        constraints.apply(parent)
        
        children.forEach { child ->
            child.position(this)
        }
    }
    
    fun translate(translation: Vector2) {
        constraints.translate(translation)
        children.forEach { child ->
            child.translate(translation)
        }
    }
    
    fun place(placement: Vector2) {
        val difference = placement - constraints.translation()
        children.forEach { child ->
            child.translate(difference)
        }
        constraints.place(placement)
    }
}