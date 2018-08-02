package com.zwhtk.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
//子弹类
public class Missile {
	int x,y;
	public static final int MISSILE_SPEED = 10;
	public static final int MISSILE_WIDTH = 10;
	public static final int MISSILE_HEIGHT = 10;
	Tank.Direction dir;
	private boolean live = true;
	private boolean good;//子弹也要区分，自己的子弹不能打自己
	
	private TankClient tc;
	
	public boolean isLive() {
		return live;
	}
	//构造函数
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public Missile(int x, int y, Tank.Direction dir, boolean good, TankClient tc){
		this(x,y,dir);
		this.good = good;
		this.tc = tc;
	}
	public void draw(Graphics g){
		if(!live) {
			tc.missiles.remove(this);
			return;
		}//在画子弹之前判断子弹是否打中了坦克，如果打中了（没活着）就直接return，不用画子弹
		Color c = g.getColor();
		if(!good) g.setColor(Color.BLACK);//敌方子弹黑色
		else g.setColor(Color.WHITE);//我方子弹白色
		g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);
		g.setColor(c);
		
		move();
	}

	private void move() {
		switch(dir){
		case L:
			x -= MISSILE_SPEED;
			break;
		case U:
			y -= MISSILE_SPEED;
			break;
		case R:
			x += MISSILE_SPEED;
			break;
		case D:
			y += MISSILE_SPEED;
			break;
		case LU:
			x -= MISSILE_SPEED;
			y -= MISSILE_SPEED;
			break;
		case LD:
			x -= MISSILE_SPEED;
			y += MISSILE_SPEED;
			break;
		case RU:
			x += MISSILE_SPEED;
			y -= MISSILE_SPEED;
			break;
		case RD:
			x += MISSILE_SPEED;
			y += MISSILE_SPEED;
			break;
		default:
			break;
		}
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT){
			live = false;
		}
	}
	//子弹外边的小方块
	public Rectangle getRect() {
		return new Rectangle(x,y,MISSILE_WIDTH,MISSILE_HEIGHT);
	}
	//子弹打中一个坦克
	public boolean hitTank(Tank t){
		//子弹击中敌方坦克
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {//加t.isLive是由于坦克虽然没有画出来，但是他仍然在那个位置，子弹到了该位置还是会消失
			if(t.isGood()){//己方坦克血量
				t.setLife(t.getLife() - 1);
				if(t.getLife() <= 0) t.setLive(false);
			}
			else t.setLive(false);
			this.live = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		//子弹没有击中敌方坦克
		return false;
	}
	//子弹打中所有的敌方坦克
	public boolean hitTanks(List<Tank> tanks){
		for(int i = 0; i < tanks.size(); i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public boolean collidesWithWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.live = false;
			return true;
		}
		return false;
	}
	
}
