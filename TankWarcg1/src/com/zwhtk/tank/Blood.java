package com.zwhtk.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Blood {
	
	private int x, y, w, h;
	TankClient tc;
	private int step;//标记运动到了pos中的哪个点
	//指明血块运动的轨迹，有pos中的各个点构成
	private int[][] pos = {
			{300,300},{302,300},{304,302},{306,303},{308,304},{310,305},{312,306},
			{314,307},{316,308},{318,309},{320,310},{322,311},{324,312},{326,313},
			{328,314},{330,315},{332,316},{334,317},{336,318},{338,319},{340,320},
			{342,321},{344,322},{346,323},{348,324},{350,325},{350,325},{348,324},
			{346,323},{344,322},{342,321},{340,320},{338,319},{336,318},{334,317},
			{332,316},{330,315},{328,314},{326,313},{324,312},{322,311},{320,310},
			{318,309},{316,308},{314,307},{312,306},{310,305},{308,304},{306,303},
			{304,302},{302,301},{300,300}	
	};
	public boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = 20;
		h =20;
	}
	
	public void draw(Graphics g){
		if(!live) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		move();
	}

	private void move() {
		step++;
		if(step == pos.length){
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	//为碰撞检测做准备
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
}
