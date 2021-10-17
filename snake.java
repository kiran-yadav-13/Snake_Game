package snake.game;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class snake extends JFrame {

	snake()
	{
		Board b=new Board();
		add(b);
		pack();
		setLocation(500,300);  // set Frame Location on screen
		//setLocationRelativeTo(null);  // set Location of Frame in middle
		setTitle("SnakeGame");
		setResizable(false);  // prevent the window to resize
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
new snake().setVisible(true);
	}

}
