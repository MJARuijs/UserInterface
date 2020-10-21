package userinterface.itemsimport devices.Mouseimport math.vectors.Vector2import userinterface.MovableUIContainerimport userinterface.UniversalParametersimport userinterface.items.backgrounds.Backgroundimport userinterface.layout.constraints.ConstraintDirectionimport userinterface.layout.constraints.ConstraintSetimport userinterface.layout.constraints.constrainttypes.Constraintimport userinterface.layout.constraints.constrainttypes.PixelConstraintimport userinterface.layout.constraints.constrainttypes.RelativeConstraintimport util.FloatUtilsimport kotlin.math.absimport kotlin.math.signclass ScrollPane(id: String, constraints: ConstraintSet, private val numberOfAdjacentItems: Int, private var boundarySize: Float, background: Background = UniversalParameters.SCROLL_PANE_BACKGROUND()) : Item(id, constraints, background) {    private val scrollSpeed = 2.5f        override operator fun plusAssign(item: Item) {        children += item    }        override fun position(parent: MovableUIContainer?, duration: Float) {        super.position(parent, duration)                val items = ArrayList<Item>()        items.addAll(children)        children.clear()                for (item in items) {            val itemPlace = children.size % numberOfAdjacentItems            val itemConstraints = ArrayList<Constraint>()                val itemWidth = getScale().x / (boundarySize * (numberOfAdjacentItems - 1) + numberOfAdjacentItems)            val horizontalPadding = itemWidth * boundarySize * 2            val verticalPadding = getScale().y / (boundarySize * (numberOfAdjacentItems - 1) + numberOfAdjacentItems)                    itemConstraints += if (children.size < numberOfAdjacentItems) {                PixelConstraint(ConstraintDirection.TO_TOP)            } else {                val topNeighborId = children[children.size - numberOfAdjacentItems].id                PixelConstraint(ConstraintDirection.TO_BOTTOM, horizontalPadding / verticalPadding, topNeighborId)            }                itemConstraints += when (itemPlace) {                0 -> PixelConstraint(ConstraintDirection.TO_LEFT)                else -> {                    val leftNeighborId = children[itemPlace - 1].id                    PixelConstraint(ConstraintDirection.TO_RIGHT, horizontalPadding, leftNeighborId)                }            }            itemConstraints += RelativeConstraint(ConstraintDirection.HORIZONTAL, itemWidth, relativePercentage = false)            item.constraints.add(itemConstraints, this)            children += item        }    }        override fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {        val mouseScroll = mouse.yScroll * deltaTime * scrollSpeed                if (mouseScroll == 0f) {            return super.update(mouse, aspectRatio, deltaTime)        }                val delta = FloatUtils.roundToDecimal(mouseScroll, 4)                val topChildPosition = children.first().getTranslation().y + children.first().getScale().y        val scrollPaneTop = getTranslation().y + getScale().y               val bottomChildPosition = children.last().getTranslation().y - children.last().getScale().y        val scrollPaneBottom = getTranslation().y - getScale().y            if (mouseScroll < 0f) {            repositionChildren(topChildPosition, scrollPaneTop, delta)        } else {            repositionChildren(bottomChildPosition, scrollPaneBottom, delta)        }        return super.update(mouse, aspectRatio, deltaTime)    }        private fun repositionChildren(childBoundary: Float, scrollPaneBoundary: Float, delta: Float) {        if (abs(childBoundary - scrollPaneBoundary) < abs(delta)) {            for (child in children) {                child.translate(Vector2(0f, sign(delta) * abs(childBoundary - scrollPaneBoundary)))            }        } else {            for (child in children) {                child.translate(Vector2(0f, delta))            }        }    }}