package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class CombinationSuggestions extends JPanel
{
	private static final long serialVersionUID = -7069576217412112335L;
	private MasterMind mm;
	private JLabel possibleLocs[][];
	private ColoredPinButton suggestions[][];
	private int suggestion[][];
	private int possibleLocations[][];
	private int nbrColors, nbrHoles;
	private final int PREFERRED_BUTTON_SIZE = 25;
	private JButton listCombinations;

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
		Dimension d2 = new Dimension(3*(nbrHoles + 1) * PREFERRED_BUTTON_SIZE, 20 * PREFERRED_BUTTON_SIZE);
		pan.setPreferredSize(d2);
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 3*(nbrHoles + 1); j++) {
				suggestion[i][j] = 0;
				possibleLocs[i][j] = new JLabel();
				possibleLocs[i][j].setOpaque(true);
				possibleLocs[i][j].setBackground((j % (nbrHoles+1) != 0) ? MasterMind.usedColors[0] : MasterMindPanel.BACKGROUND_COLOR);
				pan.add(possibleLocs[i][j]);
			}
		}
		add(pan, BorderLayout.CENTER);
		
		JPanel p = new JPanel(new GridLayout(nbrColors, nbrHoles, 1, 1));
		p.setBackground(MasterMindPanel.BACKGROUND_COLOR);
		double preferredButtonSize = 20D/nbrColors*PREFERRED_BUTTON_SIZE;
		Dimension d1 = new Dimension((int)(nbrHoles*preferredButtonSize), (int)(nbrColors*preferredButtonSize));
		p.setPreferredSize(d1);
		for(int i = 0; i < nbrColors; i++) {
			for(int j = 0; j < nbrHoles; j++) {
				possibleLocations[i][j] = i + 1;
				suggestions[i][j] = new ColoredPinButton(null, i, j, MasterMind.usedColors[i+1]);
				suggestions[i][j].setOpaque(true);
				suggestions[i][j].setName("possibles"+i+j);
				suggestions[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ColoredPinButton b = (ColoredPinButton) e.getSource();
						if(mm.playing) {
							if(getStateInt(b.getRow(), b.getCol()) != 0) {
								setState(b.getRow(), b.getCol(), 0);
								b.setButtonColor(MasterMind.usedColors[0]);
							} else {
								setState(b.getRow(), b.getCol(), b.getRow() + 1);
								b.setButtonColor(MasterMind.usedColors[b.getRow() + 1]);
							}
							repaint();
						}
					}
				});
				p.add(suggestions[i][j]);
			}
		}
		add(p, BorderLayout.WEST);
		
		listCombinations = new JButton("List combinations");
		listCombinations.setName("listCombs");
		listCombinations.setToolTipText("Press this button to select possible combinations.");
		listCombinations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ListCombinations();
				ReDraw();
			}
		});
		listCombinations.setOpaque(true);
		listCombinations.setBackground(MasterMindPanel.ATTEMPT_NBR_COLOR);
		listCombinations.setBorder(new LineBorder(Color.BLACK));
		add(listCombinations, BorderLayout.NORTH);
	}
	
	public void setState(int row, int col, int state) {
		if(col >= 0 && col < possibleLocations[0].length && state < MasterMind.usedColors.length)
			possibleLocations[row][col] = state;
	}
	
	public Color getState(int row, int col) {
		return MasterMind.usedColors[(col >= 0 && col < possibleLocations[0].length && row >= 0 && row < possibleLocations.length) ? possibleLocations[row][col] : 0];
	}
	
	public int getStateInt(int row, int col) {
		return (col >= 0 && col < possibleLocations[0].length && row >= 0 && row < possibleLocations.length) ? possibleLocations[row][col] : 0;
	}
	
	public int[] ArrayIncrement(int[] array, int maxVal) {
		return ArrayIncrement(array, maxVal, array.length-1);
	}
	
	public int[] ArrayIncrement(int[] array, int maxVal, int fromIndex)
	{
		int[] tempArray = array;
		if(tempArray[fromIndex] < maxVal) {
			tempArray[fromIndex]++;
			return tempArray;
		} else // element > maxVal
			for(int nextIncrement = 1; nextIncrement <= fromIndex; nextIncrement++)
				if(tempArray[fromIndex - nextIncrement] < maxVal) {
					tempArray[fromIndex - nextIncrement]++;
					for(int i = fromIndex - nextIncrement + 1; i < array.length; i++) //reset subsequent indices as we incremented at a preceeding index
						tempArray[i] = 0;
					return tempArray;
				}
		int[] errorArray = new int[array.length];
		for(int i = 0; i < errorArray.length; i++)
			errorArray[i] = -1;
		return errorArray;
	}
	
	public int[] CodeChecker(int[] index)
	{
		int[] array = index;
		for(int i = 0; i < index.length; i++) {
			if(possibleLocations[index[i]][i] != 0) { // color at position is valid
				if(i == index.length - 1) // combination is possible
					return index;
			} else { // color at position is not valid
				array = ArrayIncrement(index, nbrColors - 1, i);
				return (array[0] < 0) ? array : CodeChecker(array);
			}
		}
		//There was an error checking possible combinations
		int[] errorArray = new int[index.length];
		for(int i = 0; i < errorArray.length; i++)
			errorArray[i] = -1;
		return errorArray;
	}
	
	public void ListCombinations()
	{
		suggestion = new int[20][3*(nbrHoles + 1)];
		int[] currentCombination = new int[mm.getHiddenCode().length];
		for(int c = 0; c < 3; c++)
			for(int r = 0; r < suggestion.length; r++) {
				currentCombination = CodeChecker(currentCombination);
				for(int k = 0; k < currentCombination.length; k++)
					suggestion[r][(k + 1) + c*(mm.getHiddenCode().length + 1)] = currentCombination[k] + 1;
				currentCombination = ArrayIncrement(currentCombination, nbrColors - 1);
				if(currentCombination[0] < 0) {
					System.err.println("All combinations have been printed, or there was some error checking combinations.");
					return;
				}
			}
	}

	public void ReDraw()
	{
		for(int i = 0; i < suggestion.length; i++)
			for(int j = 0; j < suggestion[0].length; j++)
				if(j == 0 || j == mm.getHiddenCode().length+1 || j == 2*(mm.getHiddenCode().length+1) || j == 3*(mm.getHiddenCode().length+1))
					possibleLocs[i][j].setBackground(MasterMindPanel.BACKGROUND_COLOR);
				else
					possibleLocs[i][j].setBackground(MasterMind.usedColors[suggestion[i][j]]);
	}
}