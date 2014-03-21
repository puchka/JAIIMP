package com.imgedit;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This abstract class is a wildcard for annotations which can be drawn upon an
 * image. Any class that can be drawn must have its own drawing method and
 * some getters and setters.
 */ 
public abstract class DrawableAnnotation{
	private Color color;
  
	public abstract void paint(Graphics2D g2d);
 
	/**
  	Get the color for this drawable annotation.
	*/
	public Color getColor(){
		return color;
	}

	/**
  	Set the color for this drawable annotation.
	*/
	public void setColor(Color c){
		color = c;
	}

}
