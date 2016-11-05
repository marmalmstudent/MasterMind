package Main;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class CombinationSuggestions extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7069576217412112335L;
	private MasterMind mm;
	private JLabel possibleLocs[][];
	private ColoredPinButton suggestions[][];
	private int suggestion[][];
	private int possibleLocations[][];
	private int nbrColors, nbrHoles;
	private final int PREFERRED_BUTTON_SIZE = 25;
	private JButton listCombinations;

	/**
	 * Constructs an instance of CombinationSuggestion with desired setting.
	 * @param nbrColors The number of colors used in the game.
	 * @param nbrHoles The length of the hidden code.
	 * @param mm The current game of MasterMind.
	 * @param mmc The controlling of the current game.
	 */
	public CombinationSuggestions(int nbrColors, int nbrHoles, MasterMind mm, MasterMindControl mmc)
	{
		this.mm = mm;
		this.nbrColors = nbrColors;
		this.nbrHoles = nbrHoles;
		possibleLocations = new int[nbrColors][nbrHoles];
		suggestions = new ColoredPinButton[nbrColors][nbrHoles];
		suggestion = new int[20][3*(nbrHoles + 1)];
		possibleLocs = new JLabel[20][3*(nbrHoles + 1)];

		this.setBackground(MasterMindPanel.BACKGROUND_COLOR);
		setLayout(new BorderLayout());
				
		JPanel pan = new JPanel(new GridLayout(20, 3*(nbrHoles + 1), 1, 1));
		pan.setBackground(MasterMindPanel.BACKGROUND_COLOR);
		Dimension d2 = new Dimension(3*(nbrHoles + 1) * PREFERRED_BUTTON_SIZE,
				20 * PREFERRED_BUTTON_SIZE);
		pan.setPreferredSize(d2);
		for(int i = 0; i < 20; i++)
		{
			for(int j = 0; j < 3*(nbrHoles + 1); j++)
			{
				suggestion[i][j] = 0;
				possibleLocs[i][j] = new JLabel();
				possibleLocs[i][j].setOpaque(true);
				if(j % (nbrHoles + 1) != 0)
					possibleLocs[i][j].setBackground(MasterMind.usedColors[0]);
				else
					possibleLocs[i][j].setBackground(MasterMindPanel.BACKGROUND_COLOR);
				pan.add(possibleLocs[i][j]);
			}
		}
		add(pan, BorderLayout.CENTER);
		
		JPanel p = new JPanel(new GridLayout(nbrColors, nbrHoles, 1, 1));
		p.setBackground(MasterMindPanel.BACKGROUND_COLOR);
		double preferredButtonSize = (double)(20/nbrColors)*PREFERRED_BUTTON_SIZE;
		Dimension d1 = new Dimension( (int)(nbrHoles * preferredButtonSize),
				(int)(nbrColors * preferredButtonSize) );
		p.setPreferredSize(d1);
		for(int i = 0; i < nbrColors; i++)
		{
			for(int j = 0; j < nbrHoles; j++)
			{
				possibleLocations[i][j] = i + 1;
				suggestions[i][j] = new ColoredPinButton(null, i, j, MasterMind.usedColors[i+1]);
				suggestions[i][j].setOpaque(true);
				suggestions[i][j].setName("possibles"+i+j);
				suggestions[i][j].addActionListener(mmc);
				p.add(suggestions[i][j]);
			}
		}
		add(p, BorderLayout.WEST);
		
		listCombinations = new JButton("List combinations");
		listCombinations.setName("listCombs");
		listCombinations.setToolTipText("Press this button to select possible combinations.");
		listCombinations.addActionListener(mmc);
		listCombinations.setOpaque(true);
		listCombinations.setBackground(MasterMindPanel.ATTEMPT_NBR_COLOR);
		listCombinations.setBorder(new LineBorder(Color.BLACK));
		add(listCombinations, BorderLayout.NORTH);
	}
	
	/**
	 * Sets the state in integer form of a given button
	 * @param row The row where the button is located
	 * @param col The column wher the button is located
	 * @param state The desired state
	 */
	public void setState(int row, int col, int state)
	{
		if(col >= 0 && col < possibleLocations[0].length
				&& state < MasterMind.usedColors.length)
			possibleLocations[row][col] = state;
	}
	
	/**
	 * Returns the state in color form of a given button
	 * @param row The row where the button is located
	 * @param col The column wher the button is located
	 * @return The state of the button in color form
	 */
	public Color getState(int row, int col)
	{
		if(col >= 0 && col < possibleLocations[0].length && row >= 0 && row < possibleLocations.length)
			return MasterMind.usedColors[ possibleLocations[row][col] ];
		else
			return MasterMind.usedColors[0];
	}
	
	/**
	 * Returns the state in its integer form of a given button
	 * @param row The row where the button is located
	 * @param col The column wher the button is located
	 * @return The state of the button in its integer form
	 */
	public int getStateInt(int row, int col)
	{
		if(col >= 0 && col < possibleLocations[0].length && row >= 0 && row < possibleLocations.length)
			return possibleLocations[row][col];
		else
			return 0;
	}
	
	/**
	 * ArrayIncrement icrements an array, or rather generates the next
	 * code based on the information it is fed. This function will always
	 * start checking the last array, unlike ArrayIncrement with 3 parameters.
	 * @param array The array (code) that is to be incremented.
	 * @param maxVal The maximum allowed value of an element in the array.
	 * @return The next array based on the information it is fed.
	 */
	public int[] ArrayIncrement(int[] array, int maxVal)
	{
		int[] tempArray = array;
		if(tempArray[array.length - 1] < maxVal)
		{
			tempArray[array.length - 1]++;
			return tempArray;
		} else
		{ // last element > maxVal
			for(int nextIncrement = 1; nextIncrement < array.length; nextIncrement++)
			{
				if(tempArray[(array.length - 1) - nextIncrement] < maxVal)
				{
					tempArray[(array.length - 1) - nextIncrement]++;
					for(int i = array.length - nextIncrement; i < array.length; i++)
					{ //reset subsequent indices as we incremented at a preceeding index
						tempArray[i] = 0;
					}
					return tempArray;
				}
			}
		}
		int[] errorArray = new int[array.length];
		for(int i = 0; i < errorArray.length; i++)
		{
			errorArray[i] = -1;
		}
		return errorArray;
	}
	/**
	 * ArrayIncrement icrements an array, or rather generates the next
	 * code based on the information it is fed.
	 * @param array The array (code) that is to be incremented.
	 * @param maxVal The maximum allowed value of an element in the array.
	 * @param fromIndex The index we want to increment from.
	 * @return The next array based on the information it is fed.
	 */
	public int[] ArrayIncrement(int[] array, int maxVal, int fromIndex)
	{
		int[] tempArray = array;
		if(tempArray[fromIndex] < maxVal)
		{
			tempArray[fromIndex]++;
			return tempArray;
		} else
		{ // element > maxVal
			for(int nextIncrement = 1; nextIncrement <= fromIndex; nextIncrement++)
			{
				if(tempArray[fromIndex - nextIncrement] < maxVal)
				{
					tempArray[fromIndex - nextIncrement]++;
					for(int i = fromIndex - nextIncrement + 1; i < array.length; i++)
					{ //reset subsequent indices as we incremented at a preceeding index
						tempArray[i] = 0;
					}
					return tempArray;
				}
			}
		}
		int[] errorArray = new int[array.length];
		for(int i = 0; i < errorArray.length; i++)
		{
			errorArray[i] = -1;
		}
		return errorArray;
	}
	/**
	 * CodeChecker takes a combination of colors (represented as numbers
	 * in an array), and compare that combination to the user input. If
	 * it passes, it will return the combination. If it does not pass
	 * it will pass the combination over to ArrayIncrement to get the
	 * next code to check.
	 * @param index The array containing a combination that is to be checked
	 * if it clears out
	 * @return A combination that is possible, having compared it to the
	 * user input.
	 */
	public int[] CodeChecker(int[] index)
	{
		int[] array = index;
		for(int i = 0; i < index.length; i++)
		{
			if(possibleLocations[index[i]][i] != 0)
			{ // color at position is valid
				if(i != index.length - 1)
				{
					continue;
				} else
				{ // combination is possible
					return index;
				}	
			} else
			{ // color at position is not valid
				array = ArrayIncrement(index, nbrColors - 1, i);
				if(array[0] < 0)
					return array;
				else
					return CodeChecker(array);
			}
		}
		//There was an error checking possible combinations
		int[] errorArray = new int[index.length];
		for(int i = 0; i < errorArray.length; i++)
		{
			errorArray[i] = -1;
		}
		return errorArray;
	}
	
	/**
	 * ListCombinations lists up to 60 possible combinations based
	 * on user input.
	 */
	public void ListCombinations()
	{
		suggestion = new int[20][3*(nbrHoles + 1)];
		int[] currentCombination = new int[mm.getHiddenCode().length];
		for(int j = 0; j < 3; j++)
		{ // loop through all columns
			for(int i = 0; i < suggestion.length; i++)
			{ // loop through all rows
				currentCombination = CodeChecker(currentCombination);
				for(int k = 0; k < currentCombination.length; k++)
				{
					suggestion[i][(k + 1) + j*(mm.getHiddenCode().length + 1)] = currentCombination[k] + 1;
					
				}
				currentCombination = ArrayIncrement(currentCombination, nbrColors - 1);
				if(currentCombination[0] < 0)
				{
					System.err.println("All combinations have been printed, or there " +
							"was some error checking combinations.");
					return;
				}
			}
		}
	}

	/**
	 * ReDraw redraw the panel
	 */
	public void ReDraw()
	{
		for(int i = 0; i < suggestion.length; i++)
		{
			for(int j = 0; j < suggestion[0].length; j++)
			{
				if(j == 0 || j == mm.getHiddenCode().length+1
						|| j == 2*(mm.getHiddenCode().length+1)
						|| j == 3*(mm.getHiddenCode().length+1))
					possibleLocs[i][j].setBackground(MasterMindPanel.BACKGROUND_COLOR);
				else
					possibleLocs[i][j].setBackground(MasterMind.usedColors[ suggestion[i][j] ]);
			}
		}
	}
}