package userinterface.text.fontMeshCreator;

import math.vectors.Vector2;
import math.vectors.Vector3;

/**
 * Represents a piece of userinterface.text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {

	private String textString;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3 colour = new Vector3(0f, 0f, 0f);

	private Vector2 position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;

	/**
	 * Creates a new userinterface.text, loads the userinterface.text's quads into a VAO, and adds the userinterface.text
	 * to the screen.
	 * 
	 * @param text
	 *            - the userinterface.text.
	 * @param fontSize
	 *            - the font size of the userinterface.text, where a font size of 1 is the
	 *            default size.
	 * @param font
	 *            - the font that this userinterface.text should use.
	 * @param position
	 *            - the position on the screen where the top left corner of the
	 *            userinterface.text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength
	 *            - basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the userinterface.text is longer than this length it will go onto the next
	 *            line. When userinterface.text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param centered
	 *            - whether the userinterface.text should be centered or not.
	 */
	public GUIText(String text, float fontSize, FontType font, Vector2 position, float maxLineLength,
			boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
//		TextMaster.loadText(this);;
	}

	/**
	 * Remove the userinterface.text from the screen.
	 */
	public void remove() {
//		TextMaster.removeText(this);
	}

	/**
	 * @return The font used by this userinterface.text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the colour of the userinterface.text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
	 */
	public void setColour(float r, float g, float b) {
		colour.setX(r);
		colour.setY(g);
		colour.setZ(b);
	}

	/**
	 * @return the colour of the userinterface.text.
	 */
	public Vector3 getColour() {
		return colour;
	}

	/**
	 * @return The number of lines of userinterface.text. This is determined when the userinterface.text is
	 *         loaded, based on the length of the userinterface.text and the max line length
	 *         that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the userinterface.text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @return the ID of the userinterface.text's VAO, which contains all the vertex data for
	 *         the quads on which the userinterface.text will be rendered.
	 */
	public int getMesh() {
		return textMeshVao;
	}

	/**
	 * Set the VAO and vertex count for this userinterface.text.
	 * 
	 * @param vao
	 *            - the VAO containing all the vertex data for the quads on
	 *            which the userinterface.text will be rendered.
	 * @param verticesCount
	 *            - the total number of vertices in all of the quads.
	 */
	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	/**
	 * @return The total number of vertices of all the userinterface.text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}

	/**
	 * @return the font size of the userinterface.text (a font size of 1 is normal).
	 */
	protected float getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the number of lines that this userinterface.text covers (method used only in
	 * loading).
	 * 
	 * @param number
	 */
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the userinterface.text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this userinterface.text.
	 */
	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of userinterface.text.
	 */
	protected String getTextString() {
		return textString;
	}

}
