/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package framework.gamestates;

import java.awt.Graphics;
import java.util.Stack;

public class GameStateManager {
    private Stack<GameState> gamestatesStack;
    
    //Init Manager
    public GameStateManager(){
        this.gamestatesStack= new Stack<>();
        System.out.println("[Framework][GameStates]: Created game state manager");
    }
    
    //Adds the state to the stack
    public void changeState(GameState state){
        this.gamestatesStack.add(state);
    }
    
    //Removes the current state from the stack
    public void backToPrevious(){
        this.gamestatesStack.pop();
    }
    
    //Clears the stack by removing the game states
    public void clearStack(){
        this.gamestatesStack.clear();
    }
    
    //Calls GameState init on the first state of the stack
    public void init(){
        this.gamestatesStack.peek().init();
    }
    
    //Calls GameState tick on the first state of the stack
    public void tick(){
        this.gamestatesStack.peek().tick();
    }
    
    //Calls GameState render on the first state of the stack
    public void render(Graphics graphics){
        this.gamestatesStack.peek().render(graphics);
    }
    
    //Calls GameState keyPressed on the first state of the stack
    public void keyPressed(int key){
        this.gamestatesStack.peek().keyPressed(key);
    }
    
    //Calls GameState keyReleased on the first state of the stack
    public void keyReleased(int key){
        this.gamestatesStack.peek().keyReleased(key);
    }
}
