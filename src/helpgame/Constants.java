package helpgame;

public class Constants {
	public static class PlayerConstants {
		public static final int APPEARING = 0;
		public static final int FALL = 1;
		public static final int JUMP = 2;
		public static final int HIT = 3;
		public static final int IDLE = 4;
		public static final int RUN = 5;
		public static final int DISAPPEARING = 6;


		public static int GetSpriteAmount(int player_action) {
			if(player_action == APPEARING || player_action == DISAPPEARING || player_action == HIT) return 7;
			else if (player_action == FALL || player_action == JUMP) return 1;
			else if (player_action == IDLE) return 11;
			else if(player_action == RUN) return 12;
			return 0;
		}
	}
	public static class ActionTurtle {
		public final static int HIT = 0;
		public final static int IDLE = 1;
		public final static int SPIKES = 2;

		public static int GetAmountSprite(int ac){
			if(ac == IDLE || ac == SPIKES) return 13;
			else if(ac == HIT) return 5;
			return 0;
		}
	}
	public static class ActionTotem {
		public final static int ATTACK = 0;
		public final static int HIT = 1;
		public final static int IDLE = 2;
		public final static int DESTROYED = 3;

		public static int GetAmountSprite(int ac){
			if(ac == ATTACK) return 6;
			else if(ac == HIT || ac == DESTROYED) return 4;
			else if(ac == IDLE) return 1;
			return 0;
		}
	}
	public static class PlayerSprite{
		public static final int MASK_DUDE = 0;
		public static final int NINJA_FROG = 1;
		public static final int PINK_MAN = 2;
		public static final int VIRTUAL_GUY = 3;
	}
}