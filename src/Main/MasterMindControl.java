package Main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


public class MasterMindControl extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1380221733866727225L;
	private MasterMind mm;
	private MasterMindPanel mmp;
	private CombinationSuggestions cs;
	private ColorPalettePanel cpp;
	//private JLabel message; //Used for displaying things when testing
	private JButton showHiddenCode, checkComb, possCombs, newGame;
	//private JLabel chosenColor;
	private JFrame frame;
	//private ColoredPinButton colorPalette[][] = new ColoredPinButton[(MasterMind.usedColors.length - 1)/2][2];
	//private static final double PREFERRED_BUTTON_SIZE = 13.0/9.0*MasterMindPanel.PREFERRED_BUTTON_SIZE;
	private int chosenState;

	/**
	 * Constructs an instance of MasterMindControl.
	 * @param mm The current game of MasterMind.
	 */
	public MasterMindControl(MasterMind mm)
	{
		this.mm = mm;
		setTitle("Master Mind - (c) 1972 Invicta (R), Written in Java by Marcus Malmquist");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setBackground(MasterMindPanel.BACKGROUND_COLOR);
		
		// combination suggester
		frame = new JFrame("Helper");
		cs = new CombinationSuggestions(mm.getNbrOfColors(), mm.getHiddenCode().length, mm, this);
		frame.setLayout(new BorderLayout());
		frame.add(cs, BorderLayout.CENTER);
		frame.setResizable(false);
		
		// the board
		mmp = new MasterMindPanel(mm, this);
		add(mmp, BorderLayout.CENTER);
		
		// Color palette panel
		cpp = new ColorPalettePanel(mm, this, MasterMind.usedColors.length/2, 2,
				MasterMindPanel.BACKGROUND_COLOR);
		add(cpp, BorderLayout.EAST);

		// add panel with buttons
		JPanel panel = new JPanel(new FlowLayout(0, 0, 0));
		panel.setBackground(MasterMindPanel.BACKGROUND_COLOR);
		Dimension dim = new Dimension(200, 25);
		
		showHiddenCode = new JButton("Reveal hidden code");
		AddButton(showHiddenCode, "showHiddenCode", "Press this button to show the " +
				"hidden code. Doing so will stop the current game.", true, true,
				MasterMindPanel.ATTEMPT_NBR_COLOR, Color.BLACK, dim);
		panel.add(showHiddenCode);
		
		newGame = new JButton("New game");
		AddButton(newGame, "newGame", "Press this button to start a new game.",
				true, true, MasterMindPanel.ATTEMPT_NBR_COLOR, Color.BLACK, dim);
		panel.add(newGame);
		
		possCombs = new JButton("Possible combinations");
		AddButton(possCombs, "possCombs", "Press this button to select possible " +
				"combinations.", true, true, MasterMindPanel.ATTEMPT_NBR_COLOR,
				Color.BLACK, dim);
		panel.add(possCombs);

		checkComb = new JButton("Check combination");
		AddButton(checkComb, "checkComb", "Press this button to check how your" +
				" combination matches the hidden combination.", true, true,
				MasterMindPanel.ATTEMPT_NBR_COLOR, Color.BLACK, dim);
		panel.add(checkComb);
		
		add(panel, BorderLayout.NORTH);
		
		setResizable(false);
		setVisible(true);
		pack();
	}
	
	/**
	 * Creates a button for the menu bar.
	 * @param button The button to be created.
	 * @param nameSet The name of the button; crucial for its ActionListener.
	 * @param tooltip The tooltip text.
	 * @param actionListen Wether the button should have an ActionListener.
	 * @param opaque Wether the button should be opaque (show background).
	 * @param background The background color.
	 * @param border The border color.
	 * @param d The preferred dimension of the button.
	 */
	public void AddButton(JButton button, String nameSet, String tooltip,
			boolean actionListen, boolean opaque, Color background,
			Color border, Dimension d)
	{
		button.setName(nameSet);
		button.setToolTipText(tooltip);
		if(actionListen)
			button.addActionListener(this);
		button.setOpaque(opaque);
		button.setBackground(background);
		button.setBorder(new LineBorder(border));
		button.setPreferredSize(d);
	}

	/**
	 * Controls what happens when an event is registered by the listener.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof ColoredPinButton)
		{
			ColoredPinButton b = (ColoredPinButton) e.getSource();
			if(mm.playing)
			{
				if(b.getCol() >= mm.getNbrOfCols() - mm.getHiddenCode().length
						&& b.getCol() <= mm.getNbrOfCols()
						&& b.getRow() == mm.getAttemptNbr())
				{
					mm.setState(b.getRow(), b.getCol(), chosenState);
					mmp.ReDraw();
				} else if(b.getName() != null && b.getName().startsWith("colorPalette"))
				{
					for(int i = 0; i < MasterMind.colors.length; i++)
					{
						if(b.getName().endsWith(""+i))
						{
							chosenState = i + 1;
							cpp.setChosenColor(MasterMind.color[chosenState]);
						}
					}
					mmp.ReDraw();
				} else if(b.getName() != null && b.getName().startsWith("possibles"))
				{
					if(cs.getStateInt(b.getRow(), b.getCol()) != 0)
					{
						cs.setState(b.getRow(), b.getCol(), 0);
						b.setButtonColor(MasterMind.usedColors[0]);
					} else
					{
						cs.setState(b.getRow(), b.getCol(), b.getRow() + 1);
						b.setButtonColor(MasterMind.usedColors[b.getRow() + 1]);
					}
					cs.repaint();
				}
			}
		} else if(e.getSource() instanceof JButton)
		{
			JButton b = (JButton) e.getSource();
			if(mm.playing)
			{
				if(b.getName().equalsIgnoreCase("showHiddenCode"))
				{
					mm.playing = false;
					mm.RevealHiddenCode();
					mmp.drawHiddenCode();
				} else if(b.getName().equalsIgnoreCase("checkComb"))
				{
					for(int i = mm.getNbrOfCols() - mm.getHiddenCode().length; i < mm.getNbrOfCols(); i++)
					{
						if(mm.getState(mm.attemptNbr, i) == MasterMind.color[0])
						{ //a slot has not been filled
							break;
						} else if (i == mm.getNbrOfCols() - 1)
						{ //if all slot have been filled
							mm.CheckCorrect(mm.getCurrentCode());
							mm.attemptNbr++;
							mmp.ReDraw();
							if(mm.youWin)
								JOptionPane.showMessageDialog(null, "You found the hidden code in "+mm.attemptNbr+" attempts!", "You won!", JOptionPane.DEFAULT_OPTION);

						}
					}
				} else if(b.getName().equalsIgnoreCase("possCombs"))
				{
					frame.setVisible(true);
					frame.pack();
				} else if(b.getName().equalsIgnoreCase("listCombs"))
				{
					cs.ListCombinations();
					cs.ReDraw();
				}
			}
			if(b.getName().equalsIgnoreCase("newGame"))
			{
				Object[] hCodeLength = {4, 5, 6, 7, 8, 9};
				Object[] nColors = {6, 7, 8, 9, 10, 11};
				int hiddenLength = 0;
				int numberOfColors = 0;
				try
				{
					//hiddenLength = (int)JOptionPane.showInputDialog(null, "Number of hidden code columns:", "Select hidden code length", JOptionPane.PLAIN_MESSAGE, null, hCodeLength, 5);
					//numberOfColors = (int)JOptionPane.showInputDialog(null, "Number of colors:", "Select number of colors", JOptionPane.PLAIN_MESSAGE, null, nColors, 8);
					Object hiddenLen = JOptionPane.showInputDialog(null, "Number of hidden code columns:", "Select hidden code length", JOptionPane.PLAIN_MESSAGE, null, hCodeLength, 5);
					hiddenLength = Integer.parseInt(hiddenLen.toString());
					Object nbrColors =  JOptionPane.showInputDialog(null, "Number of colors:", "Select number of colors", JOptionPane.PLAIN_MESSAGE, null, nColors, 8);
					numberOfColors = Integer.parseInt(nbrColors.toString());
				} catch(IllegalArgumentException iae)
				{
					JOptionPane.showMessageDialog(null, "Input was not an integer", "Input error", JOptionPane.ERROR_MESSAGE);
				} catch(ClassCastException cce)
				{
					JOptionPane.showMessageDialog(null, "Input was not an integer", "Input error", JOptionPane.ERROR_MESSAGE);
				} catch(NullPointerException npe)
				{
					JOptionPane.showMessageDialog(null, "Input was not an integer", "Input error", JOptionPane.ERROR_MESSAGE);
				}
				MasterMind.NewGame(hiddenLength, numberOfColors); // start a new game
				this.dispose(); //closes active window

				// this does the same thing as pressing the [x] button in top right corner
				//this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
		}
	}
}
