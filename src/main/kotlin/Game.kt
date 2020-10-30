import devices.Key
import devices.Timer
import devices.Window
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.Sky
import math.Color
import math.vectors.Vector2
import org.lwjgl.opengl.GL11.*
import userinterface.*
import userinterface.animation.effects.ColorEffect
import userinterface.animation.effects.Effect
import userinterface.animation.effects.TranslationEffect
import userinterface.items.UIButton
import userinterface.items.backgrounds.ColoredBackground
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.PixelConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint
import userinterface.window.ButtonAlignment
import userinterface.window.UIWindow

fun main() {
    val window = Window("Game", ::onWindowResized)
    val keyboard = window.keyboard
    val mouse = window.mouse
    val timer = Timer()
    
    UniversalParameters.init(window.aspectRatio)
    
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

    optionsWindow.addButtonHoverEffects("close_button", ColorEffect(UniversalParameters.CLOSE_BUTTON_HOVERED_COLOR()))

    val buttonHoverEffects = arrayListOf<Effect>(
        TranslationEffect(0.05f, 0.0f),
        ColorEffect(UIColor.GREY_DARK)
    )
    
    val buttonClickEffects = arrayListOf(
        TranslationEffect(0.0f, -0.01f, 0.05f),
        ColorEffect(UIColor.GREY_DARK)
    )
    
    val buttonConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT),
        PixelConstraint(ConstraintDirection.TO_TOP),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.2f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.2f)
    )
    
    val buttonConstraints2 = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT),
        PixelConstraint(ConstraintDirection.TO_TOP),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.5f)
    )
    
    val button = UIButton("button", buttonConstraints).addHoverEffects(buttonHoverEffects).addClickEffects(buttonClickEffects)
//    val switch = Switch("switch", buttonConstraints2)
    
    val layout1 = UILayout("main")
    val layout2 = UILayout("second")
    layout1 += Pair("button", buttonConstraints)
    layout2 += Pair("button", buttonConstraints2)
    
//    val mainMenu = UIPage("main_menu")
    
    userInterface += optionsWindow
//    optionsWindow += switch
//    userInterface += mainMenu
    
    optionsWindow += button
    
//    userInterface.showPage("main_menu")
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

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        sky.render()
        
        if (userInterface.isShowing()) {
            userInterface.update(mouse, timer.getDelta())
            userInterface.draw(window.width, window.height)
        }
        
        if (keyboard.isPressed(Key.A)) {
            optionsWindow.apply(layout1, 0.2f)
//            ValueAnimation(0.2f, 1.0f, "X", test).apply(0.1f, button)
        }
        
        if (keyboard.isPressed(Key.S)) {
            optionsWindow.apply(layout2, 0.2f)
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