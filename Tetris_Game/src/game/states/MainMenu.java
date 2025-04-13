/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.states;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import framework.display.Window;
import framework.gamestates.GameState;
import framework.resources.ResourceManager;
import game.Game;
import java.awt.Color;

public class MainMenu extends GameState{
    protected String[] options;
    protected int selected;
    
    @Override
    protected void init(){
        this.options= new String[] {"Start Game", "Highscores", "Quit Game"};
        this.selected= 0;
        System.out.println("[Game][States]: Created main menu");
    }
    
    @Override
    public void tick(){}
    
    @Override
    public void render(Graphics graphics){
        this.drawBackdround(graphics);
        this.drawButtons(graphics);
        this.drawOptions(graphics);
    }
    
    @Override
    public void keyPressed(int key){
        if(key == KeyEvent.VK_UP){
            if(this.selected > 0) this.selected--;
        }
        else if(key == KeyEvent.VK_DOWN){
            if(this.selected < this.options.length-1) this.selected++;
        }
        else if(key == KeyEvent.VK_ENTER){
            if(this.selected == 0){
                Game.STATE_MANAGER.changeState(new PlayingState());
            }else if(this.selected == 1){
                Game.STATE_MANAGER.changeState(new HighscoresMenu());
            }else if(this.selected == 2){
                System.exit(0);
            }
        }
    }
    
    @Override
    public void keyReleased(int key){}
    
    //Fills the background with a color
    private void drawBackdround(Graphics graphics){
        graphics.setColor(new Color(10, 10, 30));
        graphics.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
    }
    
    //Draws the menu Options
    private void drawOptions(Graphics graphics){
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        for(int i=0; i< options.length; i++){
            if(selected == i)
                graphics.setColor(Color.GREEN);
            else 
                graphics.setColor(Color.WHITE);
            
            graphics.drawString(options[i], Window.WIDTH/3, 190 + 100 * i);
        }
    }
    
    //Draws the buttons and the logo
    private void drawButtons(Graphics graphics){
        graphics.drawImage(ResourceManager.texture("logo.png"), Window.WIDTH/3- 40, 20, 180, 120, null);
        for(int i=0; i<options.length; i++){
            graphics.drawImage(ResourceManager.texture("menu_button.png"), Window.WIDTH/3-30, 160+ 100 * i, 160, 50, null);
        }
    }
}
