package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

class PixelConstraint(direction: ConstraintDirection, private val offset: Float, val anchorId: String = "parent") : Constraint(direction) {

    override fun type() = ConstraintType.PIXEL

    override fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item>): ItemPosition {

        var referenceTranslation = parentTranslation
        var referenceScale = parentScale

        if (anchorId != "parent") {
            referenceTranslation = siblings.findLast { item -> item.id == anchorId }?.translation ?: referenceTranslation
            referenceScale = siblings.findLast { item -> item.id == anchorId }?.scale ?: referenceScale
        }

        if (direction == ConstraintDirection.HORIZONTAL || direction == ConstraintDirection.VERTICAL) {
            println("Invalid direction given for PixelConstraint!")
        }

        if (direction == ConstraintDirection.TO_TOP) {
            val parentTop = referenceScale.y + referenceTranslation.y
            if (anchorId == "parent") {
                translation.y = parentTop - scale.y
                translation.y -= offset * referenceScale.y * 2.0f
            } else {
                translation.y = parentTop + scale.y
                translation.y += offset * referenceScale.y * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_BOTTOM) {
            val parentBottom = referenceTranslation.y - referenceScale.y
            if (anchorId == "parent") {
                translation.y = parentBottom + scale.y
                translation.y += offset * referenceScale.y * 2.0f
            } else {
                translation.y = parentBottom - scale.y
                translation.y -= offset * referenceScale.y * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_LEFT) {
            val parentLeft = referenceTranslation.x - referenceScale.x
            if (anchorId == "parent") {
                translation.x = parentLeft + scale.x
                translation.x += offset * referenceScale.x * 2.0f
            } else {
                translation.x = parentLeft - scale.x
                translation.x -= offset * referenceScale.x * 2.0f
            }
        }
        if (direction == ConstraintDirection.TO_RIGHT) {
            val parentRight = referenceTranslation.x + referenceScale.x
            if (anchorId == "parent") {
                translation.x = parentRight - scale.x
                translation.x -= offset * referenceScale.x * 2.0f
            } else {
                translation.x = parentRight + scale.x
                translation.x += offset * referenceScale.x * 2.0f
            }
        }

        return ItemPosition(translation, scale)
    }
}