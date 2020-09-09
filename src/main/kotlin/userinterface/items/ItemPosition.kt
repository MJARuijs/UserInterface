package userinterface.items

import math.vectors.Vector2
import userinterface.constraints.ConstraintSet

data class ItemPosition(val translation: Vector2, val scale: Vector2) {

    fun computeItemPosition(constraints: ConstraintSet, parentTranslation: Vector2): ItemPosition {
        val data = constraints.apply(parentTranslation, scale, ArrayList())
        return ItemPosition(data.translation, data.scale)
    }

}