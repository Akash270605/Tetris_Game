/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.pieces;

public class Grid {
    public static final int LINES= 22;
    public static final int LIZE_SIZE= 10;
    
    private Square[][] singlePieces;
    
    public Grid(){
        this.singlePieces= new Square[LINES][LIZE_SIZE];
        
    }
    
    public void placeTetromino(Tetromino tetromino, int locationX, int locationY, Tetromino.Rotation rotation) {
		for(int i=0;i<tetromino.getSize();i++) {
			for(int j=0;j<tetromino.getSize();j++) {
				if(tetromino.getSquareAt(rotation, i, j) != null) {
					this.singlePieces[locationY+i][locationX+j] = tetromino.getSquareAt(rotation, i, j);
				}
			}
		}
	}
    
    public Square[] getLine(int i){
        return singlePieces[i];
    }
    
    //Checks if all squares can fall in the tile below
    public boolean allSquaresCanFall(){
        for(int i= Grid.LINES-1; i>=0; i--){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] !=null){
                    if((!this.singlePieces[i][j].isFixed() && i== Grid.LINES-1) || (!this.singlePieces[i][j].isFixed() && this.singlePieces[i+1][j] !=null && this.singlePieces[i+1][j].isFixed())){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    //Check if all the squares can move to the tile on their left
    public boolean allSquaresCanGoLeft(){
        for(int i=0; i<Grid.LINES; i++){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] !=null){
                    if((!this.singlePieces[i][j].isFixed() && j==0 ) || (!this.singlePieces[i][j].isFixed() && this.singlePieces[i][j-1] != null && this.singlePieces[i][j-1].isFixed())){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    //Checks if all squares can be moved to the tile on their right
    public boolean allSquaresCanGoRight(){
        for(int i=0; i<Grid.LINES; i++){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] !=null){
                    if((!this.singlePieces[i][j].isFixed() && j== Grid.LIZE_SIZE-1 ) || (!this.singlePieces[i][j].isFixed() && this.singlePieces[i][j+1] != null && this.singlePieces[i][j+1].isFixed())){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    //Moves every non fixed square in the tile below
    public void movePiecesDown(){
        for(int i=Grid.LINES-2; i>=0; i--){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] !=null){
                    if(!this.singlePieces[i][j].isFixed()){
                        this.singlePieces[i+1][j] = this.singlePieces[i][j];
                        this.singlePieces[i][j]= null;
                    }
                }
            }
        }
    }
    
    //Moves  every non fixed square in the tile on its left
    public void movePiecesLeft(){
        for(int i=0; i<Grid.LINES; i++){
            for(int j=1; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] != null){
                    if(!this.singlePieces[i][j].isFixed()){
                        this.singlePieces[i][j-1]= this.singlePieces[i][j];
                        this.singlePieces[i][j-1].resetStopTime();
                        this.singlePieces[i][j]= null;
                    }
                }
            }
        }
    }
    
    //Moves every non fixed square in the tile on its right
    public void movePiecesRight(){
        for(int i=0; i<Grid.LINES; i++){
            for(int j=Grid.LIZE_SIZE-2; j>=0; j--){
                if(this.singlePieces[i][j] != null){
                    if(!this.singlePieces[i][j].isFixed()){
                        this.singlePieces[i][j+1]= this.singlePieces[i][j];
                        this.singlePieces[i][j+1].resetStopTime();
                        this.singlePieces[i][j]= null;
                    }
                }
            }
        }
    }
  
    //When finds non fixed and non stopping square sets it to 'stopping'
    public void setAllSquaresToBeStopped(){
        for(int i=Grid.LINES-1; i>=0; i--){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] !=null){
                    if(!this.singlePieces[i][j].isFixed() && !this.singlePieces[i][j].isStopping()){
                        this.singlePieces[i][j].setStopping();
                    }
                }
            }
        }
    }
    
    //check if all the squares are fixed
    public boolean allSquaresAreFixed(){
        for(int i=0; i<Grid.LINES; i++){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] !=null){
                    if(!this.singlePieces[i][j].isFixed()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    //Sets all squares in the given line to null
    public void removeLine(int line){
        for(int i=0; i<Grid.LIZE_SIZE; i++){
            this.singlePieces[line][i] = null;
        }
        for(int i=line; i>=0; i--){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] != null){
                    this.singlePieces[i][j].setNotFixed();
                }
            }
        }
    }
    
    //Removes all the non fixed squares from the grid
    public void removeTetromino(){
        for(int i=0; i<Grid.LINES; i++){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] != null){
                    if(!this.singlePieces[i][j].isFixed()){
                        this.singlePieces[i][j] = null;
                    }
                }
            }
          }
        }
    
    //Check if the given tetromino can be placed at the given position
    public boolean canPlaceHere(Tetromino tetromino, int locationX, int locationY, Tetromino.Rotation rotation){
        for(int i=0; i<tetromino.getSize(); i++){
            for(int j=0; j<tetromino.getSize(); j++){
                if(tetromino.getSquareAt(rotation, i, j) != null && this.singlePieces[locationY+i][locationX+j] !=null && this.singlePieces[locationY+i][locationX+j].isFixed()){
                    return false;
                }
            }
        }
        return true;
    }
    
    //Keeps moving all sqaures down until it's no longer possible
    public void hardDrop(){
        while(this.allSquaresCanFall()){
            this.movePiecesDown();
        }
        for(int i=0; i<Grid.LINES; i++){
            for(int j=0; j<Grid.LIZE_SIZE; j++){
                if(this.singlePieces[i][j] != null){
                    this.singlePieces[i][j].forceFix();
                }
            }
        }
    }
    
    public int findEmptyLine(int startFrom, Tetromino toPlace){
        if(this.canPlaceHere(toPlace, 3, startFrom, Tetromino.Rotation.ROT0)){
            return startFrom;
        }else{
            return this.findEmptyLine(startFrom-1, toPlace);
        }
    }
}
