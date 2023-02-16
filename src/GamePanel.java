import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
	
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int applesX;
	int applesY;
	char direction = randInt(1,2);
	// R -> Right
	// U -> Up
	// D -> Down
	// L -> Left
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		 random = new Random();
		 this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		 this.setBackground(Color.black);
		 this.setFocusable(true);
		 this.addKeyListener(new MyKeyAdapter());
		 startGame();
	}
	
	public static char randInt(int min, int max) {
		Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    if (randomNum == 1) {
	    	return 'R';
	    }
	    if (randomNum == 2) {
	    	return 'D';
	    }
	    return ' ';
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	} 
	
	public void draw(Graphics g) {
		
		if (running) { 
			 g.setColor(Color.red);
			 g.fillOval(applesX, applesY, UNIT_SIZE, UNIT_SIZE);
			 
			 for (int i =0 ; i < bodyParts ; i++ ) {
				 if (i==0) {
					 g.setColor(new Color(0,0,204));
					 g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				 }
				 else {
					 g.setColor(new Color(0,0,102));
					 
//					 g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					 
					 g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				 }
			 }
				 g.setColor(Color.green);
				 g.setFont(new Font("Ink Free",Font.BOLD,25));
				 FontMetrics metrics = getFontMetrics(g.getFont());
				 g.drawString("Score: "+ applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: "+ applesEaten))/2 , g.getFont().getSize());
					
		}
		else {
			gameOver(g);
		}
	} 
	
	public void newApple() {
		applesX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		applesY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	} 
	
	public void move() {
		 for (int i = bodyParts ; i >0;i--) {
			 x[i] = x[i-1];
			 y[i] = y[i-1];
		 }
		 
		 switch (direction) {
		 case 'U':
			 y[0] = y[0] - UNIT_SIZE;
			 break;
		 case 'D':
			 y[0] = y[0] + UNIT_SIZE;
			 break;
		 case 'L':
			 x[0] = x[0] - UNIT_SIZE;
			 break;
		 case 'R':
			 x[0] = x[0] + UNIT_SIZE;
			 break;
		 }
	} 
	

	public void checkApple() {
		 if ((x[0]==applesX)&&(y[0]==applesY)) {
			 bodyParts++;
			 applesEaten++;
			 newApple();
		 }
	} 
	
	public void checkCollisions() {
		// Checks if head collides with body
		 for (int i = bodyParts ; i > 0 ; i--) {
			 if ((x[0]==x[i])&&(y[0]==y[i])) {
				 running = false;
			 }
		 }
		// Checks if head collides with the border
//		 if (x[0] < 0 ) {
//			 running = false;
//		 }
//		 if (x[0] > SCREEN_WIDTH ) {
//			 running = false;
//		 }
//		 if (y[0] < 0 ) {
//			 running = false;
//		 }
//		 if (y[0] > SCREEN_HEIGHT ) {
//			 running = false;
//		 }
//		 
//		 if (!running) {
//			 timer.stop();
//		 }
		 
		 // No Collisions with the border
		 if (x[0] < 0 ) {
			 x[0] = SCREEN_WIDTH;
		 }
		 if (x[0] > SCREEN_WIDTH ) {
			 x[0] = 0;
		 }
		 if (y[0] < 0 ) {
			 y[0] = SCREEN_HEIGHT;
		 }
		 if (y[0] > SCREEN_HEIGHT ) {
			 y[0] = 0;
		 }
		 
		 if (!running) {
			 timer.stop();
		 }
		 
	} 
	
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER",(SCREEN_WIDTH - metrics1.stringWidth("GAME OVER"))/2 , SCREEN_HEIGHT/2);
		g.setColor(Color.green);
		g.setFont(new Font("Ink Free",Font.BOLD,25));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+ applesEaten,(SCREEN_WIDTH - metrics2 .stringWidth("Score: "+ applesEaten))/2 , g.getFont().getSize());
			
	} 
	@Override
	public void actionPerformed(ActionEvent e) {
		 
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint(); 
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			 switch (e.getKeyCode()) {
			 case KeyEvent.VK_LEFT:
				 if (direction != 'R') {
					 direction = 'L';
				 }
				 break;
				 
			 case KeyEvent.VK_RIGHT:
				 if (direction != 'L') {
					 direction = 'R';
				 }
				 break;

			 case KeyEvent.VK_DOWN:
				 if (direction != 'U') {
					 direction = 'D';
				 }
				 break;

			 case KeyEvent.VK_UP:
				 if (direction != 'D') {
					 direction = 'U';
				 }
				 break;
			 }
		}
	}

}
