package com.imgedit;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * This class represents (and is able to draw) a cross over an image.
 */
public class CrossAnnotation extends DrawableAnnotation{
	/** The center for drawing the cross annotation */
	private Point2D center;
	/** The width of the cross annotation */
	private double width;
	/** The height of the cross annotation */
	private double height;
  
	/**
	 * The constructor, used to initialize the center, width and height of a 
	 * cross annotation
	*/	
	public CrossAnnotation(Point2D c,double w,double h){
		center = c;
		width = w;
		height = h;
	}
	
	/**
	 * This method will draw a cross using the center coordinates, a width
	 * and a height.
	*/
	public void paint(Graphics2D g2d){
		int x = (int)center.getX();
		int y = (int)center.getY();
		int xmin = (int)(x-width);
		int xmax = (int)(x+width);
		int ymin = (int)(y-height);
		int ymax = (int)(y+height);
		g2d.setColor(getColor());
		g2d.drawLine(x,ymin,x,ymax);
		g2d.drawLine(xmin,y,xmax,y);
    }
}
