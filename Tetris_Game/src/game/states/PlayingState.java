/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.states;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.concurrent.ArrayBlockingQueue;

import framework.display.Window;
import framework.gamestates.GameState;
import framework.resources.ResourceManager;
import framework.utils.MathHelper;
import game.Game;
import game.pieces.Grid;
import game.pieces.Square;
import game.pieces.Tetromino;
import java.awt.Color;

public class PlayingState extends GameState{
    private Grid grid;
    
    private int pieceFallTime;
    private int moveDownDelay;
    
    private boolean foundFullLine;
    
    private int currentRotationOriginX;
    private int currentRotationOriginY;
    private Tetromino.Rotation currentRotation;
    
    private ArrayBlockingQueue<Tetromino> queue;
    private Tetromino currentFallingTetromino;
    
    private int lines;
    private  int level;
    
    private Tetromino hold;
    
    private boolean lost;
    
    @Override
    protected void init(){
        this.grid= new Grid();
        this.pieceFallTime= 50;
        this.moveDownDelay = 0;
        this.foundFullLine= false;
        
        this.queue = new ArrayBlockingQueue<>(4);
        this.queue.add(Tetromino.LIST.get(MathHelper.randomInt(Tetromino.LIST.size())));
        this.queue.add(Tetromino.LIST.get(MathHelper.randomInt(Tetromino.LIST.size())));
        this.queue.add(Tetromino.LIST.get(MathHelper.randomInt(Tetromino.LIST.size())));
        
        this.placeTetromino();
        
        this.lines= 0;
        this.lines =1;
        
        this.hold= null;
       
        this.lost= false;
        
        System.out.println("[Game][States]: Created playing state");
    }
    
    @Override
    public void tick(){
        if(!this.lost){
            this.movePiecesDown();
            this.stopPieces();
            
            this.findFullLine();
            if(this.foundFullLine)
                this.grid.hardDrop();
            
            this.nextPiece();
        }
    }
    
    @Override
    public void render(Graphics graphics){
        this.drawBackground(graphics);
        this.drawGrid(graphics);
        this.drawQueue(graphics);
        
        if(this.lost)
            this.drawGameOverMessage(graphics);
    }
    
    @Override
    public void keyPressed(int key){
        if(key == KeyEvent.VK_RIGHT){
            if(this.grid.allSquaresCanGoRight() && !foundFullLine && !lost){
                this.grid.movePiecesRight(); //Right
                this.currentRotationOriginX++;
            }
        }
        else if(key == KeyEvent.VK_LEFT){
            if(this.grid.allSquaresCanGoLeft() && !foundFullLine && !lost){
                this.grid.movePiecesLeft(); // Left
                this.currentRotationOriginX--;
            }
        }
        
        else if(key == KeyEvent.VK_DOWN){
            if(this.grid.allSquaresCanFall() && !foundFullLine && !lost){
                this.grid.movePiecesDown(); //SoftDrop
                this.currentRotationOriginY++;
            }
        }
        
        else if(key == KeyEvent.VK_UP){
            if(!foundFullLine && !lost)
                this.rotateClosewise(); //Rotate
        }
        
        else if(key == KeyEvent.VK_C || key == KeyEvent.VK_Z){
            if(!foundFullLine && !lost)
                this.rotateCounterclockwise(); //Rotate
        }
        else if(key == KeyEvent.VK_SPACE){
            if(!lost)
                this.grid.hardDrop(); //Hard drop
        }
        else if(key == KeyEvent.VK_H){
            if(!lost)
                this.hold(); //Hold
        }
        else if(key == KeyEvent.VK_ESCAPE){
            if(!lost){
                Game.STATE_MANAGER.changeState(new PauseMenu());
            }else {
                int [] scores= ResourceManager.readHighscoresFile();
                if(this.lines > scores[8]){
                    scores[8] = this.lines;
                    MathHelper.sortArray(scores);
                    ResourceManager.writeHighscores(scores);
                }
                Game.STATE_MANAGER.backToPrevious(); // Reset
            }
        }
    }
    
    @Override
    public void keyReleased(int key) {}
    
    //If they can't move down set them to 'to be stopped'
    private void movePiecesDown(){
        if(this.moveDownDelay == this.pieceFallTime){
            if(this.grid.allSquaresCanFall()){
                this.grid.movePiecesDown();
                this.currentRotationOriginY++;
            }
            
            if(!this.grid.allSquaresCanFall()){
                this.grid.setAllSquaresToBeStopped();
            }
            this.moveDownDelay = 0;
        }
        if(this.moveDownDelay < this.pieceFallTime) this.moveDownDelay++;
    }
    
    //IF they are set to "to be stopped' calls setFixed()
    private void stopPieces(){
        for(int i=0; i<Grid.LINES; i++){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.grid.getLine(i)[j] != null){
                    if(this.grid.getLine(i)[j].isStopping()){
                        this.grid.getLine(i)[j].setFixed();
                    }
                }
            }
        }
    }
    
    //Check if all square in the grid are fixed, then take another tetromino and place it on the grid
    private void nextPiece(){
        if(this.grid.allSquaresAreFixed()){
            this.moveDownDelay= 0;
            this.foundFullLine= false;
            this.placeTetromino();
        }
    }
    
    //If all the squares are fixed start looking for a full line
    private void findFullLine(){
        if(this.grid.allSquaresAreFixed()){
            boolean thisLine = false;
            for(int i=0; i<Grid.LINES; i++){
                for(Square square : this.grid.getLine(i)){
                    thisLine= true;
                    if(square == null){
                        thisLine = false;
                        break; // Not complete
                    }
                }
                
                if(thisLine){
                    this.foundFullLine= true;
                    this.grid.removeLine(i);
                    
                    this.lines++;
                    if(this.lines%10 == 0){
                        this.pieceFallTime -=5;
                        this.level++;
                    }
                    
                    this.findFullLine();
                }
            }
        }
    }
    
    //Fills the background with a color and draws the rectangle for the queue
    private void drawBackground(Graphics graphics ){
        graphics.setColor(new Color(10,10,30));
        graphics.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
        
        graphics.setColor(new Color(26, 26, 79));
        graphics.drawRoundRect(210, 30, 70, 210, 5, 5);
        graphics.drawRoundRect(210, 330, 70, 70, 5, 5);
        
        graphics.setFont(new Font("arial", Font.PLAIN, 16));
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawString("Next:",210, 30);
        graphics.drawString("Lines: "+ this.lines, 210, 270);
        graphics.drawString("Level: "+ this.level, 210, 290);
        graphics.drawString("Hold:", 210, 330);
    }
    
    //draws the grid
    private void drawGrid(Graphics graphics){
        for(int i=2; i<Grid.LINES; i++){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                Square square = this.grid.getLine(i)[j];
                if(square !=null){
                    if(!square.isFixed())
                        graphics.drawImage(ResourceManager.texture("block_"+square.getColor()+ ".png"), j*20, i*20-40, 20, 20, null);
                    else
                        graphics.drawImage(ResourceManager.texture("block_"+square.getColor()+ "_dark.png"), j*20, i*20-40, 20, 20, null);
                }
                else{
                    graphics.drawImage(ResourceManager.texture("block_void.png"), j*20, i*20-40, 20, 20, null);
                }
            }
        }
    }
    
    //Draws the three tetrominoes of the queue
    private void drawQueue(Graphics graphics){
        int p=0;
        for(Tetromino tetromino : this.queue){
            for(int i=0; i<tetromino.getSize(); i++){
                for(int j=0; j<tetromino.getSize(); j++){
                    Square square = tetromino.getSquareAt(Tetromino.Rotation.ROT0, i, j);
                    if(square !=null)
                        graphics.drawImage(ResourceManager.texture("block_"+square.getColor()+".png"), 220+j*15, 40+p*70+i*15, 15, 15 , null);
                }
            }
            p++;
        }
        
        if(this.hold != null){
            for(int i=0; i<this.hold.getSize(); i++){
                for(int j=0; j<this.hold.getSize(); j++){
                    Square square = this.hold.getSquareAt(Tetromino.Rotation.ROT0, i, j);
                    if(square != null)
                      graphics.drawImage(ResourceManager.texture("block_"+square.getColor()+".png"), 220+j*15, 340+i*15, 15, 15 , null);  
                }
            }
        }
    }
    
    //Writes 'Game Over' on the screen
        private void drawGameOverMessage(Graphics graphics){
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, Window.HEIGHT/2-30, Window.WIDTH, 70);
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("Arial", Font.PLAIN, 30));
            graphics.drawString("Game Over!", 15 ,Window.HEIGHT/2);
            graphics.setFont(new Font("Arial", Font.PLAIN, 14));
            graphics.drawString("Press esc to return to title screen", 15, Window.HEIGHT/2+30);
        }
        
        //Takes the next Tetromino from the queue and place it on the grid
        private void placeTetromino(){
            try{
                this.currentFallingTetromino= this.queue.poll();
                this.currentRotationOriginX =3;
                this.currentRotationOriginY= this.grid.findEmptyLine(2, this.currentFallingTetromino);
                this.currentRotation= Tetromino.Rotation.ROT0;
                this.grid.placeTetromino(currentFallingTetromino, currentRotationOriginX, currentRotationOriginY, currentRotation);
                this.queue.add(Tetromino.LIST.get(MathHelper.randomInt(Tetromino.LIST.size())));
            }catch(ArrayIndexOutOfBoundsException e){
                this.lost = true;
            }
        }
        
        //If the falling tetromino can be rotated removes it from the grid and then place the rotated  one
        private void rotateClosewise(){
            this.fixRotationOrigin();
            
            if(this.grid.canPlaceHere(currentFallingTetromino, currentRotationOriginX, currentRotationOriginY, currentRotation.rotateClosewise())){
                this.grid.removeTetromino();
                this.currentRotation= this.currentRotation.rotateClosewise();
                this.grid.placeTetromino(currentFallingTetromino, currentRotationOriginX, currentRotationOriginY, currentRotation);
            }
        }
        
        //If falling tetromino can be rotated removes it from the grid and then place the rotated one
        private void rotateCounterclockwise(){
            this.fixRotationOrigin();
            
            if(this.grid.canPlaceHere(currentFallingTetromino, currentRotationOriginX, currentRotationOriginY, currentRotation.rotateCounterclockwise())){
                this.grid.removeTetromino();
                this.currentRotation= this.currentRotation.rotateCounterclockwise();
                this.grid.placeTetromino(currentFallingTetromino, currentRotationOriginX, currentRotationOriginY, currentRotation);
            }
        }
        
        //Changes the value of currentRotationOrigin so when the tetromino is rotated it doesn't go offscreen
        private void fixRotationOrigin(){
            if(this.currentRotationOriginX < 0)
                this.currentRotationOriginX = 0;
            if(this.currentRotationOriginX + this.currentFallingTetromino.getSize() > Grid.LIZE_SIZE)
                this.currentRotationOriginX= (Grid.LIZE_SIZE -1) - this.currentFallingTetromino.getSize();
            if(this.currentRotationOriginY + this.currentFallingTetromino.getSize() > Grid.LINES)
                this.currentRotationOriginY = (Grid.LINES -1) - this.currentFallingTetromino.getSize();
        }
        
        //Removes the tetromino from the grid
        private void hold(){
            this.grid.removeTetromino();
            Tetromino newTetromino = null;
            if(this.hold == null){
                newTetromino = this.queue.poll();
                this.queue.add(Tetromino.LIST.get(MathHelper.randomInt(Tetromino.LIST.size())));
            }else {
                newTetromino = this.hold;
            }
            this.currentRotation = Tetromino.Rotation.ROT0;
            this.currentRotationOriginX = 3;
            this.currentRotationOriginY = this.grid.findEmptyLine(2, newTetromino);
            this.grid.placeTetromino(newTetromino, currentRotationOriginX, currentRotationOriginY, currentRotation);
            this.hold= this.currentFallingTetromino;
            this.currentFallingTetromino = newTetromino;
        }
}
