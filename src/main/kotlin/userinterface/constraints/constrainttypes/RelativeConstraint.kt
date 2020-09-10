package userinterface.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class RelativeConstraint(direction: ConstraintDirection, val percentage: Float, val anchorId: String = "parent") : Constraint(direction) {
    
    override fun apply(itemDimensions: ItemDimensions, parent: MovableUIContainer?): ItemDimensions {
        if (parent == null && anchorId != "parent") {
            return itemDimensions
        }
        
        val result = itemDimensions
        var referenceScale = parent?.getScale() ?: Vector2(1.0f, 1.0f)
        
        if (anchorId != "parent") {
            referenceScale = parent?.findById(anchorId)?.getScale() ?: referenceScale
        }
        
        if (direction == ConstraintDirection.HORIZONTAL) {
            result.scale.x = referenceScale.x * percentage
        }
        
        if (direction == ConstraintDirection.VERTICAL) {
            result.scale.y = referenceScale.y * percentage
        }
        
        return result
    }
}