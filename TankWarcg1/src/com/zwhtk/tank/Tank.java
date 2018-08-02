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
	 * ������������˼ά��̹�˰�װ����
	 * ȷ������֮������Ӧ���뵽���ǹ��캯��
	 * д�ù��캯��֮���ٿ���������
	 */
	//̹�˵�λ��
	int x,y;
	//ͨ��������ĸ�����ȷ��̹�˵ķ���
	private boolean bL = false;
	private boolean bU = false;
	private boolean bR = false;
	private boolean bD = false;
	public static final int TANK_SPEED = 5;
	public static final int TANK_WIDTH = 40;
	public static final int TANK_HEIGHT = 40;
	public static final int FIRE_FREQUENCY = 38;
	//ö�����ʹ��̹�˵ķ���
	enum Direction {L,U,R,D,LU,LD,RU,RD,STOP};
	private Direction dir = Direction.STOP; 
	private Direction ptDir = Direction.U;
	TankClient tc;
	//����̹�˺û�����
	private boolean good;
	
	private int myTankLife = 3;//̹���ܹ��ܹ������Ĵ���������0ʱ��Ϸ������
	
	private boolean live = true;//̹���Ƿ���
	private int life = 5;//̹������ֵ
	
	private BloodBar bb = new BloodBar();//��ʼ��Ѫ��
	
	private static Random r = new Random();
	
	private int step = r.nextInt(12) + 3;//�з�̹��ǰ��step���ı䷽��
	private int oldX,oldY;//��¼̹��ǰһ��ʱ�̵�λ�á�
	
	
	
	/**
	 *  ��ʼ��ͼƬ��ImageIO.read(new File("ͼƬ·��"));
	 *  ��ȡͼƬ����tkImage��
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
			tkImageL = ImageIO.read(new File("D:/�����ϰ/Java�γ�����/Java��������/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankL.gif"));
			tkImageU = ImageIO.read(new File("D:/�����ϰ/Java�γ�����/Java��������/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankU.gif"));
			tkImageR = ImageIO.read(new File("D:/�����ϰ/Java�γ�����/Java��������/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankR.gif"));
			tkImageD = ImageIO.read(new File("D:/�����ϰ/Java�γ�����/Java��������/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankD.gif"));
			tkImageLU = ImageIO.read(new File("D:/�����ϰ/Java�γ�����/Java��������/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankLU.gif"));
			tkImageLD = ImageIO.read(new File("D:/�����ϰ/Java�γ�����/Java��������/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankLD.gif"));
			tkImageRU = ImageIO.read(new File("D:/�����ϰ/Java�γ�����/Java��������/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankRU.gif"));
			tkImageRD = ImageIO.read(new File("D:/�����ϰ/Java�γ�����/Java��������/eclipse-jee-mars-x86/workspace/TanKeWar/src/Images/tankRD.gif"));
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
	//Ϊ���ܹ����ʣ���live���getters��setters
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	/**
	 * ���캯��
	 * @param x̹��λ�ú�����
	 * @param y̹��λ�������
	 * @param good̹�˺û�
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
			return;//���̹��û�л��žͲ���̹��
		}
		if(good) bb.draw(g);//��Ѫ��
		switch(ptDir){//������Ͳ�ķ���ѡ��ͬ�����̹��ͼƬ
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
	//�������ķ����ƶ�
	void move(){
		this.oldX = x;//move֮ǰ�ȱ���̹�˵�λ��
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
		//̹�˲��ܳ���
		if (x < 0) x = 0;
		if (y < 30) y = 30;
		if (x > TankClient.GAME_WIDTH - TANK_WIDTH) x = TankClient.GAME_WIDTH - TANK_WIDTH;
		if (y > TankClient.GAME_HEIGHT - TANK_HEIGHT) y =TankClient.GAME_HEIGHT - TANK_HEIGHT;
		//����ǵз�̹����ÿ���ƶ���ı�һ�η���
		if(!good){
			Direction[] dirs  = Direction.values();//��enum�еķ�����һ�����顪��values������
			if(step == 0){//����step��֮���ٸı䷽��
				int rn = r.nextInt(dirs.length);//����0~dirs.length֮����������
				dir = dirs[rn];
				step = r.nextInt(12) + 3;
			}
			step--;
			if(r.nextInt(40) > FIRE_FREQUENCY) this.fire();//����̹�˷����ӵ���Ƶ��
		}
	}
	//����з�̹��ײǽ��ײ������̹�ˣ��ص�ԭ����λ��
	public void stay(){
		x = oldX;
		y = oldY;
	}
	//�����µļ����Ϊtrue
	/**
	 * �ڼ���̹�˱��ɵ�֮��F2���ɼ���̹��
	 * ���ϡ��¡����Ҹı�̹�˷���
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
	//�ɿ�����
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
		if(!live) return null;//������˾Ͳ������ӵ�
		int x = this.x +Tank.TANK_WIDTH/2-Missile.MISSILE_WIDTH/2;
		int y = this.y +Tank.TANK_HEIGHT/2-Missile.MISSILE_HEIGHT/2;
		Missile ms = new Missile(x,y,ptDir,good,this.tc);//�����good���ݵ���̹�˵�goodֵ��Ҳ�ͱ�֤���ӵ��ĺû����ӵ�����ͬ
		tc.missiles.add(ms);
		return ms;
	}
	//�����ڵ�
	public Missile fire(Direction dir){
		if(!live) return null;//������˾Ͳ������ӵ�
		int x = this.x +Tank.TANK_WIDTH/2-Missile.MISSILE_WIDTH/2;
		int y = this.y +Tank.TANK_HEIGHT/2-Missile.MISSILE_HEIGHT/2;
		Missile ms = new Missile(x,y,dir,good,this.tc);//�����good���ݵ���̹�˵�goodֵ��Ҳ�ͱ�֤���ӵ��ĺû����ӵ�����ͬ
		tc.missiles.add(ms);
		return ms;
	}
	//�ж����ĸ���������������
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
	//̹����ߵ�С����
	public Rectangle getRect() {
		return new Rectangle(x,y,TANK_WIDTH,TANK_HEIGHT);
	}
	public boolean collidesWithWall(Wall w){
		//̹������ǽʱ�ص�ǰһ�̵�λ��
		if(this.live && this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	//̹��֮�䲻���໥��Խ
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
	//�����ӵ�
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
	//Ѫ��
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





