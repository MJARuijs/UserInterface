package userinterface.layout.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection

class CenterConstraint(direction: ConstraintDirection) : Constraint(direction) {
    
    override fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2?, parentScale: Vector2?, parent: MovableUIContainer?): Pair<Vector2, Vector2> {
        if (parent == null && parentTranslation == null) {
            return Pair(translation, scale)
        }
        
        var referenceTranslation = Vector2()
        
        if (parentTranslation != null) {
            referenceTranslation = parentTranslation
        } else if (parent != null) {
            referenceTranslation = parent.getTranslation()
        }
    
        when (direction) {
            ConstraintDirection.HORIZONTAL -> translation.x = referenceTranslation.x
            ConstraintDirection.VERTICAL -> translation.y = referenceTranslation.y
            else -> println("Invalid direction given for CenterConstraint!")
        }
        
        translation.roundToDecimal(5)
        
        return Pair(translation, scale)
    }
}