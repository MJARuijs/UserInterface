import devices.Key
import devices.Timer
import devices.Window
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.Point
import graphics.Sky
import graphics.shaders.ShaderProgram
import math.Color
import math.vectors.Vector2
import math.vectors.Vector3
import org.lwjgl.opengl.GL11.*
import userinterface.UIColor
import userinterface.UIPage
import userinterface.UniversalParameters
import userinterface.UserInterface
import userinterface.animation.animationtypes.ColorAnimationType
import userinterface.animation.effects.ColorEffect
import userinterface.animation.effects.TranslationEffect
import userinterface.items.*
import userinterface.items.backgrounds.ColorType
import userinterface.items.backgrounds.ColoredBackground
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.AspectRatioConstraint
import userinterface.layout.constraints.constrainttypes.CenterConstraint
import userinterface.layout.constraints.constrainttypes.PixelConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint
import userinterface.text.AlignmentType
import userinterface.text.TextAlignment
import userinterface.text.font.FontLoader
import userinterface.window.ButtonAlignment
import userinterface.window.TitleBarData
import userinterface.window.UIWindow

fun main() {
    val window = Window("Game", ::onWindowResized)
    val keyboard = window.keyboard
    val mouse = window.mouse
    val timer = Timer()
    
    GraphicsContext.init(Color(0.25f, 0.25f, 0.25f))
    GraphicsContext.enable(GraphicsOption.DEPTH_TESTING, GraphicsOption.FACE_CULLING, GraphicsOption.TEXTURE_MAPPING)

    val sky = Sky()
    UniversalParameters.init(window.aspectRatio, FontLoader(window.aspectRatio).load("fonts/arial_df.png"))

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
    
//    val optionsWindow = UIWindow(
//        "options_menu",
//        ConstraintSet(
//            CenterConstraint(ConstraintDirection.HORIZONTAL),
//            PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
//            RelativeConstraint(ConstraintDirection.VERTICAL, 0.9f),
//            RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.9f * 1.7f)
//        ),
//        windowBackground,
//        false,
//        TitleBarData(0.05f, titleBarBackground, ButtonAlignment.LEFT)
//    )

    optionsWindow.addButtonHoverEffects("close_button", ColorEffect(UniversalParameters.CLOSE_BUTTON_HOVERED_COLOR(), ColorType.BACKGROUND_COLOR, ColorAnimationType.ADD_TO_COLOR))

    val mainMenuTitleConstraints = ConstraintSet(
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.6f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.3f)
    )

    val loadLevelButtonConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.01f),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f, "main_menu_title"),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.2f),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.25f)
    )

    val newLevelButtonConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.01f),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.1f, "load_level_button"),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.2f),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.25f)
    )
    
    val optionsButtonConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.01f),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.1f, "new_level_button"),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.2f),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.25f)
    )
    
    val creditsButtonConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.01f),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.1f, "options_button"),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.2f),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.25f)
    )
    
    val quitButtonConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.01f),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.1f, "credits_button"),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.2f),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.25f)
    )
    
    val progressBarConstraints = ConstraintSet(
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.01f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.05f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 14.0f)
    )

    val mainMenuTitle = TextLabel("main_menu_title", mainMenuTitleConstraints, "Main Menu", 6f, TextAlignment(AlignmentType.CENTER), background = ColoredBackground(UIColor.TRANSPARENT))

    val buttonHoverEffects = arrayListOf(
        TranslationEffect(0.05f, 0.0f, 0.2f),
        ColorEffect(UIColor.GREY_DARK, ColorType.BACKGROUND_COLOR, ColorAnimationType.ADD_TO_COLOR)
    )

    val buttonClickEffects = arrayListOf(
        TranslationEffect(0.0f, -0.01f, 0.05f),
        ColorEffect(UIColor.GREY_DARK, ColorType.BACKGROUND_COLOR, ColorAnimationType.ADD_TO_COLOR)
    )
    
    val progressBar = ProgressBar("bar", progressBarConstraints)
    
    val leftTextAlignment = TextAlignment(AlignmentType.LEFT_ALIGNED, 0.04f)
//
//    val loadLevelButton = UIButton("load_level_button", loadLevelButtonConstraints)
//        .setText("Load Level", leftTextAlignment)
//        .addHoverEffects(buttonHoverEffects)
//        .addClickEffects(buttonClickEffects)
//        .setOnClick {
//            if (progressBar.isPaused()) {
//                progressBar.resume()
//            } else {
//                progressBar.pause()
//            }
//        }
//
//    val newLevelButton = UIButton("new_level_button", newLevelButtonConstraints)
//        .setText("New Level", leftTextAlignment)
//        .addHoverEffects(buttonHoverEffects)
//        .addClickEffects(buttonClickEffects)
//        .setOnClick {
//            println("Go to New-Level Menu")
//        }
//
//    val optionsButton = UIButton("options_button", optionsButtonConstraints)
//        .setText("Options", leftTextAlignment)
//        .addHoverEffects(buttonHoverEffects)
//        .addClickEffects(buttonClickEffects)
//        .setOnClick {
//            println("Go to options menu")
//        }
//
//    val creditsButton = UIButton("credits_button", creditsButtonConstraints)
//        .setText("Credits", leftTextAlignment)
//        .addHoverEffects(buttonHoverEffects)
//        .addClickEffects(buttonClickEffects)
//        .setOnClick {
//            println("Go to credits menu")
//        }
//
//    val quitButton = UIButton("quit_button", quitButtonConstraints)
//        .setText("Quit", leftTextAlignment)
//        .addHoverEffects(buttonHoverEffects)
//        .addClickEffects(buttonClickEffects)
//        .setOnClick {
//            window.close()
//        }

    val switchConstraints = ConstraintSet(
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        CenterConstraint(ConstraintDirection.VERTICAL),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.1f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 3.0f)
    )

    val checkBoxConstraints = ConstraintSet(
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.25f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.1f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
    )

    val scrollPaneConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT),
        PixelConstraint(ConstraintDirection.TO_TOP),
        RelativeConstraint(ConstraintDirection.VERTICAL, 1.0f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.2f)
    )

    val scrollPane = ScrollPane("scrollPane", scrollPaneConstraints, 2, 0.2f, 0.2f)
    for (i in 0 until 16) {
        scrollPane += Item("item$i", ConstraintSet(), ColoredBackground(UIColor.values()[i+4]))
    }

    val switch = Switch("switch", switchConstraints)
    val checkBox = CheckBox("checkBox", checkBoxConstraints)

    val textInputBox = TextInputBox("input_box", ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.02f),
        PixelConstraint(ConstraintDirection.TO_TOP),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.15f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 3.0f)
    ), "Preview", 6.0f)

    val mainMenu = UIPage("main_menu")
    mainMenu += textInputBox
//    mainMenu += mainMenuTitle
//    mainMenu += switch
//    mainMenu += progressBar
//    mainMenu += loadLevelButton
//    mainMenu += newLevelButton
//    mainMenu += optionsButton
//    mainMenu += creditsButton
//    mainMenu += quitButton
//    mainMenu += scrollPane
//    mainMenu += checkBox
    
    userInterface += optionsWindow
    userInterface += mainMenu

    userInterface.showPage("main_menu")
    
    timer.reset()
    mouse.release()
    
    val points = textInputBox.getPoints()
    val point = Point(points)
    glPointSize(6.0f)
    val pointProgram = ShaderProgram.load("shaders/point.vert", "shaders/point.frag")
    while (!window.isClosed()) {
        window.poll()
    
        if (keyboard.isPressed(Key.ESCAPE)) {
//            mouse.toggle()
//            if (userInterface.isShowing()) {
//                userInterface.hideWindows()
//            } else {
                userInterface.showWindow("options_menu")
//            }
        }
        
        if (keyboard.isPressed(Key.F1) || keyboard.isPressed(Key.KP1)) {
            window.close()
        }

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        sky.render()
        
        if (!progressBar.isPaused()) {
            progressBar.setProgress(progressBar.getProgress() + 0.001f)
        }
        
        if (userInterface.isShowing()) {
            userInterface.update(mouse, keyboard, timer.getDelta())
            userInterface.draw(window.width, window.height)
        }
        
        pointProgram.start()
        pointProgram.set("color", Vector3(1,0,0))
//        point.render()
        pointProgram.stop()
        
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