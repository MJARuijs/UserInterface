package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

class PixelConstraint(private val offset: Float, private val direction: ConstraintDirection, private val anchorId: String = "parent") : Constraint() {

    override fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item>): ItemPosition {

        var referenceTranslation = parentTranslation
        var referenceScale = parentScale

        if (anchorId != "parent") {
            referenceTranslation = siblings.findLast { item -> item.id == anchorId }?.translation ?: referenceTranslation
            referenceScale = siblings.findLast { item -> item.id == anchorId }?.scale ?: referenceScale
        }

        if (direction == ConstraintDirection.HORIZONTAL) {
            translation.x = referenceTranslation.x + offset
        }
        if (direction == ConstraintDirection.TO_TOP) {
            val parentTop = referenceScale.y + referenceTranslation.y
            translation.y = parentTop - scale.y
            translation.y -= offset * referenceScale.y * 2.0f
        }
        if (direction == ConstraintDirection.TO_BOTTOM) {
            val parentBottom = referenceTranslation.y - referenceScale.y
            translation.y = parentBottom + scale.y
            translation.y += offset * referenceScale.y * 2.0f
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
            translation.x = parentRight - scale.x
            translation.x -= offset * referenceScale.x * 2.0f
        }

        return ItemPosition(translation, scale)
    }
}