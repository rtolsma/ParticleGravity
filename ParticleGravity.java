import acm.graphics.*;
import acm.gui.IntField;
import acm.program.*;
import acm.util.RandomGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;
/*
 * Should I clear or create a new ArrayLIst?
 * Currently has memory leak
 */
public class ParticleGravity extends GraphicsProgram {

	
    static int width=500, height=500;
	static double gravityRadius=100;
	ArrayList<Ball> balls=new ArrayList<Ball>();
	GLabel fps=new GLabel(""), velocity=new GLabel("");
	double frames=0;
	
	// 5*r^2=c,   c/5=r^2, root(c/5)=r  c=1125 if r=15
	int numBalls, radius;
	
	
	double frameRate=60;
	int counter=0;
	double avgV=0;
	GImage b;
	public ParticleGravity(int balls, double gravityRadius, int size) {
		this.gravityRadius=gravityRadius;
		numBalls=balls;
		width=size;
		height=size;
		
	}
	public void init() {
		
		setSize(width, height);
		fps.setColor(Color.black);
		velocity.setColor(Color.black);
	    b=new GImage("background.jpg"); //1080 x 1920
		b.scale((width/1920.0), height/1080.0);
		//println(b.getHeight()+": "+b.getWidth());
		add(b);
		add(fps,width-60, 20);
		add(velocity, width-150, 50);
	}
	
	public void run() {
			for(int i=0; i<numBalls; i++) {
				balls.add(new Ball(radius));
				add(balls.get(i));
			}
			double start=System.currentTimeMillis();

			while(true) {
				//Graphic scaling
				width=getGCanvas().getWidth();
				height=getGCanvas().getHeight();
				b.scale(width/(b.getWidth()), height/b.getHeight());
				fps.setLocation(width/2-30,20);
				velocity.setLocation(width/2-30, 40);
				radius=(int) Math.sqrt((width+height)*4/numBalls) ;
				for(int i=0; i<balls.size();i++) balls.get(i).radius=radius;
				if(System.currentTimeMillis()-start<1000.0/frameRate) continue;

			frames=counter/((System.currentTimeMillis()-start)/1000);
			fps.setLabel("FPS:"+frames);
			velocity.setLabel(" Average Velocity: "+avgV);
			counter=0;
			start=System.currentTimeMillis();
				avgV=0;
				for(int i=0; i<balls.size(); i++) {
					balls.get(i).update(balls);
				avgV+=Math.sqrt(    Math.pow(balls.get(i).dx, 2)  +Math.pow(balls.get(i).dy, 2)     );
				}
				avgV/=balls.size();
				for(int i=0; i<balls.size(); i++)  balls.get(i).hitList=new ArrayList<UUID>();// balls.get(i).hitList.clear();
				
			counter++;
			
			}
			
		}
	public static void main(String[] args) {
				new Input().start();
			}
	
}
  class Ball extends GCompound{
	  double dx, dy;
	  double x,y;
	  double radius;
	 // double gravity=(Math.abs(dx)+Math.abs(dy)) *   .1;
	  double gravity=.05+(Math.abs(dx)+Math.abs(dy)/4);
	  double lineRadius=ParticleGravity.gravityRadius;
	final  UUID id=UUID.randomUUID();
	  ArrayList<UUID> hitList=new ArrayList<UUID>();
	  GOval ball;
	  GLine line;
	  ArrayList<GLine> lines=new ArrayList<GLine>();
	  RandomGenerator rgen=new RandomGenerator();
	  public Ball(double radius) {
		this.radius=radius;
		x=rgen.nextDouble(0,ParticleGravity.width-2*radius);
		y=rgen.nextDouble(0,ParticleGravity.height-2*radius);
		dx=rgen.nextDouble(0,5);
		dy=rgen.nextDouble(0,5);
		ball=new GOval(x,y, radius*2, radius*2);
		ball.setFilled(true);
		ball.setFillColor(Color.red);

		add(ball);
		
	  }
	  
	  public void update(ArrayList<Ball> balls) {
		  ball.setSize(2*radius, 2*radius);
		  for(int i=0; i<lines.size(); i++ ){
			  remove(lines.get(i));
		  }
		  lines=new ArrayList<GLine>();
		 // lines.clear();
		this.x=ball.getX();
		this.y=ball.getY();
		  for(int i=0; i<balls.size(); i++) {
			  Ball temp=balls.get(i);
			  if(!temp.hitList.contains(id)) {
			  hitList.add(temp.id);
			  
			 // System.out.println("This : "+x+" : "+y+" Balls("+i+") "+temp.getX()+": "+temp.getY());
			  if(Math.sqrt(  Math.pow(this.x-temp.x, 2)+Math.pow(this.y-temp.y, 2)           ) <lineRadius) {
				  line=new GLine(this.x+radius, this.y+radius, temp.x+temp.radius, temp.y+temp.radius);
				  lines.add(line);
				  line.setColor(getColor(temp));
				  add(line);
				  
				  //attempt to have objects close in on each other
				  if(temp.x>this.x) {
					  temp.dx-=gravity; 
					  this.dx+=temp.gravity;
				  }
				  else {
					  temp.dx+=gravity;
					  this.dx-=temp.gravity;
				  }
				  if(temp.y>this.y){
					  temp.dy-=gravity;
					  this.dy+=temp.gravity;
				  }
				  else {
					  temp.dy+=gravity;
					  this.dy-=temp.gravity;
				  }
				  
				  
			  		}
			  double dV=.001;
				
				 if(dx>0)
				 this.dx-=dV;
				  else this.dx+=dV;
				 if(dy>0)
				  this.dy-=dV;
				 else this.dx+=dV;
			  }
			  
		  }
		  
		  if(this.x+2*radius> ParticleGravity.width && dx>0) dx=-dx;
		  else if(this.x<0 && (dx<0)) dx=-dx;
		  if(this.y+3*radius>ParticleGravity.height && dy>0) dy=-dy;
		  else if(this.y<0 && dy<0) dy=-dy;
		  
		  ball.move(dx, dy);
		  
		  
		  
		  
	  }
	  
	  public Color getColor(Ball temp) {
		  
		  double distance= Math.sqrt(  Math.pow(this.x-temp.x, 2)+Math.pow(this.y-temp.y, 2));
		// if(!(distance<lineRadius)) return Color.black;
		//  System.out.println(distance/lineRadius);
		  /*
		   * Don't have access to color javadocs, so for right now this isn't really working the way I think it should yet
		   */
		  float alpha=(float) (1-(distance/lineRadius)/3);
		 //decrease opacity with distance slightly
		  
		  if( alpha>=1) {alpha%=1;}
		  
		  
		  Color col;
		  col = new Color(0,0,0, alpha);
		  
		  return col;
	  }
	  
  }