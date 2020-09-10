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
import userinterface.UserInterface
import userinterface.animation.TransitionType
import userinterface.animation.XTransitionAnimation
import userinterface.constraints.ConstraintDirection
import userinterface.constraints.ConstraintSet
import userinterface.constraints.constrainttypes.AspectRatioConstraint
import userinterface.constraints.constrainttypes.CenterConstraint
import userinterface.constraints.constrainttypes.PixelConstraint
import userinterface.constraints.constrainttypes.RelativeConstraint
import userinterface.effects.ColorEffect
import userinterface.items.Switch
import userinterface.items.UIButton
import userinterface.items.backgrounds.ColoredBackground
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
    
    val buttonBackground = ColoredBackground(Color(0.0f, 0.0f, 1.0f), 0f, 0.05f, Color(1f, 1.0f, 1.0f))
    val buttonBackground2 = ColoredBackground(Color(0.0f, 1.0f, 0.0f), 0f, 0.05f, Color(1f, 1.0f, 1.0f))
    val buttonBackground3 = ColoredBackground(Color(1.0f, 0.0f, 0.0f), 0f, 0.05f, Color(1f, 1.0f, 1.0f))
    val buttonBackground4 = ColoredBackground(Color(1.0f, 1.0f, 0.0f), 0f, 0.05f, Color(1f, 1.0f, 1.0f))
    val onClickBackground = ColoredBackground(Color(0.0f, 0.0f, 0.75f), 0f, 0.05f, Color(1f, 1.0f, 1.0f))
    
    val button1Constraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0f),
        PixelConstraint(ConstraintDirection.TO_TOP, 0f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.3f)
    )
    
    val testButton = UIButton("testButton1",
                              button1Constraints,
                              buttonBackground,
                              {
                                  println("TestButton1 Clicked")
                              }
    )
    testButton.addOnClickEffect(ColorEffect(Color(0.0f, 0.0f, 0.75f)))
    
    val testButton2 = UIButton("testButton2",
                               ConstraintSet(
                                   PixelConstraint(ConstraintDirection.TO_LEFT, 0f),
                                   PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f, "testButton3"),
                                   RelativeConstraint(ConstraintDirection.VERTICAL, 0.3f),
                                   AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
                               ),
                               buttonBackground2,
                               {
                                   println("TestButton2 Clicked")
                               }
    )
    
    val testButton3 = UIButton("testButton3",
                               ConstraintSet(
                                   PixelConstraint(ConstraintDirection.TO_LEFT, 0f),
                                   PixelConstraint(ConstraintDirection.TO_TOP, 0f),
                                   RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
                                   AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
                               ),
                               buttonBackground3,
                               {
                                   println("TestButton3 Clicked")
                               }
    )
    
    val testButton4 = UIButton("testButton4",
                               ConstraintSet(
                                   PixelConstraint(ConstraintDirection.TO_LEFT, 0.0f),
                                   PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f),
                                   RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
                                   AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
                               ), buttonBackground4,
                               {
                                   println("Hoi")
                               }
    )
    
    val switch = Switch(
        "test_switch",
        ConstraintSet(
            CenterConstraint(ConstraintDirection.HORIZONTAL),
            CenterConstraint(ConstraintDirection.VERTICAL),
            RelativeConstraint(ConstraintDirection.VERTICAL, 0.25f),
            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
        ),
        ColoredBackground(Color(0.0f, 0.0f, 0.0f, 0.0f), 90.0f, 0.05f, Color(37, 82, 118)),
        ColoredBackground(Color(0.25f, 0.64f, 0.94f, 0.5f), 90.0f, 0.00f)
    )
    
//    testButton4 += testButton3
//    testButton4 += testButton2
    testButton += testButton4
    optionsWindow += testButton
    
    userInterface += optionsWindow

    val textProgram = ShaderProgram.load("shaders/text.vert", "shaders/text.frag")
    val arialFont = FontLoader(window.aspectRatio).load("fonts/arial.png")
    val text = Text("Hoi lieverd :)", 5.0f, arialFont)
    
    timer.reset()
    
    mouse.release()
    
    userInterface.showWindow("options_menu")
    
//    val button1Constraints2 = ConstraintSet(
//        PixelConstraint(ConstraintDirection.TO_RIGHT, 0f),
//        PixelConstraint(ConstraintDirection.TO_TOP, 0f),
//        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.5f),
//        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.3f)
//    )
    
    val button1Constraints2 = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0f),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0f),
        RelativeConstraint(ConstraintDirection.HORIZONTAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.VERTICAL, 0.3f)
    )
    
    val button4Constraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT, 0.0f),
        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
    )
    
    val button4Constraints2 = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f),
        PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
    )
    
    val c1 = button1Constraints.computeResult(optionsWindow)
    val c2 = button1Constraints2.computeResult(optionsWindow)
    
    val c3 = button4Constraints.computeResult(testButton)
    val c4 = button4Constraints2.computeResult(testButton)
    
    val button1Animation1 = XTransitionAnimation(0.5f, c2.translation.x, testButton, TransitionType.PLACEMENT)
    
    while (!window.isClosed()) {
        if (keyboard.isPressed(Key.ESCAPE)) {
            mouse.toggle()
            if (userInterface.isShowing()) {
                userInterface.hideWindows()
            } else {
                userInterface.showWindow("options_menu")
            }
        }
        
        if (keyboard.isPressed(Key.F1)) {
            window.close()
        }
        
        if (keyboard.isPressed(Key.A)) {
            button1Animation1.changeGoal(c2.translation.x)
            userInterface += button1Animation1
//            userInterface += XTransitionAnimation(1.5f, c2.translation.x, testButton, TransitionType.PLACEMENT)
//            userInterface += YTransitionAnimation(0.5f, c2.translation.y, testButton, TransitionType.PLACEMENT)
        }
    
        if (keyboard.isPressed(Key.S)) {
            button1Animation1.changeGoal(c1.translation.x)
//            userInterface += XTransitionAnimation(0.5f, c1.translation.x, testButton, TransitionType.PLACEMENT)
//            userInterface += XTransitionAnimation(0.5f, c3.translation.x, testButton4, TransitionType.PLACEMENT)
//            userInterface += YTransitionAnimation(0.5f, c1.translation.y, testButton, TransitionType.PLACEMENT)
        }
        
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        sky.render()
        
        EntityRenderer.render(camera, entities, ambientLight, directionalLight)
        
        if (userInterface.isShowing()) {
            userInterface.update(mouse, timer.getDelta())
            userInterface.draw(window.width, window.height)
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