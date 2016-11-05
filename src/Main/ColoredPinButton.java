package Main;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class ColoredPinButton extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2173145128295973312L;
	private final int row, col;
	private String buttonText;
	private Color buttonColor = null;
	public static final Color BROWN = new Color(153,76,0);
	public static final Color LIGHT_BLUE = new Color(0,128,255);
	public static final Color DARK_ORANGE = new Color(255,128,0);
	public static final Color NEW_GREEN = new Color(0,204,0);
	public static final Color EMPTY_SLOT = new Color(200,100,50);
	
	/**
	 * Creates an instance of ColoredPinButton.
	 * @param buttonText The text that should be displayed on the button.
	 * @param row The row of the given button.
	 * @param col The column of the given button.
	 * @param buttonColor The color of the given button.
	 */
	public ColoredPinButton(String buttonText, int row, int col, Color buttonColor)
	{
		super(buttonText);
		this.buttonText = buttonText;
		this.row = row;
		this.col = col;
		setButtonColor(buttonColor);
		setBorder(new LineBorder(MasterMindPanel.BACKGROUND_COLOR, 0));
	}
	/**
	 * 
	 * @return The column where the button is located in the grid.
	 */
	public int getCol()
	{
		return col;
	}
	/**
	 * 
	 * @return The row where the button is located in the grid.
	 */
	public int getRow()
	{
		return row;
	}
	/**
	 * 
	 * @return The color of the given button.
	 */
	public Color getColor()
	{
		return buttonColor;
	}
	/**
	 * 
	 * @return The text displayed on the button.
	 */
	public String getButtonText()
	{
		return buttonText;
	}
	/**
	 * Sets the background color of the given button.
	 * @param buttonColor The desired new color.
	 */
	public void setButtonColor(Color buttonColor)
	{
		this.buttonColor = buttonColor;
		setBackground(buttonColor);
	}
}
