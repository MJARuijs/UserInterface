package userinterface.items

import math.Color
import math.vectors.Vector2
import userinterface.constraints.*
import userinterface.items.backgrounds.ColoredBackground

class Switch(id: String, constraints: ConstraintSet, trackBackground: ColoredBackground, private val thumbBackground: ColoredBackground) : Item(id, constraints, trackBackground) {

    constructor(id: String, constraints: ConstraintSet, trackColor: Color, thumbColor: Color) : this(id, constraints, ColoredBackground(trackColor, 90.0f), ColoredBackground(thumbColor, 90.0f))

    constructor(id: String, position: Vector2, scale: Vector2, trackColor: Color, thumbColor: Color) : this(id, ConstraintSet(

    ), trackColor, thumbColor)

    private lateinit var thumb: Item

    init {
//        println(constraints.scale.y)

//        val thumbConstraints = ConstraintSet(
//            CenterConstraint(ConstraintDirection.VERTICAL),
//            RelativeConstraint(ConstraintDirection.TO_LEFT, -constraints.scale.x / 2.0f),
//            RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
//            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
//        )
//
//        thumb = Item("${id}_thumb", thumbConstraints, thumbBackground)
//
//        add(thumb)
//        println(scale.x)
////
//        val thumbConstraints = ConstraintSet(
//            CenterConstraint(ConstraintDirection.VERTICAL),
//            PixelConstraint(ConstraintDirection.TO_RIGHT, -scale.x, id),
//            RelativeConstraint(ConstraintDirection.VERTICAL, 1.5f),
//            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
//        )
//
//        thumb = Item("${id}_thumb", thumbConstraints, thumbBackground)
//        println(thumb.translation)
//        add(thumb)
    }

    override fun position(parentTranslation: Vector2, parentScale: Vector2, parentChildren: ArrayList<Item>) {
        super.position(parentTranslation, parentScale, parentChildren)

        println(scale.x)

        val thumbConstraints = ConstraintSet(
            CenterConstraint(ConstraintDirection.VERTICAL),
            PixelConstraint(ConstraintDirection.TO_RIGHT, -scale.x, id),
            RelativeConstraint(ConstraintDirection.VERTICAL, 1.5f),
            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
        )

        thumb = Item("${id}_thumb", thumbConstraints, thumbBackground)
        thumb.position(translation, scale, parentChildren)
        println(thumb.translation)
        add(thumb)
    }
}