package userinterface.layout.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

class PixelConstraint(direction: ConstraintDirection, var offset: Float = 0.0f, var anchorId: String = "parent") : Constraint(direction) {
    
    override fun apply(itemDimensions: ItemDimensions, parentDimensions: ItemDimensions?, parent: MovableUIContainer?) {
        var referenceTranslation = Vector2()
        var referenceScale = Vector2()
    
        if (parent == null && parentDimensions == null) {
            return
        } else if (parentDimensions != null) {
            referenceTranslation = parentDimensions.translation
            referenceScale = parentDimensions.scale
        } else if (parent != null) {
            referenceTranslation = parent.getTranslation()
            referenceScale = parent.getScale()
        }
        
        if (anchorId != "parent") {
            val sibling = parent?.findById(anchorId)
            if (sibling != null) {
                referenceTranslation = sibling.getGoalDimensions().translation
                referenceScale = sibling.getGoalDimensions().scale
            }
        }
    
        if (direction == ConstraintDirection.HORIZONTAL || direction == ConstraintDirection.VERTICAL) {
            println("Invalid direction given for PixelConstraint!")
        }
    
        if (direction == ConstraintDirection.TO_TOP) {
            val referenceTop = referenceScale.y + referenceTranslation.y
            if (anchorId == "parent") {
                itemDimensions.translation.y = referenceTop - itemDimensions.scale.y
                itemDimensions.translation.y -= offset * referenceScale.y * 2.0f
            } else {
                itemDimensions.translation.y = referenceTop + itemDimensions.scale.y
                itemDimensions.translation.y += offset * referenceScale.y * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_BOTTOM) {
            val referenceBottom = referenceTranslation.y - referenceScale.y
            if (anchorId == "parent") {
                itemDimensions.translation.y = referenceBottom + itemDimensions.scale.y
                itemDimensions.translation.y += offset * referenceScale.y * 2.0f
            } else {
                itemDimensions.translation.y = referenceBottom - itemDimensions.scale.y
                itemDimensions.translation.y -= offset * referenceScale.y * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_LEFT) {
            val referenceLeft = referenceTranslation.x - referenceScale.x
            if (anchorId == "parent") {
                itemDimensions.translation.x = referenceLeft + itemDimensions.scale.x
                itemDimensions.translation.x += offset * referenceScale.x * 2.0f
            } else {
                itemDimensions.translation.x = referenceLeft - itemDimensions.scale.x
                itemDimensions.translation.x -= offset * referenceScale.x * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_RIGHT) {
            val referenceRight = referenceTranslation.x + referenceScale.x
            if (anchorId == "parent") {
                itemDimensions.translation.x = referenceRight - itemDimensions.scale.x
                itemDimensions.translation.x -= offset * referenceScale.x * 2.0f
            } else {
                itemDimensions.translation.x = referenceRight + itemDimensions.scale.x
                itemDimensions.translation.x += offset
            }
        }
        itemDimensions.translation.roundToDecimal(5)
    }
}