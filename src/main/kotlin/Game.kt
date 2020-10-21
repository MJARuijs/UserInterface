import devices.Key
import devices.Timer
import devices.Window
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.PointMesh
import graphics.Sky
import graphics.shaders.ShaderProgram
import math.Color
import math.vectors.Vector2
import org.lwjgl.opengl.GL11.*
import userinterface.UIColor
import userinterface.UniversalParameters
import userinterface.UserInterface
import userinterface.animation.effects.ColorEffect
import userinterface.items.*
import userinterface.items.backgrounds.ColorType
import userinterface.items.backgrounds.ColoredBackground
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.AspectRatioConstraint
import userinterface.layout.constraints.constrainttypes.CenterConstraint
import userinterface.layout.constraints.constrainttypes.PixelConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint
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

    val sky = Sky()

    val userInterface = UserInterface(window.aspectRatio)

    val windowBackground = ColoredBackground(Color(0.5f, 0.5f, 0.5f, 0.5f))
    val titleBarBackground = ColoredBackground(Color(0.25f, 0.25f, 0.25f, 0.75f))

    val optionsWindow = UIWindow(
        "options_menu",
        Vector2(1.7f, 0.9f),
        windowBackground,
        0.05f,
        titleBarBackground,
        ButtonAlignment.RIGHT
    )

    optionsWindow.addButtonHoverEffects("close_button", ColorEffect(UniversalParameters.CLOSE_BUTTON_HOVERED_COLOR(), ColorType.BACKGROUND_COLOR))
    
    val buttonConstraints = ConstraintSet(
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        CenterConstraint(ConstraintDirection.VERTICAL),
        RelativeConstraint(ConstraintDirection.VERTICAL, 1.0f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
    )
    
//    val button2Constraints = ConstraintSet(
//        PixelConstraint(ConstraintDirection.TO_RIGHT),
//        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f),
//        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.2f),
//        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.25f)
//    )

    val button2Constraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT),
        PixelConstraint(ConstraintDirection.TO_BOTTOM),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.25f)
    )

    val switchConstraint = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_TOP, 0.2f),
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.2f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.15f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 3.0f)
    )

    val progressBarConstraint = ConstraintSet(
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.15f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.05f)
    )

    val checkBoxConstraint = ConstraintSet(
        CenterConstraint(ConstraintDirection.VERTICAL),
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.1f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
    )
    
    val scrollConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_BOTTOM),
        PixelConstraint(ConstraintDirection.TO_LEFT),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.8f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.3f)
    )
    
    val scrollTitleConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.0f),
        PixelConstraint(ConstraintDirection.TO_TOP, 0.0f, "scroll_pane"),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.5f, "scroll_pane"),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.25f)
    )
    
    val dummies = ArrayList<Item>()
    val numberOfDummies = 10
    for (i in 0 until numberOfDummies) {
        dummies += Item("dummy$i", ConstraintSet(RelativeConstraint(ConstraintDirection.VERTICAL, 0.25f)), ColoredBackground(UIColor.values()[i + 4]))
    }

    val testButton = UIButton("testButton", buttonConstraints, {
        println("Button 2 clicked!")
    })
    val arialFont = FontLoader(window.aspectRatio).load("fonts/arial.png")

//    testButton.addOnHoverAnimation(ColorEffect(UIColor.RED.color, ColorType.BACKGROUND_COLOR))
//    testButton.addOnHoverAnimation(TranslationEffect(0.05f, 0f))
    
    val testButton2 = UIButton("testButton2", button2Constraints, {
        println("Button 2 clicked!")
    })
    testButton2.setText("Play", UIColor.WHITE, arialFont)

    val switch = Switch("switch", switchConstraint, false, { newState ->
        println("State changed to $newState")
    })
    
    val progressBar = ProgressBar("progress_bar", progressBarConstraint)
    
    val checkBox = CheckBox("check_box", checkBoxConstraint, true, { checked ->
        println("CheckBox checked: $checked")
    })
    
    val textBox = TextBox("text", scrollTitleConstraints, "Items", UIColor.WHITE, arialFont, 1.77f, ColoredBackground(UIColor.GREY_DARK))
    
    val scrollPane = ScrollPane("scroll_pane", scrollConstraints, 2, 0.01f)
//    for (dummy in dummies) {
//        scrollPane += dummy
//    }
//    scrollPane += textBox

    optionsWindow += scrollPane
//    optionsWindow += checkBox
//    optionsWindow += progressBar
//    optionsWindow += testButton
    optionsWindow += testButton2
    optionsWindow += textBox
//    optionsWindow += switch
    userInterface += optionsWindow
    
    userInterface.showWindow("options_menu")
    timer.reset()
    mouse.release()
    
    while (!window.isClosed()) {
        window.poll()
    
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
        
        if (userInterface.isShowing()) {
            userInterface.update(mouse, timer.getDelta())
            userInterface.draw(window.width, window.height)
        }
        
        if (!progressBar.isPaused()) {
//            progressBar.setProgress(progressBar.getProgress() + 0.01f)
        }
        
        window.synchronize()
        timer.update()
    }
    
    sky.destroy()
    userInterface.destroy()
    window.destroy()
}

fun onWindowResized(width: Int, height: Int) {
    glViewport(0, 0, width, height)
}