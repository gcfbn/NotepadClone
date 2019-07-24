import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageFilter;
import java.awt.font.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NotepadClone extends JFrame implements ActionListener
{
	public static void main(String[] args)
	{
		NotepadClone app = new NotepadClone();
		app.addWindowListener(new WindowAdapter() 
		{
			   public void windowClosing(WindowEvent evt) 
			   {
			     app.exit();
			   }
		});
		app.setVisible(true);
	}
	
	private String fileName = null;
	private String fileDirectory = null;
	private boolean isEdited = false;
	
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JMenuBar menuMain;
	private JMenu menuFile, menuEdit, menuFormat, menuHelp;
	private JMenuItem itemNew, itemOpen, itemSave, itemSaveAs, itemExit;
	private JMenuItem itemFind, itemReplace, itemTimeDate;
	private JMenuItem itemFont;
	private JMenuItem itemHelp, itemAbout;
		
	NotepadClone()
	{
		setSize(1000, 600);
		setTitle("Notepad");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
//				===CREATE COMPONENTS===
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		menuMain = new JMenuBar();

		menuFile = new JMenu("File");
		
		itemNew = new JMenuItem("New");
		itemNew.setMnemonic('N');
		
		itemOpen = new JMenuItem("Open...");
		itemOpen.setMnemonic('O');
		
		itemSave = new JMenuItem("Save");
		itemSave.setMnemonic('S');
		
		itemSaveAs = new JMenuItem("Save As...");
		itemSaveAs.setMnemonic('A');
		
		itemExit = new JMenuItem("Exit");
		itemExit.setMnemonic('E');
		
		menuEdit = new JMenu("Edit");
		
		itemFind = new JMenuItem("Find...");
		itemFind.setMnemonic('F');
		
		itemReplace = new JMenuItem("Find and replace...");
		itemReplace.setMnemonic('R');
		
		itemTimeDate = new JMenuItem("Time/Date");
		itemTimeDate.setMnemonic('T');
		
		menuFormat = new JMenu("Format");
		
		itemFont = new JMenuItem("Font...");
		itemFont.setMnemonic('F');

		menuHelp = new JMenu("Help");

		itemHelp = new JMenuItem("Help");
		itemHelp.setMnemonic('H');
		
		
//				===EVENTS AND SHORTCUTS===
		textArea.getDocument().addDocumentListener(new DocumentListener()
				{
			@Override
			public void changedUpdate(DocumentEvent e) 
				{isEdited = true;}
			@Override
			public void removeUpdate(DocumentEvent e) 
				{isEdited = true;}
			@Override
			public void insertUpdate(DocumentEvent e)
				{isEdited = true;}
				});
		
		itemAbout = new JMenuItem("About the program...");
		itemAbout.setMnemonic('A');
		
		itemNew.addActionListener(this);
		itemNew.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		
		itemOpen.addActionListener(this);
		itemOpen.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		
		itemSave.addActionListener(this);
		itemSave.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		
		itemSaveAs.addActionListener(this);
		itemSaveAs.setAccelerator(KeyStroke.getKeyStroke("ctrl alt N"));
		
		itemExit.addActionListener(this);
		itemExit.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
		
		itemFind.addActionListener(this);
		itemFind.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
		
		itemReplace.addActionListener(this);
		itemReplace.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		
		itemTimeDate.addActionListener(this);
		itemTimeDate.setAccelerator(KeyStroke.getKeyStroke("F5"));
		
		itemHelp.addActionListener(this);
		itemHelp.setAccelerator(KeyStroke.getKeyStroke("F1"));
		
		itemAbout.addActionListener(this);
		
//				===ADD COMPONENTS===
		add(scrollPane, BorderLayout.CENTER);
		add(menuMain, BorderLayout.PAGE_START);
		
		menuMain.add(menuFile);
		menuFile.add(itemOpen);
		menuFile.add(itemNew);
		menuFile.add(itemSave);
		menuFile.add(itemSaveAs);
		menuFile.addSeparator();
		menuFile.add(itemExit);
		
		menuMain.add(menuEdit);
		menuEdit.add(itemFind);
		menuEdit.add(itemReplace);
		menuEdit.addSeparator();
		menuEdit.add(itemTimeDate);
		
		menuMain.add(menuFormat);
		menuFormat.add(itemFont);
		menuMain.add(menuHelp);
		menuHelp.add(itemHelp);
		menuHelp.add(itemAbout);
	}
	
//				===CALL EVENTS===
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		if (source == itemNew)
			createNewFile();
		else if (source == itemOpen)
			openFile();
		else if (source == itemSave)
			saveFile();
		else if (source == itemSaveAs)
			saveFileAs();
		else if (source == itemExit)
			exit();
		else if (source == itemFind)
			find();
		else if (source == itemReplace)
			replace();
		else if (source == itemTimeDate)
			addTimestamp();
		else if (source == itemHelp)
			help();
		else if (source == itemAbout)
			aboutProgram();
	}
	
//				===FUNCTIONS CALLED BY EVENTS===
	
	private void createNewFile()
	{
		if (textArea.getText().equals(""))
			clearFile();
		else
		{
			int answer = JOptionPane.showConfirmDialog(this, "Do you want to save this file?", "Save file", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (answer)
			{
				case JOptionPane.YES_OPTION:
				{
					if (fileName == null) saveFileAs();
					else saveFile();
				}
				case JOptionPane.NO_OPTION:
					clearFile();
			}
		}
		isEdited = false;
	}
	
	private void openFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Text Files (.txt)", "txt");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if(isEdited == true)
		{
			int answer = JOptionPane.showConfirmDialog(this, "Do you want to save your file?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
			if (answer == JOptionPane.YES_OPTION) saveFile();
			else if (answer == JOptionPane.CANCEL_OPTION) return;
		}
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			File choosenFile = fileChooser.getSelectedFile();
			Scanner stream = null;
			try 
			{
				textArea.setText("");
				stream = new Scanner(choosenFile);
				while (stream.hasNext())
					textArea.append(stream.nextLine() + "\n");
				fileDirectory = choosenFile.getAbsolutePath();
				fileName = choosenFile.getName();
				setTitle(fileName);
			} 
			catch (FileNotFoundException e1) 
			{
				JOptionPane.showMessageDialog(null, "Opening file failed!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				if (stream != null)
					stream.close();
			}
			isEdited = false;
		}
	}
	
	private void saveFile()
	{
		if (fileName != null && isEdited == true)
		{
			File currentFile = new File(fileDirectory);
			PrintWriter stream = null;
			Scanner scanner = null;
			try 
			{
				scanner = new Scanner(textArea.getText());
				stream = new PrintWriter(currentFile);
				while(scanner.hasNext())
					stream.println(scanner.nextLine());
			} 
			catch (FileNotFoundException e) 
			{
				JOptionPane.showMessageDialog(null, "An error has occured!\nFile has not been saved.", "Error", JOptionPane.ERROR_MESSAGE);
			}	
			finally
			{
				if (scanner != null)
					scanner.close();
				if (stream != null)
					stream.close();
			}
			isEdited = false;
		}
		else saveFileAs();
	}
	
	private void saveFileAs()
	{
		JFileChooser fileChooser = new JFileChooser()
				{
					@Override
					public void approveSelection()
					{
						File choosenFile = getSelectedFile();
						if (choosenFile.exists() && getDialogType() == SAVE_DIALOG)
						{
							int result = JOptionPane.showConfirmDialog(null, "This file already exists." + "\n" + "Are you sure to overwrite it?", "Confirm", JOptionPane.YES_NO_OPTION);
							if (result == JOptionPane.YES_OPTION)
								super.approveSelection();
						}
						else if (!choosenFile.exists() && getDialogType() == SAVE_DIALOG)
						{
							super.approveSelection();
						}
					}
				};
		FileFilter filter = new FileNameExtensionFilter("Text Files (.txt)", "txt");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			File choosenFile = fileChooser.getSelectedFile();
			String choosenFilePath = choosenFile.getAbsolutePath().toString();
			if (!choosenFilePath.endsWith(".txt"))
				choosenFile = new File(choosenFilePath + ".txt");
			
			fileName = choosenFile.getName();
			fileDirectory = choosenFile.getAbsolutePath();
			setTitle(fileName);
			
			PrintWriter stream = null;
			Scanner scanner = null;
			try 
			{
				scanner = new Scanner(textArea.getText());
				stream = new PrintWriter(choosenFile);
				while(scanner.hasNext()) 
					stream.println(scanner.nextLine());
			} 
			catch (FileNotFoundException e) 
			{
				JOptionPane.showMessageDialog(null, "An error has occured!\nFile has not been saved.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				if (stream != null) 
					stream.close();
				if (scanner != null) 
					scanner.close();
			}
			isEdited = false;
		}
	}
	
	private void exit()
	{
		if ((fileName != null || !textArea.getText().contentEquals("")) && isEdited == true)
		{
			int ans = JOptionPane.showConfirmDialog(this, "Save this file?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (ans)
			{
				case JOptionPane.YES_OPTION:
				{
					if (fileName == null) saveFileAs();
					else saveFile();
					setDefaultCloseOperation(EXIT_ON_CLOSE);
					dispose();
					
				}
				case JOptionPane.NO_OPTION:
				{
					setDefaultCloseOperation(EXIT_ON_CLOSE);
					dispose();
				}
			}
		}
		else 
		{
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			dispose();
		}
	}
	
	private void find()
	{
		JDialog findWindow;
		JLabel findLabel;
		JTextField textToFindField;
		JButton findButton, cancelButton;
		JCheckBox startAtBeginningBox;
		
//				set new window in the middle of the main window
		findWindow = new JDialog();
		{
			
			int findWindowWidth = 300, findWindowHeight = 150;
			int findWindowX = getX() + getWidth()/2 - findWindowWidth/2;
			int findWindowY = getY() + getHeight()/2 - findWindowHeight/2;
			findWindow.setBounds(findWindowX, findWindowY, findWindowWidth, findWindowHeight);
			
			findWindow.setTitle("Finding");
			findWindow.setResizable(false);
			findWindow.setVisible(true);
		}
		
//				===CREATE COMPONENTS===
		findLabel = new JLabel("Find: ");
		findLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		findLabel.setBounds(10, 20, 50, 15);
		
		textToFindField = new JTextField();
		textToFindField.setBounds(45, 15, 230, 25);
		
		findButton = new JButton("Find next");
		findButton.setBounds(25, 70, 120, 40);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(150, 70, 120, 40);
		
		startAtBeginningBox = new JCheckBox("Start at the beginning");
		startAtBeginningBox.setBounds(25, 45, 200, 20);
		
		
//				===SET EVENTS===
		findButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String text = textArea.getText();
				String toFind = textToFindField.getText();
				int start;
				if (startAtBeginningBox.isSelected())
				{
					start = 0;
					startAtBeginningBox.setSelected(false);
				}
					
				else
					start = textArea.getCaretPosition();
				int index = text.indexOf(toFind, start);
				if (index != -1)
					textArea.select(index, index + toFind.length());
				else
					JOptionPane.showMessageDialog(findWindow, "Can't find \"" + toFind + "\".", "Notepad",JOptionPane.ERROR_MESSAGE);
			}
		});
		
		cancelButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {findWindow.dispose();}
		});
		
//			===ADD COMPONENTS===
		findWindow.add(findLabel);
		findWindow.add(textToFindField);
		findWindow.add(findButton);
		findWindow.add(cancelButton);
		findWindow.add(startAtBeginningBox);
	}
	
	private void replace()
	{
		JDialog replaceWindow;
		JLabel findLabel, replaceForLabel;
		JTextField textToFindField, textToReplaceForField;
		JButton replaceNextButton, replaceAllButton, cancelButton;
		JCheckBox startAtBeginningBox;
		
//				set new window in the middle of the main window
		replaceWindow = new JDialog();
		{
			replaceWindow.setLayout(new BorderLayout());
			int replaceWindowWidth = 400, replaceWindowHeight = 200;
			int replaceWindowX = getX() + getWidth()/2 - replaceWindowWidth/2;
			int replaceWindowY = getY() + getHeight()/2 - replaceWindowHeight/2;
			replaceWindow.setBounds(replaceWindowX, replaceWindowY, replaceWindowWidth, replaceWindowHeight);
			
			replaceWindow.setTitle("Replacing");
			replaceWindow.setResizable(false);
			replaceWindow.setVisible(true);
		}
		
//				===CREATE COMPONENTS===
		findLabel = new JLabel("Find: ");
		findLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		findLabel.setBounds(20, 20, 100, 20);
		
		textToFindField = new JTextField();
		textToFindField.setBounds(110, 15, 270, 30);
		
		replaceForLabel = new JLabel("Replace for: ");
		replaceForLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		replaceForLabel.setBounds(20, 60, 100, 20);
		
		textToReplaceForField = new JTextField();
		textToReplaceForField.setBounds(110, 55, 270, 30);
		
		replaceNextButton = new JButton("Replace next");
		replaceNextButton.setBounds(10, 120, 110, 50);
		
		replaceAllButton = new JButton("Replace all");
		replaceAllButton.setBounds(140, 120, 110, 50);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(270, 120, 110, 50);
		
		startAtBeginningBox = new JCheckBox("Start at the beginning");
		startAtBeginningBox.setBounds(15, 90, 200, 20);
		
//				===SET EVENTS===
		replaceNextButton.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						String text = textArea.getText();
						String toFind = textToFindField.getText();
						String toReplaceFor = textToReplaceForField.getText();
						
						int start;
						if (startAtBeginningBox.isSelected())
						{
							start = 0;
							startAtBeginningBox.setSelected(false);
						}
							
						else
							start = textArea.getCaretPosition();
						int index = text.indexOf(toFind, start);
						if (index != -1)
						{
							textArea.replaceRange(toReplaceFor, index, index + toFind.length());
							textArea.setCaretPosition(index + toFind.length());
						}
						else
							JOptionPane.showMessageDialog(replaceWindow, "Can't find \"" + toFind + "\".", "Notepad", JOptionPane.ERROR_MESSAGE);
					}
				});
		
		replaceAllButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						String text = textArea.getText();
						String toFind = textToFindField.getText();
						String toReplaceFor = textToReplaceForField.getText();
						
						int start;
						if (startAtBeginningBox.isSelected())
							start = 0;
						else
							start = textArea.getCaretPosition();
						
						int replacements = 0;
						while (text.indexOf(toFind, start) != -1)
						{
							int index = text.indexOf(toFind, start);
							replacements++;
							textArea.replaceRange(toReplaceFor, index, index + toFind.length());
							start = index + toFind.length() + 1;
						}
						if (replacements == 0)
							JOptionPane.showMessageDialog(replaceWindow, "Can't find \"" + toFind + "\".", "Notepad", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(replaceWindow, "Replaced " + replacements + " occurrings.", "Notepad", JOptionPane.INFORMATION_MESSAGE);
					}
				});
		
		cancelButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {replaceWindow.dispose();}
				});
		
//				===ADD COMPONENTS===
		replaceWindow.add(findLabel);
		replaceWindow.add(textToFindField);
		replaceWindow.add(replaceForLabel);
		replaceWindow.add(textToReplaceForField);
		replaceWindow.add(replaceNextButton);
		replaceWindow.add(replaceAllButton);
		replaceWindow.add(cancelButton);
		replaceWindow.add(startAtBeginningBox);
	}
	
	private void addTimestamp()
	{
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		textArea.append(timestamp + " ");
	}
	
	private void help()
	{
		URL url = null;
		try 
		{
			url = new URL("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
		} 
		catch (MalformedURLException e1) 
		{
			e1.printStackTrace();
		}
//				open WebPage
		{ 					
			URI uri = null;
			try 
			{
				uri = url.toURI();
			} 
			catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
			
			Desktop desktop = Desktop.getDesktop();
			try 
			{
				desktop.browse(uri);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private void aboutProgram()
	{
		JOptionPane.showMessageDialog(this, "Notepad\n" 
											+ "Version: 1.0\n"
											+ "Author: Bartosz Mêkarski\n"
											+ "Source Code: https://github.com/gcfbn/NotepadClone\n",
										"About Me", JOptionPane.INFORMATION_MESSAGE); 
	}

	private void clearFile()
	{
		textArea.setText(null);
		fileName = null;
		fileDirectory = null;
		setTitle("Notepad");
	}
}