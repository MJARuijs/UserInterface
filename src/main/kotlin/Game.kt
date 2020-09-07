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
import userinterface.animation.TransitionAnimation
import userinterface.constraints.*
import userinterface.effects.ColorEffect
import userinterface.items.Button
import userinterface.items.Switch
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

    val optionsWindow = UIWindow("options_menu", Vector2(0.9f * window.aspectRatio, 0.9f), windowBackground, 0.05f, titleBarBackground, ButtonAlignment.RIGHT)
    optionsWindow.addButtonHoverEffects("close_button", ColorEffect(Color(0.0f, 0.0f, 0.0f, 0.0f), Color(0.5f, 0.5f, 0.5f)))
    optionsWindow.addButtonOnClickEffects("close_button", ColorEffect(Color(0.0f, 0.0f, 0.0f, 0.0f), Color(0.0f, 0.1f, 0.5f)))

    val buttonBackground = ColoredBackground(Color(0.0f, 0.0f, 1.0f), 0f, 0.05f, Color(1f, 1.0f, 1.0f))
    val buttonBackground2 = ColoredBackground(Color(0.0f, 0.0f, 1.0f, 0f), 0.5f, 0.02f, Color(1f, 1.0f, 1.0f, 0.5f))
    val buttonBackground3 = ColoredBackground(Color(0.0f, 0.0f, 1.0f), 40f, 0.02f, Color(1f, 1.0f, 1.0f))

    val testButton = Button("testButton1",
        ConstraintSet(
            PixelConstraint(ConstraintDirection.TO_LEFT, 0f),
            PixelConstraint(ConstraintDirection.TO_TOP, 0f),
            RelativeConstraint(ConstraintDirection.VERTICAL, 0.1f),
            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 3f)
        ),
        buttonBackground,
        {
            println("TestButton1 Clicked")
        }
    )

    val testButton2 = Button("testButton2",
        ConstraintSet(
            PixelConstraint(ConstraintDirection.TO_LEFT, 0f),
            PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.2f, "testButton1"),
            RelativeConstraint(ConstraintDirection.VERTICAL, 0.1f),
            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
        ),
        buttonBackground,
        {
            println("TestButton2 Clicked")
        }
    )

    val testButton3 = Button("testButton3",
        ConstraintSet(
            PixelConstraint(ConstraintDirection.TO_LEFT, 0f),
            PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.2f, "testButton2"),
            RelativeConstraint(ConstraintDirection.VERTICAL, 0.1f),
            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
        ),
        buttonBackground,
        {
            println("TestButton3 Clicked")
        }
    )

    val testButton4 = Button("testButton4",
        ConstraintSet(
            PixelConstraint(ConstraintDirection.TO_RIGHT, 0.2f, "testButton1"),
            PixelConstraint(ConstraintDirection.TO_BOTTOM, 0.2f, "testButton2"),
            RelativeConstraint(ConstraintDirection.VERTICAL, 0.1f),
            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
        ),
        buttonBackground,
        {
            println("TestButton4 Clicked")
        }
    )
    val switch = Switch("test_switch",
        ConstraintSet(
            CenterConstraint(ConstraintDirection.HORIZONTAL),
            CenterConstraint(ConstraintDirection.VERTICAL),
            RelativeConstraint(ConstraintDirection.VERTICAL, 0.25f),
            AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 2f)
        ),
        ColoredBackground(Color(0.0f, 0.0f, 0.0f, 0.0f), 90.0f, 0.05f, Color(37, 82, 118)),
        ColoredBackground(Color(0.25f, 0.64f, 0.94f, 0.5f), 90.0f, 0.00f)
    )


    optionsWindow += testButton2
    optionsWindow += testButton4

    optionsWindow += testButton3

    optionsWindow += testButton
    userInterface += optionsWindow
    userInterface.init()

    val textProgram = ShaderProgram.load("shaders/text.vert", "shaders/text.frag")
    val arialFont = FontLoader(window.aspectRatio).load("fonts/arial.png")
    val text = Text("Hoi lieverd :)", 5.0f, arialFont)

    timer.reset()

    mouse.release()
    val uiProgram = ShaderProgram.load("shaders/ui.vert", "shaders/ui.frag")

    var enableWireFrame = false

    var increasingRadius = false
    userInterface.showWindow("options_menu")
//    testButton.init(Vector2(), Vector2(0.5f, 0.5f))

    while (!window.isClosed()) {
        if (keyboard.isPressed(Key.ESCAPE)) {
            mouse.toggle()
        }

        if (keyboard.isPressed(Key.F1)) {
            window.close()
        }

        if (keyboard.isPressed(Key.F)) {
            enableWireFrame = !enableWireFrame
        }

        if (keyboard.isPressed(Key.A)) {
            userInterface += TransitionAnimation(1.0f, Vector2(1.0f, 0.0f), testButton)
        }

        if (increasingRadius) {
//            buttonBackground2.cornerRadius += timer.getDelta() * 25f
//            buttonBackground.cornerRadius = buttonBackground2.cornerRadius
//            buttonBackground3.cornerRadius = buttonBackground2.cornerRadius
            if (buttonBackground2.cornerRadius >= 90.0f) {
                increasingRadius = false
            }
        } else {
//            buttonBackground2.cornerRadius -= timer.getDelta() * 25f
//            buttonBackground.cornerRadius = buttonBackground2.cornerRadius
//            buttonBackground3.cornerRadius = buttonBackground2.cornerRadius
            if (buttonBackground2.cornerRadius <= 0.0f) {
                increasingRadius = true
            }
        }

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        sky.render()

        EntityRenderer.render(camera, entities, ambientLight, directionalLight)
//        testButton.update(mouse, window.aspectRatio)
//        testButton.draw(uiProgram)
        if (mouse.captured) {
            camera.update(window.keyboard, window.mouse, timer.getDelta())
        } else {
            if (enableWireFrame) {
                glLineWidth(5.0f)
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
            } else {
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            }
            userInterface.update(mouse, timer.getDelta())
            userInterface.draw(window.width, window.height)
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