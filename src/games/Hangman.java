package games;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Hangman extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JButton ok, newGame;
	private JButton [] alphNum;
	private JLabel choose, guessing;
	private JTextField wordT;
	private final int totalGuesses = 6, wordLengthLimit = 20;
	private int guesses;
	private String wordS;
	
	public Hangman()
	{
		super("Hangman");
		
		ok = new JButton("Ok");
		ok.addActionListener(this);
		
		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		
		alphNum = new JButton [36];
		char c;
		for(int i = 0; i < alphNum.length; i++)
		{
			if(i < 26)
			{
				c = 'A';
				c += i;
			}
			else
			{
				c = '0';
				c += i - 26;
			}
			
			alphNum[i] = new JButton("" + c);
			alphNum[i].addActionListener(this);
		}
		
		guesses = totalGuesses;
		
		choose = new JLabel("Choose Word:");
		guessing = new JLabel("Guesses Left: " + guesses);
		
		wordT = new JTextField(10);
		
		chooseWord();
	}
	
	private void chooseWord()
	{
		reset();
		
		this.getContentPane().removeAll();
		this.validate();
		
		JPanel p = new JPanel();
		p.add(choose);
		p.add(wordT);
		p.add(ok);
		
		this.add(p);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void startGame(String word)
	{
		if(word.length() == 0)
		{
			JOptionPane.showMessageDialog(this, "No word was entered.");
			return;
		}
		else if(word.length() > wordLengthLimit)
		{
			JOptionPane.showMessageDialog(this, "Word is too long (max 10 characters). This word is " + word.length() + " characters.");
			return;
		}
		else if(!isValidWord(word))
		{
			JOptionPane.showMessageDialog(this, "Word contains invalid characters (only letters, numbers, and spaces are allowed).");
			return;
		}
		
		this.getContentPane().removeAll();
		this.validate();
		
		wordS = word;
		wordT.setText("");
		wordT.setEditable(false);
		wordT.setColumns(wordS.length());
		
		for(int i = 0; i < wordS.length(); i++)
		{
			if(wordS.charAt(i) == ' ')
				wordT.setText(wordT.getText() + " ");
			else
				wordT.setText(wordT.getText() + "*");
		}
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(6, 6));
		for(JButton b : alphNum)
			buttons.add(b);
		
		JPanel top = new JPanel();
		top.add(wordT);
		top.add(guessing);
		
		JPanel total = new JPanel();
		total.setLayout(new BorderLayout());
		total.add(top, BorderLayout.NORTH);
		total.add(buttons, BorderLayout.CENTER);
		total.add(newGame, BorderLayout.SOUTH);
		
		this.add(total);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void guess(String l)
	{
		for(JButton b : alphNum)
			if(b.getText().equals(l))
				b.setEnabled(false);
		
		char [] orig = wordS.toCharArray();
		char [] cs = wordS.toUpperCase().toCharArray();
		char [] cs2 = wordT.getText().toCharArray();
		
		boolean correct = false;
		
		for(int i = 0; i < cs.length; i++)
		{
			if(cs[i] == l.charAt(0))
			{
				cs2[i] = orig[i];
				correct = true;
			}
		}
		
		if(!correct)
			guesses--;
		
		guessing.setText("Guesses Left: " + guesses);
		wordT.setText(new String(cs2));
		
		if(didWin(wordT.getText()))
			showResult(true);
		else if(guesses == 0)
			showResult(false);
	}
	
	private void showResult(boolean b)
	{
		if(b)
			JOptionPane.showMessageDialog(this, "You win!");
		else
			JOptionPane.showMessageDialog(this, "You lose...");
		
		for(JButton bu : alphNum)
			bu.setEnabled(false);
	}
	
	private boolean didWin(String s)
	{
		char [] cs = s.toCharArray();
		
		for(char c : cs)
			if(c == '*')
				return false;
		
		return true;
	}
	
	private boolean isValidWord(String s)
	{
		char [] cs = s.toUpperCase().toCharArray();
		
		for(char c : cs)
		{
			if(c < 48 || c > 90 || (c > 57 && c < 65))
			{
				if(c == 32)
					continue;
				else
					return false;
			}
		}
		
		return true;
	}
	
	private void reset()
	{
		wordT.setText("");
		wordT.setColumns(10);
		wordT.setEditable(true);
		
		for(JButton b : alphNum)
			b.setEnabled(true);
		
		guesses = totalGuesses;
		guessing.setText("Guesses Left: " + guesses);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		
		if(o.equals(ok))
			startGame(wordT.getText());
		else if(o.equals(newGame))
			chooseWord();
		else
			guess(((JButton)o).getText());
	}
	
	public static void main(String [] args)
	{
		final JFrame f = new Hangman();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				f.setVisible(true);
			}
		});
	}
}
