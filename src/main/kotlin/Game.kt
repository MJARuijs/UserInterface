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
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.AspectRatioConstraint
import userinterface.layout.constraints.constrainttypes.PixelConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint
import userinterface.effects.ColorEffect
import userinterface.items.UIButton
import userinterface.items.backgrounds.ColoredBackground
import userinterface.layout.UILayout
import userinterface.layout.constraints.constrainttypes.CenterConstraint
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
    val titleBarBackground = ColoredBackground(Color(0.25f, 0.25f, 0.25f, 0.25f))
    
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
    
    val buttonBackground = ColoredBackground(Color(0.0f, 0.0f, 1.0f), 0f, 0.00f, Color(1f, 1.0f, 1.0f))
    val buttonBackground2 = ColoredBackground(Color(1.0f, 1.0f, 0.0f), 0f, 0.00f, Color(1f, 1.0f, 1.0f))
    val buttonBackground3 = ColoredBackground(Color(0.0f, 1.0f, 0.0f), 0f, 0.00f, Color(1f, 1.0f, 1.0f))
    val buttonBackground4 = ColoredBackground(Color(1.0f, 0.0f, 0.0f), 0f, 0.00f, Color(1f, 1.0f, 1.0f))
    val buttonBackground5 = ColoredBackground(Color(1.0f, 0.0f, 1.0f), 0f, 0.00f, Color(1f, 1.0f, 1.0f))
    
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
        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f, "testButton3"),
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
//    val button1Constraints3 = ConstraintSet(
//        PixelConstraint(ConstraintDirection.TO_RIGHT),
//        PixelConstraint(ConstraintDirection.TO_BOTTOM),
//        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
//        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
//    )
//
//    val button2Constraints3 = ConstraintSet(
//        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f),
//        PixelConstraint(ConstraintDirection.TO_TOP, 0.0f),
//        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
//        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1f)
//    )
//
//    val button3Constraints3 = ConstraintSet(
//        PixelConstraint(ConstraintDirection.TO_RIGHT, 0.0f),
//        PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.0f),
//        RelativeConstraint(ConstraintDirection.VERTICAL, 0.5f),
//        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 0.5f)
//    )
    
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
    
    testButton5 += testButton4
    testButton5 += testButton3
    testButton5 += testButton2
    testButton += testButton5
    optionsWindow += testButton
    
    userInterface += optionsWindow

    val textProgram = ShaderProgram.load("shaders/text.vert", "shaders/text.frag")
    val arialFont = FontLoader(window.aspectRatio).load("fonts/arial.png")
    val text = Text("Hoi lieverd :)", 5.0f, arialFont)
    
    val standardLayout = UILayout("standard_layout")
    val animatedLayout = UILayout("animated_layout")
//    val thirdLayout = UILayout("third_layout")

    standardLayout += Pair(testButton.id, button1Constraints)
    standardLayout += Pair(testButton2.id, button2Constraints)
    standardLayout += Pair(testButton3.id, button3Constraints)
    standardLayout += Pair(testButton4.id, button4Constraints)
    standardLayout += Pair(testButton5.id, button5Constraints)
    
    animatedLayout += Pair(testButton.id, button1Constraints2)
    animatedLayout += Pair(testButton2.id, button2Constraints2)
    animatedLayout += Pair(testButton3.id, button3Constraints2)
    animatedLayout += Pair(testButton4.id, button4Constraints2)
    animatedLayout += Pair(testButton5.id, button5Constraints2)



//    thirdLayout += Pair(testButton.id, button1Constraints3)
//    thirdLayout += Pair(testButton2.id, button2Constraints3)
//    thirdLayout += Pair(testButton3.id, button3Constraints3)
    
    optionsWindow += standardLayout
    optionsWindow += animatedLayout
//    optionsWindow += thirdLayout
    
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
        
        if (keyboard.isPressed(Key.F1)) {
            window.close()
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