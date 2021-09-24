package devices

import org.lwjgl.glfw.GLFW.*

enum class Key(private val int: Int) {
    
    F1(GLFW_KEY_F1),
    F2(GLFW_KEY_F2),
    F3(GLFW_KEY_F3),
    F4(GLFW_KEY_F4),
    F5(GLFW_KEY_F5),
    F6(GLFW_KEY_F6),
    F7(GLFW_KEY_F7),
    F8(GLFW_KEY_F8),
    F9(GLFW_KEY_F9),
    F10(GLFW_KEY_F10),
    F11(GLFW_KEY_F11),
    F12(GLFW_KEY_F12),

    ESCAPE(GLFW_KEY_ESCAPE),
    TAB(GLFW_KEY_TAB),
    CAPS_LOCK(GLFW_KEY_CAPS_LOCK),
    LEFT_SHIFT(GLFW_KEY_LEFT_SHIFT),
    LEFT_CONTROL(GLFW_KEY_LEFT_CONTROL),
    LEFT_SUPER(GLFW_KEY_LEFT_SUPER),
    LEFT_ALT(GLFW_KEY_LEFT_ALT),
    SPACE(GLFW_KEY_SPACE),
    RIGHT_ALT(GLFW_KEY_RIGHT_ALT),
    RIGHT_SUPER(GLFW_KEY_RIGHT_SUPER),
    RIGHT_CONTROL(GLFW_KEY_RIGHT_CONTROL),
    RIGHT_SHIFT(GLFW_KEY_RIGHT_SHIFT),
    ENTER(GLFW_KEY_ENTER),
    BACKSPACE(GLFW_KEY_BACKSPACE),

    LEFT(GLFW_KEY_LEFT),
    RIGHT(GLFW_KEY_RIGHT),
    UP(GLFW_KEY_UP),
    DOWN(GLFW_KEY_DOWN),

    GRAVE_ACCENT(GLFW_KEY_GRAVE_ACCENT),
    MINUS(GLFW_KEY_MINUS),
    EQUAL(GLFW_KEY_EQUAL),
    LEFT_BRACKET(GLFW_KEY_LEFT_BRACKET),
    RIGHT_BRACKET(GLFW_KEY_RIGHT_BRACKET),
    BACKSLASH(GLFW_KEY_BACKSLASH),
    SEMICOLON(GLFW_KEY_SEMICOLON),
    APOSTROPHE(GLFW_KEY_APOSTROPHE),
    COMMA(GLFW_KEY_COMMA),
    PERIOD(GLFW_KEY_PERIOD),
    SLASH(GLFW_KEY_SLASH),

    INSERT(GLFW_KEY_INSERT),
    DELETE(GLFW_KEY_DELETE),
    PRINT_SCREEN(GLFW_KEY_PRINT_SCREEN),
    PAGE_UP(GLFW_KEY_PAGE_UP),
    PAGE_DOWN(GLFW_KEY_PAGE_DOWN),
    HOME(GLFW_KEY_HOME),
    END(GLFW_KEY_END),
    NUM_LOCK(GLFW_KEY_NUM_LOCK),
    SCROLL_LOCK(GLFW_KEY_SCROLL_LOCK),
    MENU(GLFW_KEY_MENU),
    PAUSE(GLFW_KEY_PAUSE),

    A(GLFW_KEY_A),
    B(GLFW_KEY_B),
    C(GLFW_KEY_C),
    D(GLFW_KEY_D),
    E(GLFW_KEY_E),
    F(GLFW_KEY_F),
    G(GLFW_KEY_G),
    H(GLFW_KEY_H),
    I(GLFW_KEY_I),
    J(GLFW_KEY_J),
    K(GLFW_KEY_K),
    L(GLFW_KEY_L),
    M(GLFW_KEY_M),
    N(GLFW_KEY_N),
    O(GLFW_KEY_O),
    P(GLFW_KEY_P),
    Q(GLFW_KEY_Q),
    R(GLFW_KEY_R),
    S(GLFW_KEY_S),
    T(GLFW_KEY_T),
    U(GLFW_KEY_U),
    V(GLFW_KEY_V),
    W(GLFW_KEY_W),
    X(GLFW_KEY_X),
    Y(GLFW_KEY_Y),
    Z(GLFW_KEY_Z),
    
    ZERO(GLFW_KEY_0),
    ONE(GLFW_KEY_1),
    TWO(GLFW_KEY_2),
    THREE(GLFW_KEY_3),
    FOUR(GLFW_KEY_4),
    FIVE(GLFW_KEY_5),
    SIX(GLFW_KEY_6),
    SEVEN(GLFW_KEY_7),
    EIGHT(GLFW_KEY_8),
    NINE(GLFW_KEY_9),

    KP1(GLFW_KEY_KP_1),

    UNKNOWN(GLFW_KEY_UNKNOWN);
    
    private fun getAlphabetKeys() = arrayListOf(
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    )
    
    private fun getNumericalKeys() = arrayListOf(
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE
    )
    
    private fun getPunctuationKeys() = arrayListOf(
        GRAVE_ACCENT, SEMICOLON, PERIOD, COMMA, APOSTROPHE
    )
    
    fun isAlphabetKey(): Boolean {
        if (getAlphabetKeys().contains(this)) {
            return true
        }
        return false
    }
    
    fun isNumericalKey(): Boolean {
        if (getNumericalKeys().contains(this)) {
            return true
        }
        return false
    }
    
    fun isPunctuationMark(): Boolean {
        if (getPunctuationKeys().contains(this)) {
            return true
        }
        return false
    }
    
    fun isTextKey(): Boolean {
        if (getAlphabetKeys().contains(this)) {
            return true
        }
        if (getNumericalKeys().contains(this)) {
            return true
        }
        if (getPunctuationKeys().contains(this)) {
            return true
        }
        if (this == SPACE) {
            return true
        }
        return false
    }
    
    companion object {

        internal fun fromInt(int: Int) = values().firstOrNull() {
            it.int == int
        }
    }
}