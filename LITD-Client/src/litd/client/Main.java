package litd.client;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.border.BevelBorder;

import litd.shared.PlayerInitInfo;

import sgf.SGF;

public class Main extends JApplet {
	
	public static Main s;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2751101756799103685L;
	
	public final JPanel textPromptPanel = new JPanel();
	public final JTextField textPromptBox = new JTextField();
	public final JLabel textPromptLabel = new JLabel("Label: ");
	public final JButton textPromptButton = new JButton("Submit");
	
	public ActionListener textPromptButtonAction = null;
	public PlayerInitInfo playerInitInfo = null;
	
	final JTextField playerNameBox = new JTextField();
	final JLabel strLabel = new JLabel("Strength: ");
	final JLabel aglLabel = new JLabel("Agility: ");
	final JLabel endLabel = new JLabel("Endurance: ");
	final JLabel mgcLabel = new JLabel("Magic: ");
	final JLabel araLabel = new JLabel("Aura: ");
	
	final JPanel statsPanel = new JPanel();
	
	JLabel introDisplayer;

	@Override
	public void destroy() {
		SGF.getInstance().stop();
		super.destroy();
		s = null;
		textPromptButtonAction = null;
		playerInitInfo = null;
	}
	
	
	
	public void rollStats()
	{
		float[] newStats = new float[5];
		
		for(int i=0;i<newStats.length;++i)
			newStats[i] = (float) Math.random();
		
		playerInitInfo = new PlayerInitInfo(playerNameBox.getText(), newStats);
		playerInitInfo.normalizeStats();
		
		strLabel.setText("Strength: " + (int)playerInitInfo.stats[0]);
		aglLabel.setText("Agility: " + (int)playerInitInfo.stats[1]);
		endLabel.setText("Endurance: " + (int)playerInitInfo.stats[2]);
		mgcLabel.setText("Magic: " + (int)playerInitInfo.stats[3]);
		araLabel.setText("Aura: " + (int)playerInitInfo.stats[4]);
	}
	
	int introStatus = 0;
	
	public void advanceIntro()
	{
		introStatus++;
		if(introStatus == 1) introDisplayer.setIcon(new ImageIcon(Main.s.getImage(Main.s.getDocumentBase(), "./media/story.png")));
		if(introStatus == 2) introDisplayer.setIcon(new ImageIcon(Main.s.getImage(Main.s.getDocumentBase(), "./media/help.png")));
		if(introStatus == 3)
		{
			introDisplayer.setVisible(false);
			//statsPanel.setVisible(true);
			//this.getContentPane().remove(introDisplayer);
			this.add(statsPanel);
			this.add(textPromptPanel);
			System.out.println("LAST");
		}
	}

	@Override
	public void init() {
		s = this;
		
		// CREATE TEXT PROMPT
		textPromptPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		textPromptPanel.setLayout(new BoxLayout(textPromptPanel, BoxLayout.X_AXIS));
		
		textPromptLabel.setPreferredSize(new Dimension(100, 20));
		textPromptBox.setPreferredSize(new Dimension(500, 20));
		textPromptButton.setPreferredSize(new Dimension(100, 20));
		
		textPromptBox.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					textPromptButtonAction.actionPerformed(null);
					textPromptPanel.setVisible(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		
		textPromptButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				textPromptButtonAction.actionPerformed(e);
				textPromptPanel.setVisible(false);
			}
		});
		
		textPromptPanel.add(textPromptLabel);
		textPromptPanel.add(Box.createHorizontalStrut(10));
		textPromptPanel.add(textPromptBox);
		textPromptPanel.add(Box.createHorizontalStrut(10));
		textPromptPanel.add(textPromptButton);
		textPromptPanel.add(Box.createHorizontalGlue());
		
		textPromptPanel.setMaximumSize(new Dimension(750, 30));
		textPromptPanel.setPreferredSize(new Dimension(750, 30));
		textPromptPanel.setVisible(false);
		
		// CREATE STATS PROMPT
		
		
		statsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
		statsPanel.setMaximumSize(new Dimension(190, 240));
		
		final JLabel nameLabel = new JLabel("Enter Character Name");
		nameLabel.setPreferredSize(new Dimension(170, 20));
		
		playerNameBox.setPreferredSize(new Dimension(170, 20));
		
		final JLabel infoLabel = new JLabel("Hover over stats for info");
		nameLabel.setPreferredSize(new Dimension(170, 20));
		
		strLabel.setPreferredSize(new Dimension(170, 20));
		strLabel.setToolTipText("Strength increases the damage of your physical attacks");
		
		aglLabel.setPreferredSize(new Dimension(170, 20));
		aglLabel.setToolTipText("Agility increases the chance your physical attacks will hit and reduces enemy chance to hit");
		
		endLabel.setPreferredSize(new Dimension(170, 20));
		endLabel.setToolTipText("Endurance increases your health pool");
		
		mgcLabel.setPreferredSize(new Dimension(170, 20));
		mgcLabel.setToolTipText("Magic increases the damage and effects of Fireball, Curse and Sleep");
		
		araLabel.setPreferredSize(new Dimension(170, 20));
		araLabel.setToolTipText("Aura increases the healing and effects of Heal, Bless and Shield");
		
		final JButton rerollButton = new JButton("Reroll Stats");
		final JButton connectButton = new JButton("Connect");
		
		rollStats();
		
		playerNameBox.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				playerInitInfo.name = playerNameBox.getText();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				playerInitInfo.name = playerNameBox.getText();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				playerInitInfo.name = playerNameBox.getText();
			}
		});
		
		connectButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				statsPanel.setVisible(false);
				SGF.getInstance().start(Main.this, Main.this, new Game(getParameter("serverHostname")));
				//SGF.getInstance().setRenderFPS(true);
			}
		});
		
		rerollButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.this.rollStats();
			}
		});
		
		statsPanel.add(nameLabel);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(playerNameBox);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(infoLabel);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(strLabel);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(aglLabel);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(endLabel);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(mgcLabel);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(araLabel);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(rerollButton);
		statsPanel.add(Box.createVerticalStrut(5));
		statsPanel.add(connectButton);
		
		this.getContentPane().setLayout(new OverlayLayout(this.getContentPane()));
		
		
		introDisplayer = new JLabel(new ImageIcon(Main.s.getImage(Main.s.getDocumentBase(), "./media/title.png")));
		introDisplayer.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				advanceIntro();
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		introDisplayer.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				advanceIntro();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		
		this.getContentPane().add(introDisplayer);
		
		
		
		super.init();
	}
}
