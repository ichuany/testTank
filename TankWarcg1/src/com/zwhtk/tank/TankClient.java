package com.zwhtk.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame{
	
	public static final int GAME_WIDTH = 800;//ȷ����Ϸ����Ĵ�С
	public static final int GAME_HEIGHT = 600;
	public static final int ENEMY_NUM = 10;//��lunchFrame�л����з�̹��
	
	Image offScreenImage = null;//����ÿ�λ�����ͼƬ����ÿ��ˢ��ʱ��������ͼƬ�Է�ֹ����
	/**
	 * ��ʼ��ʵ��
	 * ̹�ˡ��ӵ�����ը��ǽ��
	 */
	Tank myTank = new Tank(400,500,true,Tank.Direction.STOP, this);//����̹��
	Missile ms = null;//�ӵ�
	
	
	List<Missile> missiles = new ArrayList<Missile>();//�ж���ӵ��浽List��
	List<Explode> explodes = new ArrayList<Explode>();//���ܶ��ͬʱ��ը�����Խ���ըҲ�浽List��
	List<Tank> tanks = new ArrayList<Tank>();
	Wall w1 = new Wall(200,200,300,20,true,this);//����һ��ǽ
	Wall w2 = new Wall(100,300,20,200,false,this);
	Blood b = new Blood();
	/**
	 * ��дpaint��ͼ����
	 */
	public void paint(Graphics g) {
		
		g.drawString("missiles count:"+missiles.size(), 10, 50);
		g.drawString("explodes count:"+explodes.size(), 10, 70);
		g.drawString("tanks    count:"+tanks.size(), 10, 90);
		g.drawString("tanks     life:"+myTank.getLife(), 10, 110);
		g.drawString("tanks     lifeTime:"+myTank.getMyTankLife(), 10, 130);
		
		myTank.draw(g);//�����ҷ�̹��
		myTank.collidesWithTanks(tanks);
		myTank.eat(b);
		if(b.live) b.draw(g);

		//�����ӵ�
		for(int i = 0; i < missiles.size(); i++){
			Missile ms = missiles.get(i);
			ms.hitTanks(tanks);
			ms.hitTank(myTank);
			ms.collidesWithWall(w1);
			ms.collidesWithWall(w2);
			ms.draw(g);
		}
		//������ը
		for(int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		//�����з�̹�ˣ����̹��ȫ������������������ENEMY_NUM��̹��
		if(tanks.size() <= 0){
			for(int i = 0; i < ENEMY_NUM; i++){
				tanks.add(new Tank(50 + 40*(i+1), 50, false,Tank.Direction.D, this));
			}
		}
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			t.draw(g);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
		}
		w1.draw(g);//����һ��ǽ
		w2.draw(g);//���ڶ���ǽ
		//��Ϸ����
		if( myTank.getMyTankLife() == 0 && !myTank.isLive()){
			Color c = g.getColor(); // ���ò���
			g.setColor(Color.RED);
			Font f = g.getFont();
			g.setFont(new Font(" ", Font.PLAIN, 40));
			g.drawString("  �����ˣ�", 300, 200);
			g.drawString("��Ϸ������ ", 300, 300);
			g.setFont(f);
			g.setColor(c);
		}
	}
	//ʹ��˫����������˸���󣬽����ж�������һ������ͼƬ�ϣ�Ȼ��һ���Ե���ʾ������
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	//���ô���
	public void lunchFrame(){
		for(int i = 0; i < ENEMY_NUM; i++){
			tanks.add(new Tank(50 + 40*(i+1), 50, false,Tank.Direction.D, this));
		}
		this.setLocation(100, 50);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.setBackground(Color.GREEN);
		//�رմ��ڣ���д����ķ��� �Ҽ�-source-override
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}	
		});
		//�ı�̹���˶�����
		this.addKeyListener(new KeyMonitor());
		//���ô��ڸı��С
		this.setResizable(false);
		//ʹ���ڿɼ�
		setVisible(true);
		//����̹���˶��߳�
		new Thread(new PaintThread()).start();
	}
	public static void main(String[] args) {
		TankClient ck = new TankClient();
		ck.lunchFrame();
	}
	
	//�ڲ�����߳�ʹ̹���ܹ��Լ���
	private class PaintThread implements Runnable{
		public void run() {
			while(true){
				//���ø����repaint���Ӷ������paint��д
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//�ı�̹�˵��˶�����ͬ��ʹ���ڲ���ȽϷ���
	private class KeyMonitor extends KeyAdapter{
		//���°���
		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		//�ɿ�����
		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}
	
}
