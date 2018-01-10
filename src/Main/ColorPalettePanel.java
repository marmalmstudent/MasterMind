package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ColorPalettePanel extends JPanel
{
	private static final long serialVersionUID = -269465759011211071L;
	private ColoredPinButton colorPalette[][];
	private JLabel chosenColor;
	
	public ColorPalettePanel(MasterMind mm, MasterMindControl mmc, int nbrOfPaletteRows, int nbrOfPaletteCols, Color bgc)
	{
		colorPalette = new ColoredPinButton[nbrOfPaletteRows][nbrOfPaletteCols];
		setBackground(bgc);
		double PREFERRED_BUTTON_SIZE = mm.getNbrOfRows() / (MasterMind.usedColors.length - 1D) * MasterMindPanel.PREFERRED_BUTTON_SIZE;
		setLayout(new GridLayout(2*nbrOfPaletteRows, nbrOfPaletteCols/2, 1, 1));
		Dimension d = new Dimension((int) (colorPalette[0].length*PREFERRED_BUTTON_SIZE), (int) (colorPalette.length*PREFERRED_BUTTON_SIZE));
		setPreferredSize(d);
		int r, c;
		for(int i = 0; i < MasterMind.usedColors.length - 1; i++) {
			r = i / colorPalette[0].length;
			c = i % colorPalette[0].length;
			colorPalette[r][c] = createPinBtn(mm, mmc, r, c, i);
			add(colorPalette[r][c]);
		}
		chosenColor = new JLabel("Color:");
		chosenColor.setBackground(MasterMind.color[0]);
		chosenColor.setHorizontalAlignment(SwingConstants.CENTER);
		chosenColor.setOpaque(true);
		add(chosenColor);
	}

	private ColoredPinButton createPinBtn(MasterMind mm, MasterMindControl mmc, int r, int c, int i) {
		ColoredPinButton btn = new ColoredPinButton(null, r, c, MasterMind.usedColors[i+1]);
		btn.setName("colorPalette"+i);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ColoredPinButton b = (ColoredPinButton) e.getSource();
				if(mm.playing) {
					for(int i = 0; i < MasterMind.colors.length; i++) {
						if(b.getName().endsWith(""+i)) {
							mmc.setChosenState(i + 1);
							setChosenColor(MasterMind.color[i + 1]);
						}
					}
					mmc.getPanel().ReDraw();
				}
			}
		});
		return btn;
	}
	
	public void setChosenColor(Color color) { chosenColor.setBackground(color); }
}
