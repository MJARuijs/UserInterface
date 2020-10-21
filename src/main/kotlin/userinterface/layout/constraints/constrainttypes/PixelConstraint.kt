package userinterface.layout.constraints.constrainttypes

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection

class PixelConstraint(direction: ConstraintDirection, var offset: Float = 0.0f, var anchorId: String = "parent") : Constraint(direction) {

    override fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2?, parentScale: Vector2?, parent: MovableUIContainer?): Pair<Vector2, Vector2> {
        var referenceTranslation = Vector2()
        var referenceScale = Vector2()
    
        if (parent == null && parentTranslation == null && parentScale == null) {
            return Pair(translation, scale)
        } else if (parentTranslation != null && parentScale != null) {
            referenceTranslation = parentTranslation
            referenceScale = parentScale
        } else if (parent != null) {
            referenceTranslation = parent.getTranslation()
            referenceScale = parent.getScale()
        }
        
        if (anchorId != "parent") {
            val sibling = parent?.findById(anchorId)
            if (sibling != null) {
                referenceTranslation = sibling.getGoalDimensions().first
                referenceScale = sibling.getGoalDimensions().second
            }
        }
    
        if (direction == ConstraintDirection.HORIZONTAL || direction == ConstraintDirection.VERTICAL) {
            println("Invalid direction given for PixelConstraint!")
        }
    
        if (direction == ConstraintDirection.TO_TOP) {
            val referenceTop = referenceScale.y + referenceTranslation.y
            if (anchorId == "parent") {
                translation.y = referenceTop - scale.y
                translation.y -= offset * referenceScale.y * 2.0f
            } else {
                translation.y = referenceTop + scale.y
                translation.y += offset * referenceScale.y * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_BOTTOM) {
            val referenceBottom = referenceTranslation.y - referenceScale.y
            if (anchorId == "parent") {
                translation.y = referenceBottom + scale.y
                translation.y += offset * referenceScale.y * 2.0f
            } else {
                translation.y = referenceBottom - scale.y
                translation.y -= offset * referenceScale.y * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_LEFT) {
            val referenceLeft = referenceTranslation.x - referenceScale.x
            if (anchorId == "parent") {
                translation.x = referenceLeft + scale.x
                translation.x += offset * referenceScale.x * 2.0f
            } else {
                translation.x = referenceLeft - scale.x
                translation.x -= offset * referenceScale.x * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_RIGHT) {
            val referenceRight = referenceTranslation.x + referenceScale.x
            if (anchorId == "parent") {
                translation.x = referenceRight - scale.x
                translation.x -= offset * referenceScale.x * 2.0f
            } else {
                translation.x = referenceRight + scale.x
                translation.x += offset
            }
        }
        translation.roundToDecimal(5)
        
        return Pair(translation, scale)
    }
}