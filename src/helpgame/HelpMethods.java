package helpgame;

import main.Game;

import java.awt.*;

public class HelpMethods {

	public static boolean CanMove(float x, float y, float width, float height, int[][] greenLvlData) {
		if (!IsSolid(x, y, greenLvlData))
			if (!IsSolid(x + width, y + height, greenLvlData))
				if (!IsSolid(x + width, y, greenLvlData))
                    return !IsSolid(x, y + height, greenLvlData);
		return false;
	}

	private static boolean IsSolid(float x, float y, int[][] greenLvlData) {
		int maxWidth = greenLvlData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, greenLvlData);
	}

	public static boolean IsTileSolid(int xTile, int yTile, int[][] greenLvlData) {
		int value = greenLvlData[yTile][xTile];
        return value == 0 || value == 5 || value == 8;
    }

	public static int GetEntityXPosNextToWall(Rectangle hitbox, float xSpeed) {
		int currentTile = hitbox.x / Game.TILES_SIZE;
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = Game.TILES_SIZE - hitbox.width;
			return tileXPos + xOffset - 1;
		} else
			// Left
			return currentTile * Game.TILES_SIZE;
	}

	public static int GetEntityYPosUnderRoofOrAboveFloor(Rectangle hitbox, float airSpeed) {
		int currentTile = hitbox.y / Game.TILES_SIZE;
		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = Game.TILES_SIZE - hitbox.height;
			return tileYPos + yOffset - 1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}

	public static boolean IsEntityOnFloor(Rectangle hitbox, int[][] greenLvlData) {
		// Check the pixel below bottomleft and bottomright
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, greenLvlData))
            return IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, greenLvlData);
		return true;
	}

	public static boolean IsFloor(Rectangle hitbox, float xSpeed, int[][] greenLvlData) {
		if(xSpeed > 0){
			return IsSolid(hitbox.x+xSpeed+ hitbox.width, hitbox.y + hitbox.height + 2, greenLvlData);
		}
		return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 2, greenLvlData);
	}

	public static boolean CheckCollision(Rectangle rec1, Rectangle rec2){
		Point A1 = new Point(rec1.x, rec1.y);
		Point B1 = new Point(rec1.x + rec1.width, rec1.y);
		Point C1 = new Point(rec1.x, rec1.y + rec1.height);
		Point D1 = new Point(rec1.x + rec1.width, rec1.y + rec1.height);

		Point A2 = new Point(rec2.x, rec2.y);
		Point B2 = new Point(rec2.x + rec2.width, rec2.y);
		Point C2 = new Point(rec2.x, rec2.y + rec2.height);
		Point D2 = new Point(rec2.x + rec2.width, rec2.y + rec2.height);

		if(rec1.contains(A2) || rec1.contains(B2) || rec1.contains(C2) || rec1.contains(D2)){
			return true;
		}
        return rec2.contains(A1) || rec2.contains(B1) || rec2.contains(C1) || rec2.contains(D1);
    }
}