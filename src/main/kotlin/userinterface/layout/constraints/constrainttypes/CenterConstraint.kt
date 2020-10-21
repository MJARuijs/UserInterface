package userinterface.layout.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class CenterConstraint(direction: ConstraintDirection) : Constraint(direction) {
    
    override fun copy(): Constraint {
        return CenterConstraint(direction)
    }
    
    override fun apply(itemDimensions: ItemDimensions, parentDimensions: ItemDimensions?, parent: MovableUIContainer?) {
        if (parent == null && parentDimensions == null) {
            return
        }
        
        var referenceTranslation = Vector2()
        
        if (parentDimensions != null) {
            referenceTranslation = parentDimensions.translation
        } else if (parent != null) {
            referenceTranslation = parent.getTranslation()
        }
    
        when (direction) {
            ConstraintDirection.HORIZONTAL -> itemDimensions.translation.x = referenceTranslation.x
            ConstraintDirection.VERTICAL -> itemDimensions.translation.y = referenceTranslation.y
            else -> println("Invalid direction given for CenterConstraint!")
        }
        
        itemDimensions.translation.roundToDecimal(5)
    }
}