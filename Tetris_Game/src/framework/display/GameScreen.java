/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package framework.display;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import game.Game;

public class GameScreen extends JPanel {

	/**Serial*/
	private static final long serialVersionUID = 1L;

	/**Creates screen<br>
	 * Used in window manager
	 */
	public GameScreen() {
		super();
		this.setFocusable(true);
		this.addKeyListener(new Keyboard());
		this.requestFocusInWindow();
		System.out.println("[Framework][Display]: Created game screen");
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(Game.isRunning())
			Game.STATE_MANAGER.render(g);
		
		super.repaint();
	}
	
	private static class Keyboard implements KeyListener {

		@Override
		public void keyPressed(KeyEvent key) {
			if(Game.isRunning())
				Game.STATE_MANAGER.keyPressed(key.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent key) {
			if(Game.isRunning())
				Game.STATE_MANAGER.keyReleased(key.getKeyCode());
		}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
}