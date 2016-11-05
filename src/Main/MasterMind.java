package Main;
import java.awt.Color;

public class MasterMind
{
	private int ROWS;
	private int COLS;
	private int randomCode[];
	public static final String colors[] = {"Red", "Orange", "Yellow", "Green",
			"Blue", "Brown", "White", "Black"};
	public static final Color allColors[] = {Color.LIGHT_GRAY, Color.RED,
		ColoredPinButton.DARK_ORANGE, Color.YELLOW, ColoredPinButton.NEW_GREEN,
		ColoredPinButton.LIGHT_BLUE, ColoredPinButton.BROWN, Color.WHITE,
		Color.BLACK, Color.CYAN, Color.MAGENTA, Color.PINK};
	public static Color usedColors[];
	
	public static final Color color[] = {Color.LIGHT_GRAY, Color.RED,
		ColoredPinButton.DARK_ORANGE, Color.YELLOW, ColoredPinButton.NEW_GREEN,
		ColoredPinButton.LIGHT_BLUE, ColoredPinButton.BROWN, Color.WHITE,
		Color.BLACK};
	private int board[][];
	public int attemptNbr;
	public boolean youWin;
	public boolean playing;

	public static void main(String args[])
	{
		MasterMind mm = new MasterMind();
		new MasterMindControl(mm);
	}
	/**
	 * Default constructor.
	 */
	public MasterMind()
	{
		this(5, 8);
	}
	/**
	 * Constructs an instance of MasterMind with desired settings.
	 * @param cols Number of desired code length.
	 * @param nbrOfColors Number of desired colors.
	 */
	public MasterMind(int cols, int nbrOfColors)
	{
		if(nbrOfColors >= allColors.length)
			nbrOfColors = allColors.length - 1;
		if(cols >= nbrOfColors)
			cols = nbrOfColors - 1;
		//ROWS = 3*cols - 2; //does not take into account that colors add difficulty
		ROWS = (int)(cols + nbrOfColors); //TODO: balance the numbers
		COLS = 2*cols + 1;
		randomCode = new int[cols];
		usedColors = new Color[nbrOfColors + 1];
		playing = true;
		board = new int[ROWS][COLS]; // the middle one is to separate
		for(int i = 0; i < usedColors.length; i++)
		{
			usedColors[i] = allColors[i];
		}
		GenerateCode();
		attemptNbr = 0;
		youWin = false;
	}

	/**
	 * Starts a new game with default settings (5 holes, 8 colors).
	 */
	public static void NewGame()
	{
		MasterMind mm = new MasterMind();
		new MasterMindControl(mm);
	}
	/**
	 * Starts a new game with desired settings.
	 * @param nCols Number of desired code length.
	 * @param nColors Number of desired colors.
	 */
	public static void NewGame(int nCols, int nColors)
	{
		MasterMind mm = new MasterMind(nCols, nColors);
		new MasterMindControl(mm);
	}
	/**
	 * 
	 * @return Number of Rows as displayed on the board.
	 */
	public int getNbrOfRows()
	{
		return ROWS;
	}
	/**
	 * 
	 * @return Number of Columns as displayed on the board. This
	 * includes the code holes, the correct-pins-holes and the middle
	 * column displaying the attempt number.
	 */
	public int getNbrOfCols()
	{
		return COLS;
	}
	/**
	 * 
	 * @return The amount of colors used in the game. This does NOT
	 * include the default button color (light gray).
	 */
	public int getNbrOfColors()
	{
		return usedColors.length - 1;
	}
	/**
	 * 
	 * @return The attempt number that the player is currently on.
	 */
	public int getAttemptNbr()
	{
		return attemptNbr;
	}
	/**
	 * 
	 * @param row The row of the given button.
	 * @param col The column of the given button.
	 * @return The state of the given button in its color form.
	 */
	public Color getState(int row, int col)
	{
		if(col != getHiddenCode().length)
			return color[ board[row][col] ];
		else
			return color[0]; //dark gray
	}
	/**
	 * 
	 * @param row The row of the given button.
	 * @param col The column of the given button.
	 * @return The state of the given button in its integer form.
	 */
	public int getStateInt(int row, int col)
	{
		if(col != getHiddenCode().length)
			return board[row][col];
		else
			return 0; //dark gray
	}
	/**
	 * Sets the state of a given button.
	 * @param row The row of the given button.
	 * @param col The column of the given button.
	 * @param state The desired state of the button.
	 */
	public void setState(int row, int col, int state)
	{
		if(col > randomCode.length && col < board[attemptNbr].length
				&& state < usedColors.length)
			board[row][col] = state;
	}
	/**
	 * 
	 * @return The code associated with the current (latest) attempt.
	 */
	public int[] getCurrentCode()
	{
		return board[attemptNbr];
	}
	/**
	 * 
	 * @return The hidden code.
	 */
	public int[] getHiddenCode()
	{
		return randomCode;
	}
	/**
	 * Reveals the hidden code.
	 */
	public void RevealHiddenCode()
	{
		for(int i = 0; i < randomCode.length; i++)
		{
			board[ROWS - 1][i + (randomCode.length + 1)] = randomCode[i];
		}
	}
	/**
	 * Compares the input to the hidden code. Correct color on wrong position
	 * yields a white pin. Correct color on the correct position yields a
	 * black pin.
	 * @param args The code that is to be compared to the hidden code.
	 */
	public void CheckCorrect(int args[])
	{
		int correctColor = 0;
		int correctPosition = 0;
		boolean checkedCode[] = new boolean[randomCode.length];
		for(int i = 0; i < randomCode.length; i++)
		{
			if(!checkedCode[i] && args[i + (randomCode.length + 1)] == randomCode[i])
			{ //check if position and color is correct
				correctPosition++;
				checkedCode[i] = true;
			} else
			{
				for(int j = 0; j < randomCode.length; j++)
				{
					if(!checkedCode[j] && i != j
							&& args[i + (randomCode.length + 1)] == randomCode[j]
							&& args[j + (randomCode.length + 1)] != randomCode[j])
					{ //check if color is correct and position is wrong
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
		while(position <  randomCode.length && (correctPosition > 0 || correctColor > 0))
		{
			if(correctPosition > 0)
			{
				board[attemptNbr][position] = 8; // black
				correctPosition--;
			} else
			{ // since either correctPosition or correctColor > 0
				board[attemptNbr][position] = 7; // white
				correctColor--;
			}
			position++;
		}
	}
	/**
	 * Generates a random hidden code.
	 */
	public void GenerateCode()
	{
		for(int i = 0; i < randomCode.length; i++)
		{
			randomCode[i] = (int)((usedColors.length - 1)*Math.random()) + 1;
		}
	}
}
