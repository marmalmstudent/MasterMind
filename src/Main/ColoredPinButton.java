package Main;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class ColoredPinButton extends JButton
{
	private static final long serialVersionUID = -2173145128295973312L;
	private final int row, col;
	private String buttonText;
	private Color buttonColor = null;
	public static final Color BROWN = new Color(153,76,0);
	public static final Color LIGHT_BLUE = new Color(0,128,255);
	public static final Color DARK_ORANGE = new Color(255,128,0);
	public static final Color NEW_GREEN = new Color(0,204,0);
	public static final Color EMPTY_SLOT = new Color(200,100,50);
	
	public ColoredPinButton(String buttonText, int row, int col, Color buttonColor)
	{
		super(buttonText);
		this.buttonText = buttonText;
		this.row = row;
		this.col = col;
		setButtonColor(buttonColor);
		setBorder(new LineBorder(MasterMindPanel.BACKGROUND_COLOR, 0));
	}
	
	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
		setBackground(buttonColor);
	}
	
	public int getCol() { return col; }
	public int getRow() { return row; }
	public Color getColor() { return buttonColor; }
	public String getButtonText() { return buttonText; }
}
