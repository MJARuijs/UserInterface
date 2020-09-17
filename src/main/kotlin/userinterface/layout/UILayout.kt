package userinterface.layout

import userinterface.layout.constraints.ConstraintSet

class UILayout(val id: String, val constraints: HashMap<String, ConstraintSet> = HashMap()) {
    
    constructor(id: String, vararg itemConstraints: Pair<String, ConstraintSet>) : this(id) {
        for (pair in itemConstraints) {
            constraints[pair.first] = pair.second
        }
    }
    
    operator fun plusAssign(constraint: Pair<String, ConstraintSet>) {
        constraints[constraint.first] = constraint.second
    }
    
    fun containsConstraintForItem(id: String) = constraints.containsKey(id)
    
    fun getConstraint(id: String) = constraints[id]

}