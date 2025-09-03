package tetyastan.customSteveChaosReworked.players;

import lombok.Getter;

@Getter
public class InfoDuel {
	
	private int wins = 0, loses = 0;
	public void lose() {loses++;}
	public void win() {wins++;}
	
}
