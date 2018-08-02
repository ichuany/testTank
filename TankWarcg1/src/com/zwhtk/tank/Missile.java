package com.zwhtk.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
//�ӵ���
public class Missile {
	int x,y;
	public static final int MISSILE_SPEED = 10;
	public static final int MISSILE_WIDTH = 10;
	public static final int MISSILE_HEIGHT = 10;
	Tank.Direction dir;
	private boolean live = true;
	private boolean good;//�ӵ�ҲҪ���֣��Լ����ӵ����ܴ��Լ�
	
	private TankClient tc;
	
	public boolean isLive() {
		return live;
	}
	//���캯��
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
		}//�ڻ��ӵ�֮ǰ�ж��ӵ��Ƿ������̹�ˣ���������ˣ�û���ţ���ֱ��return�����û��ӵ�
		Color c = g.getColor();
		if(!good) g.setColor(Color.BLACK);//�з��ӵ���ɫ
		else g.setColor(Color.WHITE);//�ҷ��ӵ���ɫ
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
	//�ӵ���ߵ�С����
	public Rectangle getRect() {
		return new Rectangle(x,y,MISSILE_WIDTH,MISSILE_HEIGHT);
	}
	//�ӵ�����һ��̹��
	public boolean hitTank(Tank t){
		//�ӵ����ез�̹��
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {//��t.isLive������̹����Ȼû�л���������������Ȼ���Ǹ�λ�ã��ӵ����˸�λ�û��ǻ���ʧ
			if(t.isGood()){//����̹��Ѫ��
				t.setLife(t.getLife() - 1);
				if(t.getLife() <= 0) t.setLive(false);
			}
			else t.setLive(false);
			this.live = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		//�ӵ�û�л��ез�̹��
		return false;
	}
	//�ӵ��������еĵз�̹��
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
