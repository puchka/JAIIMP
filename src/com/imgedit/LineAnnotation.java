package com.imgedit;

import java.awt.Graphics2D;

public class LineAnnotation extends DrawableAnnotation {
	private int x, y, x2, y2;
	public LineAnnotation(int x, int y, int x2, int y2){
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
	}
	public void paint(Graphics2D g2d){
		g2d.setColor(getColor());
		g2d.drawLine(x, y, x2, y2);
	}

}
