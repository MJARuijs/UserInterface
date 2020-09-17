package userinterface.layout.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class AspectRatioConstraint(direction: ConstraintDirection, private val aspectRatio: Float) : Constraint(direction) {
    
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
        
//        val parentScale = parentDimensions.scale
    
        if (direction == ConstraintDirection.VERTICAL) {
            itemDimensions.scale.y = itemDimensions.scale.x * aspectRatio
        
            if (itemDimensions.scale.y > referenceScale.y) {
                println("UI Item tried to go out of bounds of parent!")
                itemDimensions.scale.y = referenceScale.y
            }
        }
        if (direction == ConstraintDirection.HORIZONTAL) {
            itemDimensions.scale.x = itemDimensions.scale.y * aspectRatio
        
            if (itemDimensions.scale.x > referenceScale.x) {
                println("UI Item tried to go out of bounds of parent!")
                itemDimensions.scale.x = referenceScale.x
            }
        }
//        apply(itemDimensions, ItemDimensions(parent.getTranslation(), parent.getScale()), parent)
    }
    
//    override fun apply(itemDimensions: ItemDimensions, parentDimensions: ItemDimensions?, parent: MovableUIContainer?) {
//        val parentScale = parentDimensions?.scale ?: Vector2(1.0f, 1.0f)
//
//        if (direction == ConstraintDirection.VERTICAL) {
//            itemDimensions.scale.y = itemDimensions.scale.x * aspectRatio
//
//            if (itemDimensions.scale.y > parentScale.y) {
//                println("UI Item tried to go out of bounds of parent!")
//                itemDimensions.scale.y = parentScale.y
//            }
//        }
//        if (direction == ConstraintDirection.HORIZONTAL) {
//            itemDimensions.scale.x = itemDimensions.scale.y * aspectRatio
//
//            if (itemDimensions.scale.x > parentScale.x) {
//                println("UI Item tried to go out of bounds of parent!")
//                itemDimensions.scale.x = parentScale.x
//            }
//        }
//    }
}