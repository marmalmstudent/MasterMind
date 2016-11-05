package Main;
import javax.swing.*;
import java.awt.*;

public class MasterMindPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7618852705631500401L;
	public static final int PREFERRED_BUTTON_SIZE = 50;
	public static final Color BACKGROUND_COLOR = new Color(100, 100, 100);
	public static final Color ATTEMPT_NBR_COLOR = new Color(150, 150, 150);
	public static final Color CURRENT_ATTEMPT_NBR_COLOR = new Color(220, 220, 220);
	private MasterMind mm;
	private ColoredPinButton boardGrid[][];
	private int nRows, nCols;
	
	/**
	 * Constructs an instance of MasterMindPanel.
	 * @param mm The current game of MasterMind
	 * @param mmc The controlling of the current game.
	 */
	public MasterMindPanel(MasterMind mm, MasterMindControl mmc)
	{
		this.mm = mm;
		nRows = mm.getNbrOfRows();
		nCols = mm.getNbrOfCols();
		Dimension d = new Dimension(nCols * PREFERRED_BUTTON_SIZE,
				nRows * PREFERRED_BUTTON_SIZE);
		setPreferredSize(d);
		setLayout(new GridLayout(nRows, nCols, 1, 1));
		setBackground(BACKGROUND_COLOR);
		boardGrid = new ColoredPinButton[nRows][nCols];
		for(int i = 0; i < nRows; i++)
		{
			for(int j = 0; j < nCols; j++)
			{
				if(i == nRows - 1)
				{ // last line, where the hidden code will appear
					boardGrid[i][j] = new ColoredPinButton(null, i, j, BACKGROUND_COLOR);
					add(boardGrid[i][j]);
				} else if(j == mm.getHiddenCode().length)
				{ // column displaying attempt nbr
					if(i == mm.attemptNbr)
						boardGrid[i][j] = new ColoredPinButton("" + (i + 1), i, j, CURRENT_ATTEMPT_NBR_COLOR);
					else
						boardGrid[i][j] = new ColoredPinButton("" + (i + 1), i, j, ATTEMPT_NBR_COLOR);
					add(boardGrid[i][j]);
				} else
				{
					boardGrid[i][j] = new ColoredPinButton(null, i, j, mm.getState(i, j));
					boardGrid[i][j].addActionListener(mmc);
					add(boardGrid[i][j]);
				}
			}
		}
	}
	/**
	 * Redraws the board.
	 */
	public void ReDraw()
	{
		for(int i = 0; i < nRows; i++)
		{
			for(int j = 0; j < nCols; j++)
			{
				if(i == mm.attemptNbr && j == mm.getHiddenCode().length)
					boardGrid[i][j].setButtonColor(CURRENT_ATTEMPT_NBR_COLOR);
				if(i != nRows - 1 && j != mm.getHiddenCode().length)
					boardGrid[i][j].setButtonColor(mm.getState(i, j));
			}
		}
	}
	/**
	 * Draws the hidden code after it has been revealed.
	 */
	public void drawHiddenCode()
	{
		if(!mm.playing)
		{
			for(int i = nCols - mm.getHiddenCode().length; i < nCols; i++)
			{
				boardGrid[nRows - 1][i].setButtonColor(mm.getState(nRows - 1, i));
			}
		}
	}
}