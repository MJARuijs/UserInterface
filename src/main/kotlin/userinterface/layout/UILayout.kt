package userinterface.layout

import userinterface.UIColor
import userinterface.layout.constraints.ConstraintSet

class UILayout(val id: String, val itemChanges: HashMap<String, Pair<ConstraintSet, UIColor?>> = HashMap()) {

    operator fun plusAssign(itemChange: Triple<String, ConstraintSet, UIColor>) {
        itemChanges[itemChange.first] = Pair(itemChange.second, itemChange.third)
    }

    operator fun plusAssign(itemChange: Pair<String, ConstraintSet>) {
        itemChanges[itemChange.first] = Pair(itemChange.second, null)
    }


    fun getConstraintsChange(id: String) = itemChanges[id]?.first

    fun getColorChange(id: String) = itemChanges[id]?.second

}