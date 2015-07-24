import acm.program.*;
import acm.graphics.*;

public class Input extends DialogProgram{

	int balls;
	boolean play=true;
	public boolean playAgain(Thread t) {
		if(!t.isAlive()) {
			String ans= readLine("Would you like to play again? Y/N");
			if(ans.contains("n")){
				return false;
			} 
			else if(ans.contains("y")) return true;
			else return true;
		  }
		else return false;
	}
	public void run() {
		
		setTitle("Particle String Pullings");
		int size=readInt("Enter the size of the applet. Usually greater than or equal to 300");
		
		balls=readInt("Enter the number of balls. Less than 40 for optimization ");
		int gravityRadius=readInt("Enter the length of the connections. Usually between 100-200");
			new ParticleGravity(balls, gravityRadius, size).start();
	//	while(!(play=playAgain(t))) continue;
		
			
		}
	
		 }
	
	

