package game.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

	private final int WIDTH = 20;
	private final int HEIGHT = 80;
	private final int BALL_WIDTH = 25;
	
	private Timer timer;
	private Random random;
	
	private int xCentre;
	private int yCentre;
	
	private int playerSpeedY;
	
	private boolean p1Up;
	private boolean p1Down;
	private boolean p2Up;
	private boolean p2Down;
	
	// Player 1
	private int player1X;
	private int player1Y;
	private Rectangle player1;
	private int player1Score;
	
	// Player 2
	private int player2X;
	private int player2Y;
	private Rectangle player2;
	private int player2Score;
		
	// Ball
	private int ballX;
	private int ballY;
	private Rectangle ball;
	private int ballSpeedX;
	private int ballSpeedY;
	private int randomDirection;
	
	private boolean isRunning;
	private boolean p1Won;
	private boolean p2Won;
	
	GamePanel() {
		setup();
		
		timer = new Timer(20, this);
		random = new Random();
		
		xCentre = (GameFrame.WIDTH / 2);
		yCentre = (GameFrame.HEIGHT / 2);
		
		playerSpeedY = 7;
		
		p1Up = false;
		p1Down = false;
		p2Up = false;
		p2Down = false;
		
		player1X = 0;
		player1Y = yCentre - (HEIGHT / 2);
		player1 = new Rectangle(player1X, player1Y, WIDTH, HEIGHT);
		player1Score = 0;
		
		player2X = GameFrame.WIDTH - WIDTH;
		player2Y = yCentre - (HEIGHT / 2);
		player2 = new Rectangle(player2X, player2Y, WIDTH, HEIGHT);
		player2Score = 0;
		
		ballSetup();
		ball = new Rectangle(ballX, ballY, BALL_WIDTH, BALL_WIDTH);
		
		isRunning = false;
		p1Won = false;
		p2Won = false;

		timer.start();
		
	}
	
	private void setup() {
		this.setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		this.setBackground(Color.LIGHT_GRAY);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(true);
		addKeyListener(this);
	}  
	
	private void ballSetup() {
		ballX = xCentre - (BALL_WIDTH / 2);
		ballY = yCentre - (BALL_WIDTH / 2);

		randomDirection = random.nextInt(2);
		if (randomDirection == 1) { 
			ballSpeedX = 8;
			ballSpeedY = 8;
		} else {
			ballSpeedX = -8;
			ballSpeedY = -8;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Centre line
		g.setColor(Color.black);
		g.drawLine(GameFrame.WIDTH / 2, 0, GameFrame.WIDTH / 2, GameFrame.HEIGHT);
				
		
		// Player 1
		g.setColor(Color.BLACK);
		g.fillRect(player1.x, player1.y, player1.width, player1.height);
		
		// Player 2
		g.setColor(Color.RED);
		g.fillRect(player2.x, player2.y, player2.width, player2.height);
		
		// Ball
		g.setColor(Color.green.darker());
		g.fillOval(ballX, ballY, BALL_WIDTH, BALL_WIDTH);
		
		// Score
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString(String.valueOf(player1Score), (GameFrame.WIDTH / 2) - 40, 20);
		g.drawString(String.valueOf(player2Score), (GameFrame.WIDTH / 2) + 25, 20);
		
		// Winner text
		g.setColor(Color.green.darker());
		if (p1Won) { 
			g.drawString("Player 1 won", xCentre - 50, yCentre - 20);
		} else if (p2Won) {
			g.drawString("Player 2 won", xCentre - 50, yCentre - 20);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			isRunning = true;
		
		if (e.getKeyCode() == KeyEvent.VK_W) {
			p1Up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			p1Down = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			p2Up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			p2Down = true;
		}
		repaint();
	}
	
	private void checkCollision() {
		if (player1.intersects(ball) || player2.intersects(ball)) {
			// Increase the speed
			ballSpeedX++;
			if (ballSpeedY > 0)
				ballSpeedY++;
			else
				ballSpeedY--;
			
			ballSpeedX *= -1;
		}	
		
		System.out.println("ballx: " + ballSpeedX);
		System.out.println("bally: " + ballSpeedY);
	}
	
	private void movePlayer() {
		if (p1Up) { 
			moveUp(player1, playerSpeedY);
		}
		else if (p1Down)
			moveDown(player1, playerSpeedY);
		
		if (p2Up)
			moveUp(player2, playerSpeedY);
		else if (p2Down)
			moveDown(player2, playerSpeedY);
	}
	
	private void moveUp(Rectangle player, int speed) {
		if (player.y <= 0)
			player.y = 0;
		
		player.y -= speed;
	}
	
	private void moveDown(Rectangle player, int speed) {
		if (player.y >= (GameFrame.HEIGHT - HEIGHT))
			player.y = GameFrame.HEIGHT - HEIGHT;
		
		player.y += speed;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			p1Up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			p1Down = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			p2Up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			p2Down = false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (isRunning) {
			moveBall();
			movePlayer();
			checkCollision();
			checkPlayer1Win();
			checkPlayer2Win();
			checkWinner();
		}
		repaint();
	}

	private void moveBall() {
		// Boundary check
		if (ballY <= 0 || ballY >= (GameFrame.HEIGHT - BALL_WIDTH))
			ballSpeedY *= -1;

		ballX += ballSpeedX;
		ballY += ballSpeedY;
		
		ball.x = ballX;
		ball.y = ballY;
	}
	
	private void checkPlayer1Win() {
		if (ballX >= (GameFrame.WIDTH - BALL_WIDTH)) {
			player1Score++;
			ballSetup();
		}
	}
	
	private void checkPlayer2Win() {
		if (ballX <= 0) {
			player2Score++;
			ballSetup();
		}
	}
	
	private void checkWinner() {
		if (player1Score >= 10) {
			p1Won = true;
			isRunning = false;
		} else if (player2Score >= 10) {
			p2Won = true;
			isRunning = false;
		}
	}

	
	@Override
	public void keyTyped(KeyEvent e) {}
	
}
