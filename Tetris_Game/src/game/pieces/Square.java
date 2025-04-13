/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.pieces;

/**
 *
 * @author Leveno
 */
public class Square {
    private String color;
    private boolean fixed;
    
    private boolean stopping;
    private int stopTime;
    
    public Square(String color){
        this.color =color;
        this.fixed = false;
        this.stopping = false;
        this.stopTime= 0;
    }
    
    //Getter method for 'fixed' value
    public boolean isFixed(){
        return fixed;
    }
    
    //First check if this square is 'stopping' , if so increase stop time
    public void setFixed(){
        if(this.stopping)
            this.stopTime++;
        
        if(stopTime == 50){
            this.fixed= true;
            this.stopTime=0;
        }
    }
    
    //Sets this square to 'stopping'
    public void setStopping(){
        this.stopping= true;
    }
    
    public void resetStopTime(){
        this.stopTime=0;
        this.stopping= false;
    }
    
    //Getter method for 'stopping' value
    public boolean isStopping(){
        return stopping;
    }
    
    public void setNotFixed(){
        this.fixed = false;
    }
    
    // Gets the color of this square
    public String getColor(){
        return color;
    }
    
    public void forceFix(){
        this.fixed= true;
    }
}
