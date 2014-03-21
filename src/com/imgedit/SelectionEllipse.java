package com.imgedit;

import java.awt.Graphics2D;

public class SelectionEllipse extends DrawableAnnotation {
	private int x, y, w, h;
	public SelectionEllipse(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public void paint(Graphics2D g2d){
		g2d.setColor(getColor());
		if (w>0 && h>0)
			g2d.drawOval(x, y, w, h);
		else if (w<0 && h>0)
			g2d.drawOval(x+w, y, Math.abs(w), h);
		else if (w>0 && h<0)
			g2d.drawOval(x, y+h, w, Math.abs(h));
		else
			g2d.drawOval(x+w, y+h, Math.abs(w), Math.abs(h));
	}

}
