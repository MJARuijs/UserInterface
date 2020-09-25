package userinterface.window

import graphics.textures.ImageMap
import math.Color
import resources.images.ImageCache
import userinterface.UIColor
import userinterface.layout.constraints.constrainttypes.*
import userinterface.items.UIButton
import userinterface.items.Item
import userinterface.items.backgrounds.Background
import userinterface.items.backgrounds.SVGBackground
import userinterface.items.backgrounds.TexturedBackground
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet

class TitleBar(id: String = "TitleBar", constraints: ConstraintSet, background: Background, closeButtonAlignment: ButtonAlignment, onClick: () -> Unit = {}) : Item(id, constraints, background) {

    constructor(id: String, height: Float, background: Background, closeButtonAlignment: ButtonAlignment, onClick: () -> Unit = {}) : this(
        id,
        ConstraintSet(
            CenterConstraint(ConstraintDirection.VERTICAL),
            PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
            RelativeConstraint(ConstraintDirection.HORIZONTAL, 1.0f),
            RelativeConstraint(ConstraintDirection.VERTICAL, height)
        ),
        background,
        closeButtonAlignment,
        onClick
    )

    constructor(id: String, titleBarData: TitleBarData, onClick: () -> Unit = {}) : this(id, titleBarData.height, titleBarData.background, titleBarData.closeButtonAlignment, onClick)

    private var closeButton: UIButton? = null

    init {
        if (closeButtonAlignment != ButtonAlignment.HIDDEN) {
            val closeButtonTexture = ImageMap(ImageCache.get("textures/userinterface/close_button.png"))
            val closeButtonBackground = TexturedBackground(closeButtonTexture, overlayColor = Color(1.0f, 1.0f, 1.0f, 1.0f))
//            val closeButtonBackground = SVGBackground("svg/close.svg", 0.1f, UIColor.WHITE.color, 0f, 0f, UIColor.WHITE.color)

            if (closeButtonAlignment == ButtonAlignment.RIGHT) {
                val buttonConstraints = ConstraintSet(
                    PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
                    PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f),
                    RelativeConstraint(ConstraintDirection.VERTICAL, 1.0f),
                    AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
                )

                closeButton = UIButton("close_button", buttonConstraints, closeButtonBackground, onClick)
            } else if (closeButtonAlignment == ButtonAlignment.LEFT) {
                val buttonConstraints = ConstraintSet(
                    PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
                    PixelConstraint(ConstraintDirection.TO_LEFT, 0.0f),
                    RelativeConstraint(ConstraintDirection.VERTICAL, 1.0f),
                    AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
                )

                closeButton = UIButton("close_button", buttonConstraints, closeButtonBackground, onClick)
            }

            add(closeButton!!)
        }
    }
}