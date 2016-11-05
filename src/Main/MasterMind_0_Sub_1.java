package Main;
import java.util.Scanner;

public class MasterMind_0_Sub_1
{
	private int randomCode[];
	private static final String colors[] = {"Red", "Orange", "Yellow", "Green",
			"Blue", "Brown", "White", "Black", ""};
	public int board[][];
	private int guess[][];
	private int correct[][];
	private int okPins;
	private int attemptNbr;
	public static boolean youWin;
	public static boolean playing;

	public static void main(String args[])
	{
		MasterMind_0_Sub_1 mm = new MasterMind_0_Sub_1();
		while(playing)
		{
			System.out.println("Try a combination of five colors: \n" +
					"0: Red, 1: Orange, 2: Yellow, 3: Green, " +
					"4: Blue, 5: Brown, 6: White, 7: Black");
			System.out.println("Type -1 to exit. Type -10 to show the hidden code.");
			mm.okPins = 0;
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			for(int i = 0; i < mm.guess[mm.attemptNbr].length; i++)
			{
				try
				{
					mm.ParseUserInput(input.next(), i);
				} catch (NumberFormatException e)
				{
					System.out.println("Please enter integers ranging from 0-7 \n" +
							"one by one or separated by a space.");
					break;
				}
			}
			mm.CheckCorrect(mm.guess[mm.attemptNbr]);
			mm.attemptNbr += mm.okPins/5;
			mm.PrintNext();
		}
	}
	public MasterMind_0_Sub_1()
	{
		playing = true;
		randomCode = new int[5];
		board = new int[12][11]; // the middle one is to separate
		guess = new int[12][5];
		correct = new int[12][5];
		GenerateCode();
		attemptNbr = 0;
		youWin = false;
	}
	public void PrintNext()
	{

		for (int i = 0; i < attemptNbr; i++)
		{
			System.out.println(ToString(correct[i]) + "|" + ToString(guess[i]) + "\t"+(i+1));
		}
		if (youWin)
		{
			System.out.println("You found the hidden code in "+attemptNbr+" attempts!");
			playing = false;
		} else if (attemptNbr == 12)
		{
			System.out.println("Game over. The hidden code is:");
			System.out.println(ToString(randomCode));
			playing = false;;
		}
	}
	public void ParseUserInput(String args, int index)
	{
		int in = Integer.parseInt(args);
		if(in >= 0 && in <= 7)
		{
			guess[attemptNbr][index] = in;
			okPins++;
		} else if(in == -1 && index == 0)
		{
			System.out.println("Game over. The hidden code is:");
			System.out.println(ToString(randomCode));
			playing = false;
		} else if (in == -10 && index == 0)
		{
			System.out.println("The hidden code is:");
			System.out.println(ToString(randomCode));
			playing = false;
		}
	}
	public void CheckCorrect(int args[])
	{
		int correctColor = 0;
		int correctPosition = 0;
		boolean checkedCode[] = {false, false, false, false, false};
		for(int i = 0; i < args.length; i++)
		{
			if(!checkedCode[i] && args[i] == randomCode[i])
			{ //check if position and color is correct
				correctPosition++;
				checkedCode[i] = true;
			} else
			{
				for(int j = 0; j < randomCode.length; j++)
				{
					if(!checkedCode[j] && i != j && args[i] == randomCode[j] && args[j] != randomCode[j])
					{ //check if color is correct and position is wrong
						correctColor++;
						checkedCode[j] = true;
						break;
					}
				}
			}
			correct[attemptNbr][i] = 8; // empty the current correct array
		}
		if(correctPosition == 5)
			youWin = true;
		int position = 0;
		while(position <  5 && (correctPosition > 0 || correctColor > 0))
		{
			if(correctPosition > 0)
			{
				correct[attemptNbr][position] = 7; // black
				correctPosition--;
			} else
			{
				correct[attemptNbr][position] = 6; // white
				correctColor--;
			}
			position++;
		}
	}
	public void GenerateCode()
	{
		for(int i = 0; i < randomCode.length; i++)
		{
			randomCode[i] = (int)(8.0*Math.random());
		}
	}
	public String ToString(int args[])
	{
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < args.length; i++)
		{
			if(i != args.length - 1)
				str.append(colors[args[i]] + "\t");
			else
				str.append(colors[args[i]]);
		}
		return str.toString();
	}
}
