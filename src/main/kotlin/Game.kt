import graphics.models.ModelCache
import graphics.GraphicsOption
import math.Color
import devices.Key
import devices.Timer
import devices.Window
import entities.Entity
import entities.EntityRenderer
import graphics.Camera
import graphics.GraphicsContext
import graphics.lights.AmbientLight
import graphics.lights.DirectionalLight
import environment.sky.Sky
import graphics.textures.ImageMap
import math.matrices.Matrix4
import math.vectors.Vector3
import org.lwjgl.opengl.GL11.*
import resources.images.ImageCache
import userinterface.UserInterface
import userinterface.constraints.*
import userinterface.effects.ColorEffect
import userinterface.items.*
import userinterface.items.backgrounds.ColoredBackground
import userinterface.items.backgrounds.TexturedBackground

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

    val constraintSet = ConstraintSet(
        CenterConstraint(ConstraintDirection.HORIZONTAL),
        CenterConstraint(ConstraintDirection.VERTICAL),
        RelativeConstraint(0.9f, ConstraintDirection.HORIZONTAL),
        RelativeConstraint(0.9f, ConstraintDirection.VERTICAL)
    )

    val constraintSet2 = ConstraintSet(
        CenterConstraint(ConstraintDirection.VERTICAL),
        PixelConstraint(0.0f, ConstraintDirection.TO_TOP),
        RelativeConstraint(1.0f, ConstraintDirection.HORIZONTAL),
        RelativeConstraint(0.05f, ConstraintDirection.VERTICAL)
    )

    val constraintSet3 = ConstraintSet(
        PixelConstraint(0.0f, ConstraintDirection.TO_TOP),
        PixelConstraint(0.0f, ConstraintDirection.TO_RIGHT),
        RelativeConstraint(1.0f, ConstraintDirection.VERTICAL),
        AspectRatioConstraint(1.0f, ConstraintDirection.HORIZONTAL)
    )

    val closeButtonTexture = ImageMap(ImageCache.get("textures/userinterface/close_button.png"))

    val windowBackground = ColoredBackground(Color(0.5f, 0.5f, 0.5f, 0.5f))
    val titleBarBackground = ColoredBackground(Color(0.25f, 0.25f, 0.25f, 0.75f))
    val closeButtonBackground = TexturedBackground(closeButtonTexture, overlayColor = Color(1.0f, 1.0f, 1.0f, 1.0f))

    val uiWindow = Item("uiWindow", constraintSet, windowBackground)
    val titleBar = Item("titleBar", constraintSet2, titleBarBackground)
    val closeButton = Button("closeButton", constraintSet3, closeButtonBackground)

    closeButton.addHoverEffect(ColorEffect(Color(0.0f, 0.0f, 0.0f, 0.0f), Color(0.5f, 0.5f, 0.5f)))

    userInterface.add(uiWindow)
    titleBar.add(closeButton)
    uiWindow.add(titleBar)

    userInterface.init()

    timer.reset()

    mouse.release()

    var enableWireFrame = false

    while (window.running) {
        if (keyboard.isPressed(Key.ESCAPE)) {
            mouse.toggle()
        }

        if (keyboard.isPressed(Key.F1)) {
            break
        }

        if (keyboard.isPressed(Key.F)) {
            enableWireFrame = !enableWireFrame
        }

        if (keyboard.isPressed(Key.M)) {
            println("${mouse.x}, ${mouse.y}")
        }

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        sky.render()

        EntityRenderer.render(camera, entities, ambientLight, directionalLight)

        if (mouse.captured) {
            camera.update(window.keyboard, window.mouse, timer.getDelta())
        } else {
            if (enableWireFrame) {
                glLineWidth(5.0f)
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
            } else {
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            }
            userInterface.update(mouse)
            userInterface.draw()
        }

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