package com.imgedit;

import java.awt.Graphics2D;

public class PointAnnotation extends DrawableAnnotation {
	private int x, y;
	public PointAnnotation(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void paint(Graphics2D g2d){
		g2d.setColor(getColor());
		g2d.fillOval(x-2, y-2, 4, 4);
	}

}
