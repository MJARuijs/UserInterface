package userinterface.window

import graphics.textures.ImageMap
import math.Color
import resources.images.ImageCache
import userinterface.constraints.*
import userinterface.items.Button
import userinterface.items.Item
import userinterface.items.backgrounds.Background
import userinterface.items.backgrounds.TexturedBackground

class TitleBar(id: String = "TitleBar", constraintSet: ConstraintSet, background: Background, closeButtonAlignment: ButtonAlignment) : Item(id, constraintSet, background) {

    constructor(id: String, height: Float, background: Background, closeButtonAlignment: ButtonAlignment) : this(
        id,
        ConstraintSet(
            CenterConstraint(ConstraintDirection.VERTICAL),
            PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
            RelativeConstraint(ConstraintDirection.HORIZONTAL, 1.0f),
            RelativeConstraint(ConstraintDirection.VERTICAL, height)
        ),
        background,
        closeButtonAlignment
    )

    private var closeButton: Button? = null

    val height: Float

    init {
        val heightConstraint = constraintSet.findConstraint(ConstraintType.RELATIVE, ConstraintDirection.VERTICAL)
        height = (heightConstraint as RelativeConstraint).percentage

        if (closeButtonAlignment != ButtonAlignment.HIDDEN) {
            val closeButtonTexture = ImageMap(ImageCache.get("textures/userinterface/close_button.png"))
            val closeButtonBackground = TexturedBackground(closeButtonTexture, overlayColor = Color(1.0f, 1.0f, 1.0f, 1.0f))

            if (closeButtonAlignment == ButtonAlignment.RIGHT) {
                val constraints = ConstraintSet(
                    PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
                    PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f),
                    RelativeConstraint(ConstraintDirection.VERTICAL, 1.0f),
                    AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
                )

                closeButton = Button("close_button", constraints, closeButtonBackground)
            } else if (closeButtonAlignment == ButtonAlignment.LEFT) {
                val constraints = ConstraintSet(
                    PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
                    PixelConstraint(ConstraintDirection.TO_LEFT, 0.0f),
                    RelativeConstraint(ConstraintDirection.VERTICAL, 1.0f),
                    AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
                )

                closeButton = Button("close_button", constraints, closeButtonBackground)
            }

            add(closeButton!!)
        }
    }
}