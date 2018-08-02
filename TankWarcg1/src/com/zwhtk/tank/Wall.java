package com.zwhtk.tank;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Wall {

	private int x, y, w, h;
	private boolean row;//判断墙是横向的还是非横向（竖向）的
	TankClient tc;
	//导入墙的图片
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] imgs = {
			tk.getImage(Wall.class.getClassLoader().getResource("images/commonWall.gif")),
	};
	
	int step = 0;
	public Wall(int x, int y, int w, int h,boolean row, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.row = row;
		this.tc = tc;
	}
	public void draw(Graphics g){
		g.fillRect(x, y, w, h);
		if(row){
			for(int i = 0; i < w/20; i++){
				g.drawImage(imgs[step],x+i*20,y,null);
			}
		}
		else{
			for(int j = 0; j < h/20; j++){
				g.drawImage(imgs[step],x,y+j*20,null);
			}
		}
		
	}
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
	
}
