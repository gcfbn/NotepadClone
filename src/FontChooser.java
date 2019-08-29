import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FontChooser extends JDialog implements ActionListener
{
	private JComboBox<String> fontCombo;
	private JButton colorButton, OKButton, cancelButton;
	private JColorChooser colorChooser;
	private JCheckBox boldCheckBox, italicCheckBox;
	private JComboBox<Integer> sizeCombo;
	private JTextArea exampleArea;
	private JLabel fontLabel, colorLabel, styleLabel, sizeLabel, exampleLabel;
	
	public static final int OK_OPTION = 1;
	public static final int CANCEL_OPTION = 0;
	public int choosenOption = CANCEL_OPTION;
	
	private Font currentFont;
	private Color currentColor;
	
	FontChooser(Font currentFont, Color currentColor)
	{
		this.currentFont = currentFont;
		this.currentColor = currentColor;
		setTitle("Font");
		setResizable(false);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(3,5,3,5);
		
		fontLabel = new JLabel("Font:");
		fontLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(fontLabel, constraints);
		fontLabel.setVisible(true);
		
		String[] fontNames;
		{ 	//gets list of fonts from system
			GraphicsEnvironment graphicsEnvironment;								
			graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			fontNames = graphicsEnvironment.getAvailableFontFamilyNames();
		}
		
//		===CREATE COMPONENTS===
		fontCombo = new JComboBox<String>(fontNames);
		fontCombo.setEditable(false);
		String currentFontName = currentFont.getFamily();
		//System.out.println(currentFont.getFamily());
		if (currentFontName.equals("Dialog.plain")) currentFontName = "Arial";  //case when font name is incorrect
		fontCombo.setSelectedItem(currentFontName);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(fontCombo, constraints);
		
		colorLabel = new JLabel("Color:");
		colorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		constraints.gridx = 2;
		constraints.gridy = 0;
		add(colorLabel, constraints);
		
		colorButton = new JButton("Color chooser");
		constraints.gridx = 2;
		constraints.gridy = 1;
		add(colorButton, constraints);
		
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.NONE;
		
		sizeLabel = new JLabel("Size:");
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(sizeLabel, constraints);
		
		Integer[] fontSizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
		sizeCombo = new JComboBox<Integer>(fontSizes);
		sizeCombo.setSelectedItem((Integer)currentFont.getSize());
		sizeCombo.setFont(new Font("Arial", Font.PLAIN, 16));
		sizeCombo.setEditable(false);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.ipadx = 30;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(sizeCombo, constraints);
		
		constraints.ipadx = 0;
		
		styleLabel = new JLabel("Font style:");
		styleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(styleLabel, constraints);
		
		constraints.anchor = GridBagConstraints.LINE_START;
		
		boldCheckBox = new JCheckBox("BOLD");
		boldCheckBox.setFont(new Font("Arial", Font.BOLD, 14));
		constraints.gridx = 4;
		constraints.gridy = 2;
		add(boldCheckBox, constraints);
		
		italicCheckBox = new JCheckBox("ITALIC");
		italicCheckBox.setFont(new Font("Arial", Font.ITALIC, 14));
		constraints.gridx = 4;
		constraints.gridy = 3;
		add(italicCheckBox, constraints);
		
		exampleLabel = new JLabel("Example:");
		exampleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(exampleLabel, constraints);
		
		exampleArea = new JTextArea("Aa Bb Cc");
		exampleArea.setFont(new Font(currentFont.getFamily(), currentFont.getStyle(), currentFont.getSize()));
		exampleArea.setForeground(currentColor);
		exampleArea.setEditable(false);
		Border exampleBorder = BorderFactory.createLineBorder(Color.BLACK);													//creates black border around
		exampleArea.setBorder(BorderFactory.createCompoundBorder(exampleBorder,BorderFactory.createEmptyBorder(2,2,2,2)));	//text area
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(exampleArea, constraints);
		
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.NONE;
		
		OKButton = new JButton("OK");
		constraints.gridx = 3;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(OKButton, constraints);
		
		cancelButton = new JButton("Cancel");
		constraints.gridx = 4;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(cancelButton, constraints);
		
		pack();
		setVisible(true);
		
//		===GET CURRENT FONT STYLE===
		int currentFontStyle = currentFont.getStyle();
		switch (currentFontStyle)
		{
		case (Font.BOLD):
			boldCheckBox.setSelected(true);
			italicCheckBox.setSelected(false);
			break;
		case (Font.ITALIC):
			boldCheckBox.setSelected(false);
			italicCheckBox.setSelected(true);
			break;
		case (Font.ITALIC + Font.BOLD):
			boldCheckBox.setSelected(true);
			italicCheckBox.setSelected(true);
			break;
		default:
			boldCheckBox.setSelected(false);
			italicCheckBox.setSelected(false);
			break;
		}
		
//		===SET EVENTS===
		fontCombo.addActionListener(this);
		sizeCombo.addActionListener(this);
		boldCheckBox.addActionListener(this);
		italicCheckBox.addActionListener(this);
		colorButton.addActionListener(this);
		OKButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		if (source == fontCombo || source == sizeCombo || source == boldCheckBox || source == italicCheckBox)
		{	//set font in Example Field
			exampleArea.setFont(getSelectedFont());
			pack();
		}
		else if (source == colorButton)
		{
			Color selectedColor = JColorChooser.showDialog(this, "Color", currentColor);
			exampleArea.setForeground(selectedColor);
		}
		else if (source == OKButton)
		{
			choosenOption = OK_OPTION;
			dispose();
		}
		else if (source == cancelButton)
		{
			choosenOption = CANCEL_OPTION;
			dispose();
		}
	}
	
	public Font getSelectedFont()
	{
		String selectedName = fontCombo.getSelectedItem().toString();
		int selectedSize = (int)sizeCombo.getSelectedItem();
		int selectedStyle;
		if (boldCheckBox.isSelected() && italicCheckBox.isSelected()) selectedStyle = Font.BOLD + Font.ITALIC;
		else if (boldCheckBox.isSelected()) selectedStyle = Font.BOLD;
		else if (italicCheckBox.isSelected()) selectedStyle = Font.ITALIC;
		else selectedStyle = Font.PLAIN;
		Font selectedFont = new Font(selectedName, selectedStyle, selectedSize);
		return selectedFont;
	}
	
	public Color getSelectedColor()
	{
		Color selectedColor = exampleArea.getForeground();
		return selectedColor;
	}
}