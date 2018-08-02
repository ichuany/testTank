package com.zwhtk.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame{
	
	public static final int GAME_WIDTH = 800;//确定游戏界面的大小
	public static final int GAME_HEIGHT = 600;
	public static final int ENEMY_NUM = 10;//在lunchFrame中画出敌方坦克
	
	Image offScreenImage = null;//储存每次画出的图片，让每次刷新时载入整张图片以防止抖动
	/**
	 * 初始化实例
	 * 坦克、子弹、爆炸、墙等
	 */
	Tank myTank = new Tank(400,500,true,Tank.Direction.STOP, this);//己方坦克
	Missile ms = null;//子弹
	
	
	List<Missile> missiles = new ArrayList<Missile>();//有多个子弹存到List中
	List<Explode> explodes = new ArrayList<Explode>();//可能多个同时爆炸，所以将爆炸也存到List中
	List<Tank> tanks = new ArrayList<Tank>();
	Wall w1 = new Wall(200,200,300,20,true,this);//画出一面墙
	Wall w2 = new Wall(100,300,20,200,false,this);
	Blood b = new Blood();
	/**
	 * 重写paint画图函数
	 */
	public void paint(Graphics g) {
		
		g.drawString("missiles count:"+missiles.size(), 10, 50);
		g.drawString("explodes count:"+explodes.size(), 10, 70);
		g.drawString("tanks    count:"+tanks.size(), 10, 90);
		g.drawString("tanks     life:"+myTank.getLife(), 10, 110);
		g.drawString("tanks     lifeTime:"+myTank.getMyTankLife(), 10, 130);
		
		myTank.draw(g);//画出我方坦克
		myTank.collidesWithTanks(tanks);
		myTank.eat(b);
		if(b.live) b.draw(g);

		//画出子弹
		for(int i = 0; i < missiles.size(); i++){
			Missile ms = missiles.get(i);
			ms.hitTanks(tanks);
			ms.hitTank(myTank);
			ms.collidesWithWall(w1);
			ms.collidesWithWall(w2);
			ms.draw(g);
		}
		//画出爆炸
		for(int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		//画出敌方坦克，如果坦克全部都被消灭，重新生成ENEMY_NUM量坦克
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
		w1.draw(g);//画第一面墙
		w2.draw(g);//画第二面墙
		//游戏结束
		if( myTank.getMyTankLife() == 0 && !myTank.isLive()){
			Color c = g.getColor(); // 设置参数
			g.setColor(Color.RED);
			Font f = g.getFont();
			g.setFont(new Font(" ", Font.PLAIN, 40));
			g.drawString("  你输了！", 300, 200);
			g.drawString("游戏结束！ ", 300, 300);
			g.setFont(f);
			g.setColor(c);
		}
	}
	//使用双缓冲消除闪烁现象，将所有东西画在一张虚拟图片上，然后一次性的显示出来，
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
	
	//设置窗口
	public void lunchFrame(){
		for(int i = 0; i < ENEMY_NUM; i++){
			tanks.add(new Tank(50 + 40*(i+1), 50, false,Tank.Direction.D, this));
		}
		this.setLocation(100, 50);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.setBackground(Color.GREEN);
		//关闭窗口，重写父类的方法 右键-source-override
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}	
		});
		//改变坦克运动方向
		this.addKeyListener(new KeyMonitor());
		//不让窗口改变大小
		this.setResizable(false);
		//使窗口可见
		setVisible(true);
		//启动坦克运动线程
		new Thread(new PaintThread()).start();
	}
	public static void main(String[] args) {
		TankClient ck = new TankClient();
		ck.lunchFrame();
	}
	
	//内部类加线程使坦克能够自己动
	private class PaintThread implements Runnable{
		public void run() {
			while(true){
				//调用父类的repaint，从而会调用paint重写
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//改变坦克的运动方向，同样使用内部类比较方便
	private class KeyMonitor extends KeyAdapter{
		//按下按键
		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		//松开按键
		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}
	
}
