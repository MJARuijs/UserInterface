package userinterface.layout.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class CenterConstraint(direction: ConstraintDirection) : Constraint(direction) {
    
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
    
        if (direction == ConstraintDirection.HORIZONTAL) {
            itemDimensions.translation.x = referenceTranslation.x
        }
        if (direction == ConstraintDirection.VERTICAL) {
            itemDimensions.translation.y = referenceTranslation.y
        }
//        apply(itemDimensions, ItemDimensions(parent.getTranslation(), parent.getScale()), parent)
    }
    
//    override fun apply(itemDimensions: ItemDimensions, parentDimensions: ItemDimensions?, parent: MovableUIContainer?) {
//        val parentTranslation = parentDimensions?.translation ?: Vector2(0f, 0f)
//
//        if (direction == ConstraintDirection.HORIZONTAL) {
//            itemDimensions.translation.x = parentTranslation.x
//        }
//        if (direction == ConstraintDirection.VERTICAL) {
//            itemDimensions.translation.y = parentTranslation.y
//        }
//    }
}