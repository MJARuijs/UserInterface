package userinterface.constraints.constrainttypes

import userinterface.MovableUIContainer
import userinterface.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class AspectRatioConstraint(direction: ConstraintDirection, private val aspectRatio: Float) : Constraint(direction) {
    
    override fun apply(itemDimensions: ItemDimensions, parent: MovableUIContainer?): ItemDimensions {
        if (parent == null) {
            return itemDimensions
        }
        
        val parentScale = parent.getScale()
        val result = itemDimensions.copy()
        
        if (direction == ConstraintDirection.VERTICAL) {
            result.scale.y = itemDimensions.scale.x * aspectRatio
            
            if (itemDimensions.scale.y > parentScale.y) {
                println("UI Item tried to go out of bounds of parent!")
                result.scale.y = parentScale.y
            }
        }
        if (direction == ConstraintDirection.HORIZONTAL) {
            result.scale.x = itemDimensions.scale.y * aspectRatio
            
            if (itemDimensions.scale.x > parentScale.x) {
                println("UI Item tried to go out of bounds of parent!")
                result.scale.x = parentScale.x
            }
        }
        return result
    }
}