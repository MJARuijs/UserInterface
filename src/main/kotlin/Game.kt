import devices.Key
import devices.Timer
import devices.Window
import entities.Entity
import entities.EntityRenderer
import environment.sky.Sky
import graphics.Camera
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.lights.AmbientLight
import graphics.lights.DirectionalLight
import graphics.models.ModelCache
import graphics.shaders.ShaderProgram
import math.Color
import math.matrices.Matrix4
import math.vectors.Vector2
import math.vectors.Vector3
import org.lwjgl.opengl.GL11.*
import userinterface.UIColor
import userinterface.UserInterface
import userinterface.effects.ColorEffect
import userinterface.items.Switch
import userinterface.items.UIButton
import userinterface.items.backgrounds.ColorType
import userinterface.items.backgrounds.ColoredBackground
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.AspectRatioConstraint
import userinterface.layout.constraints.constrainttypes.CenterConstraint
import userinterface.layout.constraints.constrainttypes.PixelConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint
import userinterface.svg.SVGIcon
import userinterface.svg.SVGLoader
import userinterface.text.Text
import userinterface.text.font.FontLoader
import userinterface.window.ButtonAlignment
import userinterface.window.UIWindow


fun main() {
    val window = Window("Game", ::onWindowResized)
    
    val keyboard = window.keyboard
    val mouse = window.mouse
    val timer = Timer()
    
    GraphicsContext.init(Color(0.25f, 0.25f, 0.25f))
    GraphicsContext.enable(GraphicsOption.DEPTH_TESTING, GraphicsOption.FACE_CULLING, GraphicsOption.TEXTURE_MAPPING)

    val duckEntity = Entity(Matrix4(), ModelCache.get("models/duck.dae"))
    val entities = ArrayList<Entity>()
    entities += duckEntity

    val camera = Camera(window.aspectRatio)
    val ambientLight = AmbientLight(Color(0.75f, 0.75f, 0.75f))
    val directionalLight = DirectionalLight(Color(0.75f, 0.75f, 0.75f), Vector3(1.0f, 1.0f, 1.0f))

    val sky = Sky()

    val userInterface = UserInterface(window.aspectRatio)

    val windowBackground = ColoredBackground(Color(0.5f, 0.5f, 0.5f, 0.5f))
    val titleBarBackground = ColoredBackground(Color(0.25f, 0.25f, 0.25f, 0.75f))

    val optionsWindow = UIWindow(
        "options_menu",
        Vector2(0.9f * window.aspectRatio, 0.9f),
        windowBackground,
        0.05f,
        titleBarBackground,
        ButtonAlignment.RIGHT
    )
    optionsWindow.addButtonHoverEffects(
        "close_button",
        ColorEffect(Color(0.0f, 0.0f, 0.0f, 0.0f), Color(0.5f, 0.5f, 0.5f))
    )
    optionsWindow.addButtonOnClickEffects(
        "close_button",
        ColorEffect(Color(0.0f, 0.0f, 0.0f, 0.0f), Color(0.0f, 0.1f, 0.5f))
    )

//    val buttonBackground = SVGBackground("svg/close.svg", 0.01f, UIColor.WHITE.color, 0f, 0f, UIColor.BLACK.color)
    val buttonBackground = ColoredBackground(UIColor.GREEN_DARK, 0f, 0.0f, Color(33, 73, 107))
    val buttonBackground2 = ColoredBackground(UIColor.CYAN, 0f, 0.00f, Color(1f, 1.0f, 1.0f))
    val buttonBackground3 = ColoredBackground(UIColor.GREEN, 0f, 0.00f, Color(1f, 1.0f, 1.0f))
    val buttonBackground4 = ColoredBackground(UIColor.RED, 0f, 0.00f, Color(1f, 1.0f, 1.0f))
    val buttonBackground5 = ColoredBackground(UIColor.YELLOW_LIGHT, 0f, 0.00f, Color(1f, 1.0f, 1.0f))
//    val switchOnTrackBackground = ColoredBackground(UIColor.TRANSPARENT, 90f, 0.09f, Color(33, 73, 107))
//    val switchOnThumbBackground = ColoredBackground(Color(65, 162, 239), 90f)
//    val switchOnThumbBackground = ColoredBackground(UIColor.BLUE_BRIGHT, 90f)

//    val switchOffTrackBackground = ColoredBackground(UIColor.TRANSPARENT, 90f, 0.09f, Color(74, 74, 74))
//    val switchOffThumbBackground = ColoredBackground(Color(185, 185, 185), 90f)
//    val switchOffThumbBackground = ColoredBackground(UIColor.WHITE, 90f)

    val button1Constraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0f),
        PixelConstraint(ConstraintDirection.TO_TOP, 0f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 1.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 1.0f)
    )

    val button2Constraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.0f),
        PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1f)
    )

    val button3Constraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f, "testButton2"),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 0.5f)
    )

    val button4Constraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f, "testButton3"),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 0.5f)
    )

    val button5Constraints = ConstraintSet(
        CenterConstraint(ConstraintDirection.VERTICAL),
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.95f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.95f)
    )

    val button1Constraints2 = ConstraintSet(
        CenterConstraint(ConstraintDirection.VERTICAL),
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.5f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f)
    )

    val button2Constraints2 = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f),
        PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.6f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1f)
    )

    val button3Constraints2 = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f, "testButton4"),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 0.5f)
    )

    val button4Constraints2 = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.0f),
        PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 0.5f)
    )

    val button5Constraints2 = ConstraintSet(
        CenterConstraint(ConstraintDirection.VERTICAL),
        PixelConstraint(ConstraintDirection.TO_RIGHT),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.7f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.95f)
    )

    val switchConstraint = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_TOP, 0.2f),
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.2f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.15f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 3.0f)
    )

    val switchConstraint2 = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_TOP, 0.2f),
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.2f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.15f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 3.0f)
    )

    val testButton = UIButton("testButton1", button1Constraints, buttonBackground, {
        println("Button 1 Clicked")
    }).addOnClickEffect(ColorEffect(Color(0.0f, 0.0f, 0.75f)))

    val testButton2 = UIButton("testButton2", button2Constraints, buttonBackground2, {
        println("Button 2 clicked!")
    })

    val testButton3 = UIButton("testButton3", button3Constraints, buttonBackground3, {
        println("Button 3 clicked!")
    })

    val testButton4 = UIButton("testButton4", button4Constraints, buttonBackground4, {
        println("Button 4 clicked!")
    })

    val testButton5 = UIButton("testButton5", button5Constraints, buttonBackground5, {
        println("Button 5 clicked!")
    })

    val switch = Switch("switch", switchConstraint, false, { newState ->
        println("State changed to $newState")
    })
    val svgLoader = SVGLoader()
    
    val checkIcon = svgLoader.load("svg/tick.svg")
    
    testButton.icon = SVGIcon(checkIcon)
    
//    testButton5 += testButton4
//    testButton5 += testButton3
//    testButton5 += testButton2
//    testButton += testButton5
    optionsWindow += testButton
//    optionsWindow += switch
    userInterface += optionsWindow

    val textProgram = ShaderProgram.load("shaders/text.vert", "shaders/text.frag")
    val arialFont = FontLoader(window.aspectRatio).load("fonts/arial.png")
    val text = Text("Hoi lieverd :)", 5.0f, arialFont)

    val standardLayout = UILayout("standard_layout")
    val animatedLayout = UILayout("animated_layout")
//    val thirdLayout = UILayout("third_layout")

    standardLayout += Triple(testButton.id, button1Constraints, Pair(UIColor.RED_BRIGHT, ColorType.BACKGROUND_COLOR))
//    standardLayout += Pair(testButton2.id, button2Constraints)
    standardLayout += Pair(testButton3.id, button3Constraints)
    standardLayout += Pair(testButton4.id, button4Constraints)
    standardLayout += Pair(testButton5.id, button5Constraints)
    standardLayout += Pair(switch.id, switchConstraint)

    animatedLayout += Pair(testButton.id, button1Constraints2)
//    animatedLayout += Pair(testButton2.id, button2Constraints)
    animatedLayout += Pair(testButton3.id, button3Constraints2)
    animatedLayout += Pair(testButton4.id, button4Constraints2)
    animatedLayout += Pair(testButton5.id, button5Constraints2)
    animatedLayout += Pair(switch.id, switchConstraint2)

    optionsWindow += standardLayout
    optionsWindow += animatedLayout
//    optionsWindow += thirdLayout

//    val svgFile = parser.load("svg/check-mark-black-outline.svg")
    
    val closeIcon = svgLoader.load("svg/close.svg", 0.1f)

    userInterface.showWindow("options_menu")
    timer.reset()
    mouse.release()
    while (!window.isClosed()) {
    
        if (keyboard.isPressed(Key.ESCAPE)) {
            mouse.toggle()
            if (userInterface.isShowing()) {
                userInterface.hideWindows()
            } else {
                userInterface.showWindow("options_menu")
            }
        }
        
        if (keyboard.isPressed(Key.F1) || keyboard.isPressed(Key.KP1)) {
            window.close()
        }
        
        if (keyboard.isPressed(Key.F)) {
            switch.turnOn()
        }

        if (keyboard.isPressed(Key.A)) {
            optionsWindow.applyLayout("animated_layout", 0.5f)
        }

        if (keyboard.isPressed(Key.S)) {
            optionsWindow.applyLayout("standard_layout", 0.5f)
        }

        if (keyboard.isPressed(Key.D)) {
            optionsWindow.applyLayout("third_layout", 0.5f)
        }

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        sky.render()
        
//        EntityRenderer.render(camera, entities, ambientLight, directionalLight)
    
        if (userInterface.isShowing()) {
            userInterface.update(mouse, timer.getDelta())
            userInterface.draw(window.width, window.height)
//            checkIcon.draw()
//            closeIcon.draw()
        }
    
        if (mouse.captured) {
            camera.update(window.keyboard, window.mouse, timer.getDelta())
        }
//        text.render(textProgram)
        
        window.synchronize()
        window.poll()
        timer.update()
    }
    
    sky.destroy()
    userInterface.destroy()
    EntityRenderer.destroy()
    window.destroy()
}

fun onWindowResized(width: Int, height: Int) {
    glViewport(0, 0, width, height)
}