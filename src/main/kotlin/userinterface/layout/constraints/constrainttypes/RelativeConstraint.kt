package userinterface.layout.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class RelativeConstraint(direction: ConstraintDirection, var percentage: Float, val anchorId: String = "parent", private val relativePercentage: Boolean = true) : Constraint(direction) {
    
    override fun copy(): Constraint {
        return RelativeConstraint(direction, percentage, anchorId, relativePercentage)
    }
    
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
            
    
            if (relativePercentage) {
                itemDimensions.scale.x = referenceScale.x * percentage
            } else {
                itemDimensions.scale.x = percentage
            }
        }
    
        if (direction == ConstraintDirection.VERTICAL) {
            if (relativePercentage) {
                itemDimensions.scale.y = referenceScale.y * percentage
            } else {
                itemDimensions.scale.y = percentage
            }
        }
        
        itemDimensions.scale.roundToDecimal(5)
    }
}