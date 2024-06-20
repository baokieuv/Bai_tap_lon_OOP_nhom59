package levels;

import java.awt.*;

public class Level {

	private final int[][] lvlData;
	private final int[][] greenLvlData;
	private final Point begin;
    private final Point end;

	public Level(int[][] lvlData, int[][] boolLvlData) {
		this.lvlData = lvlData;
		this.greenLvlData = boolLvlData;
		begin = beginMap();
		end = endMap();
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}
	public void setSpriteIndex(int x, int y, int value){
		lvlData[y][x] = value;
	}
	public void setGreenSpriteIndex(int x, int y, int value){
		greenLvlData[y][x] = value;
	}

	public int[][] getLevelData() {
		return lvlData;
	}
	public int[][] getGreenLvlData(){return greenLvlData;}

	public Point beginMap(){
		for(int j = 0; j < greenLvlData[0].length; j++){
			for(int i = greenLvlData.length-1; i >= 0; i--){
				if(greenLvlData[i][j] != 0){
					return new Point(j, i);
				}
			}
		}
		return null;
	}
	public Point endMap(){
		for(int j = greenLvlData[0].length-1; j >= 0; j--){
			for(int i = greenLvlData.length-1; i >= 0; i--){
				if(greenLvlData[i][j] != 0){
					return new Point(j, i);
				}
			}
		}
		return null;
	}
	public Point getBegin(){return begin;}
	public Point getEnd(){return end;}
}
