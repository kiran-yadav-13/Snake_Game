package snake.game;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
public class Board extends JPanel implements ActionListener{
	PreparedStatement preparedStatement;
	Statement statement;
	ResultSet result;
	String sql;
	String conUrl="jdbc:ucanaccess://C:\\Users\\Home\\Documents\\SnakeGameRecord.accdb";
	Connection con;
	int dots;
	static int count=0;
	private  Image apple;
	private  Image dot;
	private  Image head;
	private final int DOT_SIZE=10;
	private final int AllDots=900;  //300*300/10 total dots possible
	private int apple_x;
	private int apple_y;
	private final int RANDOM_POSITION=25;
	private final int x[]=new int[AllDots];
	private final int y[]=new int [AllDots];
	private Timer timer;
	private boolean left=false;
	private boolean right=true;
	private boolean up=false;
	private boolean down=false;
	private boolean inGame=true;
	FileOutputStream F1=null;
	FileInputStream f2=null;
		String msg1;
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	
 Board()
{
	
	setBackground(Color.BLACK);
	//setSize(new Dimension(300,300));
	
	setPreferredSize(new Dimension(300,300));
	addKeyListener(new MyKeyAdapter());
	setFocusable(true);
	loadImage();
	initGame();
	setBorder(BorderFactory.createLineBorder(Color.blue, 5));
}
 public void paintComponent(Graphics g)
 {
	 super.paintComponent(g);
	 draw(g);
 }
 public void draw(Graphics g)
 {
	 if(inGame)
	 {
		 g.drawImage(apple,apple_x,apple_y,this);
		 for(int i=0; i<dots; i++) {
			 if(i==0)
			 {
				 g.drawImage(head,x[i],y[i],this);
			 }
			 else
			 {
				 g.drawImage(dot, x[i],y[i],this);
			 }
		 }
		 Toolkit.getDefaultToolkit().sync();
	 }
	 else
	 {
		 gameOver( g);
	 }
	 
 }
 public int HighScore() {
	
		 
		 try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        	 con=DriverManager.getConnection(conUrl);

				if(con!=null) {
					System.out.println("Connected Sucessfully");
					sql="select Score from Score1 where id=1";
					statement=con.createStatement();
					result=statement.executeQuery(sql);
					if(result.next()) {
						int data= result.getInt("Score");
						if(count>data)
						{
							 sql="Update Score1 set Score='"+count+"' where id=1";
							 statement=con.createStatement();
							 preparedStatement=con.prepareStatement(sql);
								int  row=preparedStatement.executeUpdate();
								if(row>0)
								{
									
								  System.out.println("Records has been updated sucesssfuly Sucessfully");
								  return 1;
								}
							 
							 
						}
				}
					return 0;
				}
				else
				{
					System.out.println("No connection");
					return 0;
				}
		 
		 }catch(Exception e)
			{
				System.out.println(e);
			}
	 
	 return 0;
	
	
 }
 public void gameOver(Graphics g)
 {   msg1=" ";
	 String msg ="Game Over!!";
	 msg1="Your Score "+count;
	 Font font=new Font("SAN_SERIF",Font.BOLD,16);
	 
	int u=HighScore();
	 FontMetrics metrices=getFontMetrics(font);
	 g.setColor(Color.RED);
	 g.setFont(font);
	 g.drawString(msg, (300-metrices.stringWidth(msg))/2,280/2);
	 g.setColor(Color.YELLOW);
	 g.drawString(msg1, (300-metrices.stringWidth(msg))/2,280/2+20);
	 if(u==1)
	 {
		 msg="Waooh! HIGHSCORE";
		 g.setColor(Color.PINK);
		 g.drawString(msg, (300-metrices.stringWidth(msg))/2,280/2+45);
	 }
	 g.setColor(Color.MAGENTA);
	 msg1="Press 'y' or 'Y' to start again";
	 g.drawString(msg1, (240-metrices.stringWidth(msg))/2,280/2+65);
 }
 public void checkapple() 
 {
	 if((x[0]==apple_x) && (y[0]==apple_y))
	 {
		 dots++;
		 Locationapple();
		 count=count+10;
	 }
 }
 
 public void checkCollision()
 {
	for(int i=dots; i>0 ; i--)
	{
		if((i>4) && ( x[0]==x[i]) && (y[0]==y[i]))
		{
			inGame=false;
		}
	}
	 if(x[0] >= 295) {
		 inGame=false;
	 }
	 
	 if(y[0] >= 295) {
		 inGame=false;
	 }
	 if(x[0] <= 5) {
		 inGame=false;
	 }
	 if(y[0] <= 5) {
		 inGame=false;
	 }
	 if(!inGame)
	 {
		 timer.stop();
	 }
 }
 public void move()
 {
	 for(int i=dots; i>0; i--)
	 {
		 x[i]=x[i-1];
		 y[i]=y[i-1];
	 }
	 if(left) {
		 x[0] -= 10;
		 }
	 if(right) {
		 x[0] += 10;}
	 if(up) {
		 y[0] -= 10;}
	 if(down) {
		 y[0] += 10;}
	
 }
 public void actionPerformed(ActionEvent e) {
	 if(inGame)
	 {
		 checkapple();
		 checkCollision();
		 move();
	 }
	 repaint();
 }
public void loadImage(){
	ImageIcon i1= new ImageIcon(ClassLoader.getSystemResource("snake/game/Icons/apple.png"));
	apple=i1.getImage();
	ImageIcon i2= new ImageIcon(ClassLoader.getSystemResource("snake/game/Icons/dot.png"));
	dot=i2.getImage();
	ImageIcon i3= new ImageIcon(ClassLoader.getSystemResource("snake/game/Icons/head.png"));
	head=i3.getImage();
}
public void initGame()
{
	dots=3;
	for(int i=0; i<dots; i++ )
	{
		x[i]=50-DOT_SIZE*i; // 1st dot=50 ;  2nd dot=50-10; 3rd dot=50-2*10;
		y[i]=50;
	}
	Locationapple();
	timer=new Timer(120, this);
	timer.start();
}
public void Locationapple()
{
	int i=(int)(Math.random()*RANDOM_POSITION);  //0.6*20=12  random=0-1; randompos=29;  
	apple_x=i*DOT_SIZE;  // 30*10=300+10(1-290)
	i=(int)(Math.random()*RANDOM_POSITION);  
	apple_y=i*DOT_SIZE;
}
private class MyKeyAdapter extends KeyAdapter{
	
	public void keyPressed(KeyEvent k){
		int key=k.getKeyCode();
		char ch1=k.getKeyChar();
		if(key == KeyEvent.VK_LEFT && (!right))
		{
			left=true;
			up=false;
			down=false;
			
		}
		if(key == KeyEvent.VK_RIGHT && (!left))
		{
			right=true;
			up=false;
			down=false;
			
		}
		if(key == KeyEvent.VK_UP && (!down))
		{
			left=false;
			right=false;
			up=true;
			
		}
		if(key == KeyEvent.VK_DOWN && (!up))
		{
			left=false;
			right=false;
			down=true;
			
		}
		if(ch1=='y' || ch1=='Y')
		{
			new snake().setVisible(true);;
		}
}

	}
}

