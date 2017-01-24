package org.lwjglb.engine;

import org.joml.Vector2f;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.game.objects.Particle;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class particleSystem{
	private int maxParticles;
	public int particleN;
	private float kernel;
	private float mass;
	private Vector2f world_size;
	private float cell_size;
	private Vector2f grid_size;
	private int tot_cell;
	private Vector2f gravity;
	float stiffness = 1000.0f;
	private float wall_damping;
	private float rest_density;
//	private float gas_constant;
	private float viscosity;
	private float time_step;
/*	private float surf_norm;
	private float surf_coe;

	private float poly6_value;
	private float spiky_value;
	private float visco_value;

	private float grad_poly6;
	private float lplc_poly6;

	private float kernel_2;
	private float self_dens;
	private float self_lplc_color;
	
	private int sys_running;*/
	
	private float PI = 3.141592f;
	private float infinity = 1E-12f;
	private float BOUNDARY  = 0.001f;
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	ArrayList<Particle> cells = new ArrayList<Particle>();
	
	Mesh mesh;
	
	public particleSystem(){
		maxParticles=1000;
		particleN=0;

		kernel=0.04f;
		mass=0.02f;

		world_size=new Vector2f(1.28f,1.28f);
		cell_size=kernel;
		grid_size = new Vector2f();
		grid_size.x=(float)Math.ceil(world_size.x/cell_size);
		grid_size.y=(float)Math.ceil(world_size.y/cell_size);
		tot_cell=(int)grid_size.x*(int)grid_size.y;
		cells = (ArrayList<Particle>) Stream.generate(Particle::new).limit(tot_cell).collect(Collectors.toList());
		
		
		gravity = new Vector2f();
		gravity.x=0.0f; 
		gravity.y=-1.8f;
		wall_damping=0.0f;
		rest_density=1000.0f;
	//	gas_constant=1.0f;
		viscosity=8.0f;
		time_step=0.002f;
//		surf_norm=6.0f;
//		surf_coe=0.2f;

	

	//	grad_poly6=(float) (-945/(32 * PI * Math.pow(kernel, 9)));
	//	lplc_poly6=(float) (-945/(8 * PI * Math.pow(kernel, 9)));

	//	kernel_2=kernel*kernel;
//		self_dens=(float) (mass*poly6_value*Math.pow(kernel, 6));
//		self_lplc_color=lplc_poly6*mass*kernel_2*(0-3/4*kernel_2);
		
//		sys_running=0;
		
	//	this.mesh = mesh;
	}
	
	public void animation(){
	/*	if(sys_running == 0)
		{
			return;
		}*/

		build_table();
		comp_dens_pres();
		comp_force_adv();
		advection();
	//	System.out.println("animating");
	}
	
	public void init_system() throws Exception{
		Vector2f pos = new Vector2f(0.0f, 0.0f);
		Vector2f vel = new Vector2f(0.0f, 0.0f);

		for(pos.x=world_size.x*0.3f; pos.x<world_size.x*0.7f; pos.x+=(kernel*0.6f))
		{
			for(pos.y=world_size.y*0.3f; pos.y<world_size.y*0.9f; pos.y+=(kernel*0.6f))
			{
				add_particle(pos, vel);
			}
		}
	}
	
	public void add_particle(Vector2f pos, Vector2f vel) throws Exception{
		
		int id = particleN;
		Vector2f acceleration = new Vector2f(0.0f, 0.0f);
		Vector2f ev = new Vector2f(vel.x, vel.y);
		float density = rest_density;
		float pressure = 0.0f;
		
		
		Particle p = new Particle(pos, vel, acceleration, ev, density, pressure, null);
		particles.add(p);
		particleN++;
	}
	
	public void build_table(){
		for(int i=0; i<tot_cell; i++){
			cells.set(i,null);
		}
		
		Particle p;
		int hash;

		for(int i=0; i<particleN; i++)
		{
			p=particles.get(i);
			hash=calc_cell_hash(calc_cell_pos(p.getPosition2f()));
		//	System.out.println(hash);
			if(hash <= -1){
				p.nextP=null;
			}
			else if(cells.get(hash) == null)
			{
				p.nextP=null;
				cells.set(hash, p);
			}
			else
			{
				p.nextP=cells.get(hash);
				cells.set(hash, p);
			}
		}
	}
	
	public void comp_dens_pres(){
		Particle np;
		Vector2f cellPos = new Vector2f(0.0f, 0.0f);
		Vector2f nearPos = new Vector2f(0.0f, 0.0f);
		int hash;
		for(Particle p: particles)
		{
			p.setDensity(0.0f);
			p.setPressure(0.0f);
			cellPos = calc_cell_pos(p.getPosition2f());
			for(int i=-1; i<=1; i++)
			{
				for(int j=-1; j<=1; j++)
				{
					nearPos.x = cellPos.x + i;
					nearPos.y = cellPos.y + j;
					hash = calc_cell_hash(nearPos);
					if(hash == 0xffffffff) continue;
					np = cells.get(hash);
					while(np != null)
					{
						Vector2f distVec = new Vector2f(np.getPosition2f().x-p.getPosition2f().x, np.getPosition2f().y-p.getPosition2f().y);
						float dist2 = (distVec.x*distVec.x) + (distVec.y*distVec.y);
						
						if(dist2<infinity || dist2>=kernel*kernel)
						{
							np = np.nextP;
							continue;
						}
	
						p.setDensity( p.getDensity() + mass * poly6(dist2) );
						np = np.nextP;
					}
				}
			}
			p.setDensity( p.getDensity() + mass*poly6(0.0f) );
			p.setPressure( (float) ((Math.pow(p.getDensity() / rest_density, 7) - 1) * stiffness) );
		}
	}
	
	public void comp_force_adv(){
		Particle p, np;
		Vector2f cell_pos, near_pos;
		cell_pos = near_pos  = new Vector2f(0.0f, 0.0f);
		int hash;
		for(int i=0; i<particleN; i++)
		{
			p=particles.get(i); 
			cell_pos=calc_cell_pos(p.getPosition2f());
			p.setAcceleration(new Vector2f(0.0f,0.0f));
			
			for(int x=-1; x<=1; x++)
			{
				for(int y=-1; y<=1; y++)
				{
					near_pos.x=cell_pos.x+x;
					near_pos.y=cell_pos.y+y;
					hash=calc_cell_hash(near_pos);

					if(hash == 0xffffffff)
					{
						continue;
					}

					np=cells.get(hash);
					while(np != null)
					{
						Vector2f distVec = new Vector2f(np.getPosition2f().x-p.getPosition2f().x, np.getPosition2f().y-p.getPosition2f().y);
						float dist2 = (distVec.x*distVec.x) + (distVec.y*distVec.y);

						if(dist2 < kernel*kernel && dist2 > infinity)
						{
							float dist = (float) Math.sqrt(dist2);
							float V = mass / p.getDensity();

							float tempForce = V * (p.getPressure()+np.getPressure()) * spiky(dist);
							p.setAcceleration( new Vector2f(p.getAcceleration().x - distVec.mul(tempForce/dist).x,
									p.getAcceleration().y - distVec.mul(tempForce/dist).y));

							Vector2f relVel = np.getEv().sub(p.getEv());
							tempForce = V * viscosity * visco(dist);
							p.setAcceleration( p.getAcceleration().add(relVel.mul(tempForce)) ); 
						}
						np=np.nextP;
					}
				}
			}
			Vector2f temp = new Vector2f(p.getAcceleration().x/p.getDensity(), p.getAcceleration().y/p.getDensity()); 
			p.setAcceleration( temp.add(gravity) );
		}
	}
	
	public void advection(){
		Particle p;
		for(int i=0; i<particleN; i++)
		{
			p = particles.get(i);
			p.setVelocity( p.getVelocity().add(p.getAcceleration().mul(time_step)) );
			p.setPosition( p.getPosition2f().add(p.getVelocity().mul(time_step)) );

			if(p.getPosition2f().x < 0.0f)
			{
				p.setVelocityX(p.getVelocity().x * wall_damping );
				p.setPositionX(0.0f);
			}
			if(p.getPosition2f().x >= world_size.x)
			{
				p.setVelocityX( p.getVelocity().x * wall_damping );
				p.setPositionX(world_size.x - 0.0001f);
			}
			if(p.getPosition2f().y < 0.0f)
			{
				p.setVelocityY( p.getVelocity().y * wall_damping);
				p.setPositionY(0.0f);
			}
			if(p.getPosition2f().y >= world_size.y)
			{
				p.setVelocityY( p.getVelocity().y * wall_damping);
				p.setPositionY(world_size.y - 0.0001f);
			}
			Vector2f temp = new Vector2f(p.getEv().add( p.getVelocity() ) );
			p.setEv( new Vector2f(temp.x/2, temp.y/2) );
			
		}
	}
	
	public Vector2f calc_cell_pos(Vector2f p){
		Vector2f cell_pos = new Vector2f((float)Math.floor(p.x/cell_size), (float)Math.floor(p.y/cell_size) );
		return cell_pos;
	}
	
	public int calc_cell_hash(Vector2f cell_pos){
		if(cell_pos.x<0 || cell_pos.x>=(int)grid_size.x || cell_pos.y<0 || cell_pos.y>=(int)grid_size.y)
		{
			return (int)0xffffffff;
		} 
		return (int) (((cell_pos.y))*grid_size.x+(cell_pos.x));
	}
	
	public void compTimeStep(){
		float maxAcc = 0.0f;
		float curAcc;
		for(Particle p : particles){
			curAcc = p.getAcceleration().length();
			if(curAcc > maxAcc) maxAcc=curAcc;
		}
		if(maxAcc > 0.0f){
			time_step = kernel/maxAcc*0.4f;
		}
		else
		{
			time_step = 0.002f;
		}
	}
	//kernel function
		float poly6(float r2){ return (float) (315.0f/(64.0f * PI * Math.pow(kernel, 9)) * Math.pow(kernel*kernel-r2, 3)); }
		float spiky(float r){ return (float) (-45.0f/(PI * Math.pow(kernel, 6)) * (kernel-r) * (kernel-r)); }
		float visco(float r){ return (float) (45.0f/(PI * Math.pow(kernel, 6)) * (kernel-r)); }
}