package userinterface.constraints.constrainttypes

import userinterface.MovableUIContainer
import userinterface.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class PixelConstraint(direction: ConstraintDirection, private val offset: Float, var anchorId: String = "parent") : Constraint(direction) {
    
    override fun apply(itemDimensions: ItemDimensions, parent: MovableUIContainer?): ItemDimensions {
        if (parent == null) {
            return itemDimensions
        }
        
        var referenceTranslation = parent.getTranslation()
        var referenceScale = parent.getScale()
        val result = itemDimensions
    
        if (anchorId != "parent") {
            val sibling = parent.findById(anchorId)
            if (sibling != null) {
                referenceTranslation = sibling.getTranslation()
                referenceScale = sibling.getScale()
            }
        }
    
        if (direction == ConstraintDirection.HORIZONTAL || direction == ConstraintDirection.VERTICAL) {
            println("Invalid direction given for PixelConstraint!")
        }
    
        if (direction == ConstraintDirection.TO_TOP) {
            val parentTop = referenceScale.y + referenceTranslation.y
            if (anchorId == "parent") {
                result.translation.y = parentTop - itemDimensions.scale.y
                result.translation.y -= offset * referenceScale.y * 2.0f
            } else {
                result.translation.y = parentTop + itemDimensions.scale.y
                result.translation.y += offset * referenceScale.y * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_BOTTOM) {
            val parentBottom = referenceTranslation.y - referenceScale.y
            if (anchorId == "parent") {
                result.translation.y = parentBottom + itemDimensions.scale.y
                result.translation.y += offset * referenceScale.y * 2.0f
            } else {
                result.translation.y = parentBottom - itemDimensions.scale.y
                result.translation.y -= offset * referenceScale.y * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_LEFT) {
            val parentLeft = referenceTranslation.x - referenceScale.x
            if (anchorId == "parent") {
                result.translation.x = parentLeft + itemDimensions.scale.x
                result.translation.x += offset * referenceScale.x * 2.0f
            } else {
                result.translation.x = parentLeft - itemDimensions.scale.x
                result.translation.x -= offset * referenceScale.x * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_RIGHT) {
            val parentRight = referenceTranslation.x + referenceScale.x
            if (anchorId == "parent") {
                result.translation.x = parentRight - itemDimensions.scale.x
                result.translation.x -= offset * referenceScale.x * 2.0f
            } else {
                result.translation.x = parentRight + itemDimensions.scale.x
                result.translation.x += offset * referenceScale.x * 2.0f
            }
        }
        return result
    }
}