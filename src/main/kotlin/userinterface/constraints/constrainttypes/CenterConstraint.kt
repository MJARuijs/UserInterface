package userinterface.constraints.constrainttypes

import userinterface.MovableUIContainer
import userinterface.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class CenterConstraint(direction: ConstraintDirection) : Constraint(direction) {
    
    override fun apply(itemDimensions: ItemDimensions, parent: MovableUIContainer?): ItemDimensions {
        if (parent == null) {
            return itemDimensions
        }
        
        val result = itemDimensions
        
        val parentTranslation = parent.getTranslation()
        
        if (direction == ConstraintDirection.HORIZONTAL) {
            result.translation.x = parentTranslation.x
        }
        if (direction == ConstraintDirection.VERTICAL) {
            result.translation.y = parentTranslation.y
        }
        return result
    }
}