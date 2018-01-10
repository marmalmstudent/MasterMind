package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class MasterMindControl extends JFrame
{
	private static final long serialVersionUID = -1380221733866727225L;
	private MasterMindPanel mmp;
	private CombinationSuggestions cs;
	private ColorPalettePanel cpp;
	private int chosenState;

	public MasterMindControl(MasterMind mm)
	{
		setTitle("Master Mind - (c) 1972 Invicta (R), Written in Java by Marcus Malmquist");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setBackground(MasterMindPanel.BACKGROUND_COLOR);
		
		// combination suggester
		JFrame frame = new JFrame("Helper");
		cs = new CombinationSuggestions(mm.getNbrOfColors(), mm.getHiddenCode().length, mm, this);
		frame.setLayout(new BorderLayout());
		frame.add(cs, BorderLayout.CENTER);
		frame.setResizable(false);
		
		// the board
		mmp = new MasterMindPanel(mm, this);
		add(mmp, BorderLayout.CENTER);
		
		// Color palette panel
		cpp = new ColorPalettePanel(mm, this, MasterMind.usedColors.length/2, 2, MasterMindPanel.BACKGROUND_COLOR);
		add(cpp, BorderLayout.EAST);

		// add panel with buttons
		JPanel panel = new JPanel(new FlowLayout(0, 0, 0));
		panel.setBackground(MasterMindPanel.BACKGROUND_COLOR);

		Dimension dim = new Dimension(200, 25);
		panel.add(makeShowHiddenCodeBtn(mm, dim));
		panel.add(makeNewGameBtn(dim));
		panel.add(makePossCombBtn(mm, frame, dim));
		panel.add(makeCheckCombBtn(mm, dim));
		add(panel, BorderLayout.NORTH);
		
		setResizable(false);
		setVisible(true);
		pack();
	}

	private JButton makeShowHiddenCodeBtn(MasterMind mm, Dimension dim) {
		JButton showHiddenCode = AddButton("Reveal hidden code", "showHiddenCode", "Press this button to show the hidden code. Doing so will stop the current game.", true, MasterMindPanel.ATTEMPT_NBR_COLOR, Color.BLACK, dim);
		showHiddenCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mm.playing) {
					mm.playing = false;
					mm.RevealHiddenCode();
					mmp.drawHiddenCode();
				}
			}
		});
		return showHiddenCode;
	}

	private JButton makeNewGameBtn(Dimension dim) {
		JButton newGame = AddButton("New game", "newGame", "Press this button to start a new game.", true, MasterMindPanel.ATTEMPT_NBR_COLOR, Color.BLACK, dim);
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] hCodeLength = {4, 5, 6, 7, 8, 9};
				Object[] nColors = {6, 7, 8, 9, 10, 11};
				int hiddenLength = 0;
				int numberOfColors = 0;
				try {
					Object hiddenLen = JOptionPane.showInputDialog(null, "Number of hidden code columns:", "Select hidden code length", JOptionPane.PLAIN_MESSAGE, null, hCodeLength, 5);
					hiddenLength = Integer.parseInt(hiddenLen.toString());
					Object nbrColors =  JOptionPane.showInputDialog(null, "Number of colors:", "Select number of colors", JOptionPane.PLAIN_MESSAGE, null, nColors, 8);
					numberOfColors = Integer.parseInt(nbrColors.toString());
				} catch(NullPointerException | IllegalArgumentException | ClassCastException iae) {
					JOptionPane.showMessageDialog(null, "Input was not an integer", "Input error", JOptionPane.ERROR_MESSAGE);
				}
				MasterMind.NewGame(hiddenLength, numberOfColors); // start a new game
				dispose(); //closes active window
			}
		});
		return newGame;
	}

	private JButton makePossCombBtn(MasterMind mm, JFrame frame, Dimension dim) {
		JButton possCombs = AddButton("Possible combinations", "possCombs", "Press this button to select possible combinations.", true, MasterMindPanel.ATTEMPT_NBR_COLOR, Color.BLACK, dim);
		possCombs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mm.playing) {
					frame.setVisible(true);
					frame.pack();
				}
			}
		});
		return possCombs;
	}

	private JButton makeCheckCombBtn(MasterMind mm, Dimension dim) {
		JButton checkComb = AddButton("Check combination", "checkComb", "Press this button to check how your combination matches the hidden combination.", true, MasterMindPanel.ATTEMPT_NBR_COLOR, Color.BLACK, dim);
		checkComb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mm.playing) {
					for(int i = mm.getNbrOfCols() - mm.getHiddenCode().length; i < mm.getNbrOfCols(); i++) {
						if(mm.getState(mm.attemptNbr, i) == MasterMind.color[0]) { //a slot has not been filled
							break;
						} else if (i == mm.getNbrOfCols() - 1) { //if all slot have been filled
							mm.CheckCorrect(mm.getCurrentCode());
							mm.attemptNbr++;
							mmp.ReDraw();
							if(mm.youWin)
								JOptionPane.showMessageDialog(null, "You found the hidden code in "+mm.attemptNbr+" attempts!", "You won!", JOptionPane.DEFAULT_OPTION);
						}
					}
				}
			}
		});
		return checkComb;
	}
	
	public static JButton AddButton(String text, String nameSet, String tooltip,
			boolean opaque, Color background, Color border, Dimension d)
	{
		JButton button = new JButton(text);
		button.setName(nameSet);
		button.setToolTipText(tooltip);
		button.setOpaque(opaque);
		button.setBackground(background);
		button.setBorder(new LineBorder(border));
		button.setPreferredSize(d);
		return button;
	}
	
	public MasterMindPanel getPanel() { return mmp; }
	public void setChosenState(int i) { chosenState = i; }
	public int getChosenState() { return chosenState; }
}
