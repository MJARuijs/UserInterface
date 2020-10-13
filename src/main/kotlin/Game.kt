import devices.Key
import devices.Timer
import devices.Window
import graphics.Sky
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.shaders.ShaderProgram
import math.Color
import math.vectors.Vector2
import org.lwjgl.opengl.GL11.*
import userinterface.UserInterface
import userinterface.effects.ColorEffect
import userinterface.items.ProgressBar
import userinterface.items.Switch
import userinterface.items.UIButton
import userinterface.items.backgrounds.ColoredBackground
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

    val buttonConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT),
        PixelConstraint(ConstraintDirection.TO_TOP),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.1f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1f)
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

    val testButton = UIButton("testButton", buttonConstraints, {
        println("Button 2 clicked!")
    })

    val switch = Switch("switch", switchConstraint, false, { newState ->
        println("State changed to $newState")
    })
    
    val progressBar = ProgressBar("progress_bar", progressBarConstraint)
    
    val svgLoader = SVGLoader()
    
    val checkIcon = svgLoader.load("svg/tick.svg")
    testButton.setIcon(SVGIcon(checkIcon, 0.1f))
    
    optionsWindow += progressBar
    userInterface += optionsWindow

    val textProgram = ShaderProgram.load("shaders/text.vert", "shaders/text.frag")
    val arialFont = FontLoader(window.aspectRatio).load("fonts/arial.png")
    val text = Text("Hoi lieverd :)", 5.0f, arialFont)

//    val svgFile = parser.load("svg/check-mark-black-outline.svg")

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
        
        if (userInterface.isShowing()) {
            userInterface.update(mouse, timer.getDelta())
            userInterface.draw(window.width, window.height)
//            checkIcon.draw()
//            closeIcon.draw()
        }
        
        if (!progressBar.isPaused()) {
            progressBar.setProgress(progressBar.getProgress() + 0.01f)
        }
        
//        text.render(textProgram)
        
        window.synchronize()
        window.poll()
        timer.update()
    }
    
    sky.destroy()
    userInterface.destroy()
    window.destroy()
}

fun onWindowResized(width: Int, height: Int) {
    glViewport(0, 0, width, height)
}