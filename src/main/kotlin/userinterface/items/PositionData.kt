package userinterface.items

import math.vectors.Vector2
import userinterface.layout.constraints.ConstraintSet

class PositionData(val parentTranslation: Vector2, val parentScale: Vector2, val constraints: ConstraintSet, val children: ArrayList<Item>) {

//    fun computeItemPosition(): ItemPosition {
//        val data = constraints.apply(parentTranslation, parentScale, children)
//        return ItemPosition(data.translation, data.scale)
//    }

}