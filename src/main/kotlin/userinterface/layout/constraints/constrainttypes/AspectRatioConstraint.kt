package userinterface.layout.constraints.constrainttypes

import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class AspectRatioConstraint(direction: ConstraintDirection, var aspectRatio: Float) : Constraint(direction) {
    
    override fun apply(itemDimensions: ItemDimensions, parentDimensions: ItemDimensions?, parent: MovableUIContainer?) {
        if (parent == null && parentDimensions == null) {
            return
        }
        
        if (direction == ConstraintDirection.VERTICAL) {
            itemDimensions.scale.y = itemDimensions.scale.x * aspectRatio
        }
        
        if (direction == ConstraintDirection.HORIZONTAL) {
            itemDimensions.scale.x = itemDimensions.scale.y * aspectRatio
        }
        
        itemDimensions.scale.roundToDecimal(5)
    }
}