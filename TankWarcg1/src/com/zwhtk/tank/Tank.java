package com.zwhtk.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Tank {
	/**
	 * 掌握面向对象的思维将坦克包装成类
	 * 确定属性之后首先应该想到的是构造函数
	 * 写好构造函数之后再考虑其他的
	 */
	//坦克的位置
	int x,y;
	//通过下面的四个变量确定坦克的方向
	private boolean bL = false;
	private boolean bU = false;
	private boolean bR = false;
	private boolean bD = false;
	public static final int TANK_SPEED = 5;
	public static final int TANK_WIDTH = 40;
	public static final int TANK_HEIGHT = 40;
	public static final int FIRE_FREQUENCY = 38;
	//枚举类型存放坦克的方向
	enum Direction {L,U,R,D,LU,LD,RU,RD,STOP};
	private Direction dir = Direction.STOP; 
	private Direction ptDir = Direction.U;
	TankClient tc;
	//区分坦克好坏的量
	private boolean good;
	
	private int myTankLife = 3;//坦克总共能够死亡的次数，等于0时游戏结束。
	
	private boolean live = true;//坦克是否存活
	private int life = 5;//坦克生命值
	
	private BloodBar bb = new BloodBar();//初始化血条
	
	private static Random r = new Random();
	
	private int step = r.nextInt(12) + 3;//敌方坦克前进step步改变方向
	private int oldX,oldY;//记录坦克前一个时刻的位置。
	
	
	
	/**
	 *  初始化图片，ImageIO.read(new File("图片路径"));
	 *  读取图片存人tkImage中
	 */
	static BufferedImage tkImageL = null;
	static BufferedImage tkImageU = null;
	static BufferedImage tkImageR = null;
	static BufferedImage tkImageD = null;
	static BufferedImage tkImageLU = null;
	static BufferedImage tkImageLD = null;
	static BufferedImage tkImageRU = null;
	static BufferedImage tkImageRD = null;
	static {
		try {
			tkImageL = ImageIO.read(new File("D:/编程练习/Java课程资料/Java开发工具/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankL.gif"));
			tkImageU = ImageIO.read(new File("D:/编程练习/Java课程资料/Java开发工具/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankU.gif"));
			tkImageR = ImageIO.read(new File("D:/编程练习/Java课程资料/Java开发工具/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankR.gif"));
			tkImageD = ImageIO.read(new File("D:/编程练习/Java课程资料/Java开发工具/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankD.gif"));
			tkImageLU = ImageIO.read(new File("D:/编程练习/Java课程资料/Java开发工具/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankLU.gif"));
			tkImageLD = ImageIO.read(new File("D:/编程练习/Java课程资料/Java开发工具/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankLD.gif"));
			tkImageRU = ImageIO.read(new File("D:/编程练习/Java课程资料/Java开发工具/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankRU.gif"));
			tkImageRD = ImageIO.read(new File("D:/编程练习/Java课程资料/Java开发工具/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankRD.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getMyTankLife() {
		return myTankLife;
	}
	public void setMyTankLife(int myTankLife) {
		this.myTankLife = myTankLife;
	} 
	
	public boolean isGood() {
		return good;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	//为了能够访问，给live添加getters和setters
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	/**
	 * 构造函数
	 * @param x坦克位置横坐标
	 * @param y坦克位置纵左边
	 * @param good坦克好坏
	 */
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.good  = good;
	}
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc){
		this(x,y,good);
		this.dir = dir;
		this.tc = tc;
		this.oldX = x;
		this.oldY = y;
	}
	
	public void draw(Graphics g){
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;//如果坦克没有活着就不画坦克
		}
		if(good) bb.draw(g);//画血条
		switch(ptDir){//根据炮筒的方向选择不同方向的坦克图片
		case L:
			g.drawImage(tkImageL, x, y, tc);
			break;
		case U:
			g.drawImage(tkImageU, x, y, tc);
			break;
		case R:
			g.drawImage(tkImageR, x, y, tc);
			break;
		case D:
			g.drawImage(tkImageD, x, y, tc);
			break;
		case LU:
			g.drawImage(tkImageLU, x, y, tc);
			break;
		case LD:
			g.drawImage(tkImageLD, x, y, tc);
			break;
		case RU:
			g.drawImage(tkImageRU, x, y, tc);
			break;
		case RD:
			g.drawImage(tkImageRD, x, y, tc);
			break;
		case STOP:
			break;
		}
		move();
	}
	//往按键的方向移动
	void move(){
		this.oldX = x;//move之前先保存坦克的位置
		this.oldY = y;
		switch(dir){
		case L:
			x -= TANK_SPEED;
			break;
		case U:
			y -= TANK_SPEED;
			break;
		case R:
			x += TANK_SPEED;
			break;
		case D:
			y += TANK_SPEED;
			break;
		case LU:
			x -= TANK_SPEED;
			y -= TANK_SPEED;
			break;
		case LD:
			x -= TANK_SPEED;
			y += TANK_SPEED;
			break;
		case RU:
			x += TANK_SPEED;
			y -= TANK_SPEED;
			break;
		case RD:
			x += TANK_SPEED;
			y += TANK_SPEED;
			break;
		case STOP:
			break;
		}
		if (this.dir  != Direction.STOP){
			this.ptDir = this.dir;
		}
		//坦克不能出界
		if (x < 0) x = 0;
		if (y < 30) y = 30;
		if (x > TankClient.GAME_WIDTH - TANK_WIDTH) x = TankClient.GAME_WIDTH - TANK_WIDTH;
		if (y > TankClient.GAME_HEIGHT - TANK_HEIGHT) y =TankClient.GAME_HEIGHT - TANK_HEIGHT;
		//如果是敌方坦克则每次移动后改变一次方向
		if(!good){
			Direction[] dirs  = Direction.values();//将enum中的方向变成一个数组——values方法。
			if(step == 0){//经过step步之后再改变方向
				int rn = r.nextInt(dirs.length);//产生0~dirs.length之间的随机整数
				dir = dirs[rn];
				step = r.nextInt(12) + 3;
			}
			step--;
			if(r.nextInt(40) > FIRE_FREQUENCY) this.fire();//调整坦克发射子弹的频率
		}
	}
	//如果敌方坦克撞墙或撞到其他坦克，回到原来的位置
	public void stay(){
		x = oldX;
		y = oldY;
	}
	//将按下的键标记为true
	/**
	 * 在己方坦克被干掉之后按F2生成己方坦克
	 * 按上、下、左、右改变坦克方向
	 * @param e
	 */
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_F2:
			if(myTankLife != 0){
				myTankLife--;
				if(!this.live){
					this.live = true;
					this.life = 5;	
					}
				}
			else {
				live = false;
				myTankLife = 0;
			}
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		default:
			break;
		}
		locateDirection();
	}
	//松开按键
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_J:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_K:
			superFire();
			break;
		default:
			break;
		}
		locateDirection();
	}
	
	public Missile fire() {
		if(!live) return null;//如果死了就不发射子弹
		int x = this.x +Tank.TANK_WIDTH/2-Missile.MISSILE_WIDTH/2;
		int y = this.y +Tank.TANK_HEIGHT/2-Missile.MISSILE_HEIGHT/2;
		Missile ms = new Missile(x,y,ptDir,good,this.tc);//这里的good传递的是坦克的good值，也就保证了子弹的好坏与子弹的相同
		tc.missiles.add(ms);
		return ms;
	}
	//超级炮弹
	public Missile fire(Direction dir){
		if(!live) return null;//如果死了就不发射子弹
		int x = this.x +Tank.TANK_WIDTH/2-Missile.MISSILE_WIDTH/2;
		int y = this.y +Tank.TANK_HEIGHT/2-Missile.MISSILE_HEIGHT/2;
		Missile ms = new Missile(x,y,dir,good,this.tc);//这里的good传递的是坦克的good值，也就保证了子弹的好坏与子弹的相同
		tc.missiles.add(ms);
		return ms;
	}
	//判断是哪个或哪两个键按下
	public void locateDirection(){
		if (bL && !bU && !bR && !bD) dir = Direction.L;
		else if (!bL && bU && !bR && !bD) dir = Direction.U;
		else if (!bL && !bU && bR && !bD) dir = Direction.R;
		else if (!bL && !bU && !bR && bD) dir = Direction.D;
		else if (bL && bU && !bR && !bD) dir = Direction.LU;
		else if (bL && !bU && !bR && bD) dir = Direction.LD;
		else if (!bL && bU && bR && !bD) dir = Direction.RU;
		else if (!bL && !bU && bR && bD) dir = Direction.RD;	
		else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	//坦克外边的小方块
	public Rectangle getRect() {
		return new Rectangle(x,y,TANK_WIDTH,TANK_HEIGHT);
	}
	public boolean collidesWithWall(Wall w){
		//坦克碰到墙时回到前一刻的位置
		if(this.live && this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	//坦克之间不能相互穿越
	public boolean collidesWithTanks(List<Tank> tanks){
		for (int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			if(this != t){
				if(this.live && t.live && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	//超级子弹
	private void superFire(){
		Direction[] dirs = Direction.values();
		for(int i = 0; i < 8; i++){
			fire(dirs[i]);
		}
	}
	public boolean eat(Blood b){
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			this.life = 5;
			b.setLive(false); 
			return true;
		}
		return false;
	}
	//血条
	private class BloodBar{
		private void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x-3, y-10, TANK_WIDTH,10);
			int bw = TANK_WIDTH*life/5;
			g.fillRect(x-3, y-10, bw, 10);
			g.setColor(c);
		}
	}
	
}





