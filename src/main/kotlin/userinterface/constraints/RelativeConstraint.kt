package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

class RelativeConstraint(direction: ConstraintDirection, val percentage: Float, private val anchorId: String = "parent") : Constraint(direction) {

    override fun type() = ConstraintType.RELATIVE

    override fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item>): ItemPosition {

        var referenceScale = parentScale

        if (anchorId != "parent") {
            referenceScale = siblings.findLast { item -> item.id == anchorId }?.scale ?: referenceScale
        }

        if (direction == ConstraintDirection.HORIZONTAL) {
            scale.x = referenceScale.x * percentage
        }

        if (direction == ConstraintDirection.VERTICAL) {
            scale.y = referenceScale.y * percentage
        }

        return ItemPosition(translation, scale)
    }
}