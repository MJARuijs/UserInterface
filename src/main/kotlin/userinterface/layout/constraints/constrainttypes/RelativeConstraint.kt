package userinterface.layout.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class RelativeConstraint(direction: ConstraintDirection, var percentage: Float, val anchorId: String = "parent") : Constraint(direction) {
    
    override fun apply(itemDimensions: ItemDimensions, parentDimensions: ItemDimensions?, parent: MovableUIContainer?) {
        if (parent == null && parentDimensions == null) {
            return
        }
        
        var referenceScale = Vector2()
        
        if (parentDimensions != null) {
            referenceScale = parentDimensions.scale
        } else if (parent != null) {
            referenceScale = parent.getScale()
        }
        
        if (anchorId != "parent") {
            referenceScale = parent?.findById(anchorId)?.getGoalDimensions()?.scale ?: referenceScale
        }
    
        if (direction == ConstraintDirection.HORIZONTAL) {
            itemDimensions.scale.x = referenceScale.x * percentage
        }
    
        if (direction == ConstraintDirection.VERTICAL) {
            itemDimensions.scale.y = referenceScale.y * percentage
        }
    }
}