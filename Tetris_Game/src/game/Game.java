/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import framework.display.Window;
import framework.gamestates.GameStateManager;
import framework.resources.ResourceManager;
import game.states.MainMenu;

public class Game {
	
	public static final GameStateManager STATE_MANAGER = new GameStateManager();
	
	private static Timer timer;
	private static boolean running = false;
	
	public static void main(String[] args) {
		System.out.println("[Game][Main]: Starting...");
		
		ResourceManager.readImageFiles();
		Window.create();
		
		startGame();
		System.out.println("[Game][Main]: Started!");
	}
	
	/**Changes state to main menu<br>
	 * Starts timer (game loop)<br>
	 * Sets game to "running"
	 */
	private static void startGame() {
		STATE_MANAGER.changeState(new MainMenu());
		timer = new Timer(20, new GameLoop());
		running = true;
		timer.start();
	}
	
	/**Checks if the game is running*/
	public static boolean isRunning() {
		return running;
	}
	
	private static class GameLoop implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Game.STATE_MANAGER.tick();
		}
		
	}
}
