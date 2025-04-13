/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package framework.gamestates;

import java.awt.Graphics;

public abstract class GameState {
    
    // Creates a game state
        public GameState(){
            this.init();
        }
        
        // Called when state is created
        protected abstract void init();
        
        //Should handle the game logic
        public abstract void tick();
        
        //Renders everything that should be rendered in this state
        public abstract void render(Graphics graphics);
        
        //Performs an action when the key is pressed
        public abstract void keyPressed(int key);
        
        //Performs an action when the key is released
        public abstract void keyReleased(int key);
}
