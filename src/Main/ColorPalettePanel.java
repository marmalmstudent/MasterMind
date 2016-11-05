package Main;
import javax.swing.*;
import java.awt.*;

public class ColorPalettePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -269465759011211071L;
	private ColoredPinButton colorPalette[][];
	private JLabel chosenColor;
	
	/**
	 * Constructs an instance of ColorPalettePanel with desired number
	 * of rows and columns.
	 * @param mm The current game of MasterMind
	 * @param mmc The controlling of the current game.
	 * @param nbrOfPaletteRows The desired number of rows for the palette.
	 * @param nbrOfPaletteCols The desired number of columns for the palette.
	 * @param bgc The background color.
	 */
	public ColorPalettePanel(MasterMind mm, MasterMindControl mmc, int nbrOfPaletteRows, int nbrOfPaletteCols, Color bgc)
	{
		colorPalette = new ColoredPinButton[nbrOfPaletteRows][nbrOfPaletteCols];
		setBackground(bgc);
		double PREFERRED_BUTTON_SIZE = (double)(mm.getNbrOfRows())/(MasterMind.usedColors.length - 1)*MasterMindPanel.PREFERRED_BUTTON_SIZE;
		setLayout(new GridLayout(2*nbrOfPaletteRows, nbrOfPaletteCols/2, 1, 1));
		Dimension d = new Dimension( (int)(colorPalette[0].length * PREFERRED_BUTTON_SIZE),
				(int)(colorPalette.length * PREFERRED_BUTTON_SIZE) );
		setPreferredSize(d);
		int r, c;
		for(int i = 0; i < MasterMind.usedColors.length - 1; i++)
		{
			r = i / colorPalette[0].length;
			c = i % colorPalette[0].length;
			colorPalette[r][c] = new ColoredPinButton(null, r, c, MasterMind.usedColors[i+1]);
			colorPalette[r][c].setName("colorPalette"+i);
			colorPalette[r][c].addActionListener(mmc);
			add(colorPalette[r][c]);
		}
		chosenColor = new JLabel("Color:");
		chosenColor.setBackground(MasterMind.color[0]);
		chosenColor.setHorizontalAlignment(SwingConstants.CENTER);
		chosenColor.setOpaque(true);
		add(chosenColor);
	}
	/**
	 * Sets the color of the "Chosen color" label to the current
	 * chosen color.
	 * @param color The currently selected color.
	 */
	public void setChosenColor(Color color)
	{
		chosenColor.setBackground(color);
	}
}
