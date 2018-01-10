package Main;

import java.awt.Color;

public class MasterMind
{
	private int ROWS;
	private int COLS;
	private int randomCode[];
	public static final String colors[] = {"Red", "Orange", "Yellow", "Green", "Blue", "Brown", "White", "Black"};
	public static final Color allColors[] = {Color.LIGHT_GRAY, Color.RED, ColoredPinButton.DARK_ORANGE, Color.YELLOW, ColoredPinButton.NEW_GREEN, ColoredPinButton.LIGHT_BLUE, ColoredPinButton.BROWN, Color.WHITE, Color.BLACK, Color.CYAN, Color.MAGENTA, Color.PINK};
	public static final Color color[] = {Color.LIGHT_GRAY, Color.RED, ColoredPinButton.DARK_ORANGE, Color.YELLOW, ColoredPinButton.NEW_GREEN, ColoredPinButton.LIGHT_BLUE, ColoredPinButton.BROWN, Color.WHITE, Color.BLACK};
	public static Color usedColors[];
	private int board[][];
	public int attemptNbr;
	public boolean youWin;
	public boolean playing;

	public static void main(String args[])
	{
		new MasterMindControl(new MasterMind());
	}
	
	public MasterMind()
	{
		this(5, 8);
	}
	
	public MasterMind(int cols, int nbrOfColors)
	{
		if(nbrOfColors >= allColors.length)
			nbrOfColors = allColors.length - 1;
		if(cols >= nbrOfColors)
			cols = nbrOfColors - 1;
		ROWS = (int)(cols + nbrOfColors); //TODO: balance the numbers
		COLS = 2*cols + 1;
		randomCode = new int[cols];
		usedColors = new Color[nbrOfColors+1];
		playing = true;
		board = new int[ROWS][COLS]; // the middle one is to separate
		for(int i = 0; i < usedColors.length; i++)
			usedColors[i] = allColors[i];
		GenerateCode();
		attemptNbr = 0;
		youWin = false;
	}

	public static void NewGame() {
		new MasterMindControl(new MasterMind());
	}
	
	public static void NewGame(int nCols, int nColors) {
		new MasterMindControl(new MasterMind(nCols, nColors));
	}
	
	public int getNbrOfRows() { return ROWS; }
	public int getNbrOfCols() { return COLS; }
	public int getNbrOfColors() { return usedColors.length - 1; }
	public int getAttemptNbr() { return attemptNbr; }
	public Color getState(int row, int col) { return color[getStateInt(row, col)]; }
	public int getStateInt(int row, int col) { return (col != getHiddenCode().length) ? board[row][col] : 0; }
	public void setState(int row, int col, int state) {
		if(col > randomCode.length && col < board[attemptNbr].length && state < usedColors.length)
			board[row][col] = state;
	}
	public int[] getCurrentCode() { return board[attemptNbr]; }
	public int[] getHiddenCode() { return randomCode; }
	public void RevealHiddenCode() {
		for(int i = 0; i < randomCode.length; i++)
			board[ROWS-1][i+(randomCode.length+1)] = randomCode[i];
	}
	
	public void CheckCorrect(int args[])
	{
		int correctColor = 0;
		int correctPosition = 0;
		boolean checkedCode[] = new boolean[randomCode.length];
		for(int i = 0; i < randomCode.length; i++) {
			if(!checkedCode[i] && args[i + (randomCode.length + 1)] == randomCode[i]) { //check if position and color is correct
				correctPosition++;
				checkedCode[i] = true;
			} else {
				for(int j = 0; j < randomCode.length; j++) {
					if(!checkedCode[j] && i != j && args[i + (randomCode.length + 1)] == randomCode[j] && args[j + (randomCode.length + 1)] != randomCode[j]) { //check if color is correct and position is wrong
						correctColor++;
						checkedCode[j] = true;
						break;
					}
				}
			}
		}
		if(correctPosition == randomCode.length)
			youWin = true;
		int position = 0;
		while(position <  randomCode.length && (correctPosition > 0 || correctColor > 0)) {
			if(correctPosition > 0) {
				board[attemptNbr][position] = 8; // black
				correctPosition--;
			} else { // since either correctPosition or correctColor > 0
				board[attemptNbr][position] = 7; // white
				correctColor--;
			}
			position++;
		}
	}
	
	public void GenerateCode()
	{
		for(int i = 0; i < randomCode.length; i++)
			randomCode[i] = (int)((usedColors.length - 1)*Math.random()) + 1;
	}
}
