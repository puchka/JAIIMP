package com.imgedit;

import java.awt.Graphics2D;

public class SelectionRectangle extends DrawableAnnotation {
	private int x, y, w, h;
	public SelectionRectangle(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public void paint(Graphics2D g2d){
		g2d.setColor(getColor());
		g2d.drawRect(x, y, w, h);
	}

}
