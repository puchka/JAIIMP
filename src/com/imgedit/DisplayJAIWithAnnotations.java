package com.imgedit;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;

import java.util.ArrayList;

import com.sun.media.jai.widget.DisplayJAI;

/**
 * This class shows how one can extend the DisplayJAI class. We'll draw
 * over the image some annotations which are instances of any class which
 * inherits from DrawableAnnotation.
 */

@SuppressWarnings("serial")
public class DisplayJAIWithAnnotations extends DisplayJAI implements MouseMotionListener{
	protected ArrayList<DrawableAnnotation> annotations; // a list of annotations that will be
     	                             // (non-interactively) drawn.
	private ArrayList<Point> points;
	private int[] coordsRect = new int[4];
	private Toolkit toolkit = this.getToolkit();
	protected int width,height; // the dimensions of the dj
	private boolean selectionMode;
	private boolean dragging;
	private boolean selection = false;
	private boolean selectionMainLevee = false;
	private boolean polyOn = false;
	private String selectionType;

	/**
	* The constructor of the class, which creates the arrays and instances needed 
	* to obtain the image data and registers the class to listen to mouse motion 
	* events.
	* @param image a RenderedImage for display
	*/
	public DisplayJAIWithAnnotations(RenderedImage image){
		super(image); // calls the constructor for DisplayJAI
		
		// Get some data about the dj
		Dimension dimDj = toolkit.getScreenSize();
		width = (int)dimDj.getWidth();
		height = (int)dimDj.getHeight();
		
		// Registers the mouse motion listener.  
		addMouseMotionListener(this);
		addMouseListener(this);
		
		// Create the list that will held the drawings.
		annotations = new ArrayList<DrawableAnnotation>();
		points = new ArrayList<Point>();
		selectionMode = false;
		this.dragging = false;
		
		
	}

	/**
	* This method will be called when the mouse is moved over the image being 
	* displayed.
	* @param me the mouse event that caused the execution of this method.
	*/
	public void mouseMoved(MouseEvent me){
		int x = me.getX();
		int y = me.getY();
		
		Point2D center = new Point2D.Double(x, y);

		if (selectionMode && !selection && !selectionMainLevee && selectionType!="m" && !polyOn){
			annotations.clear();
			CrossAnnotation repere = new CrossAnnotation(center, width, height);
			repere.setColor(Color.RED);
			annotations.add(repere);
			this.repaint();
			
		}
		
	} // end of method mouseMoved
	
	@Override
	public void mouseClicked(MouseEvent me){
		int x = me.getX();
		int y = me.getY();
		selection = false;
		if (!selectionMainLevee && selectionType!="m"){
			annotations.clear();
		}
		if (polyOn && !selectionMainLevee && selectionType=="m"){
			annotations.clear();
			points.clear();
			selectionMainLevee = true;
		}
		if (selectionMainLevee){
			annotations.add(new PointAnnotation(x, y));
			points.add(new Point(x, y));
			if (points.size()>1){
				int s = points.size();
				annotations.add(new LineAnnotation(
									(int)points.get(s-2).getX(),
									(int)points.get(s-2).getY(),
									(int)points.get(s-1).getX(),
									(int)points.get(s-1).getY()));
				if (x>points.get(0).getX()-2 &&
						x<points.get(0).getX()+2 &&
						y>points.get(0).getY()-2 &&
						y<points.get(0).getY()+2){
						//performSelection();
						selectionMainLevee = false;
						polyOn = true;
						selectionMode = false;
				}
			}
		}
		this.repaint();
		
	}
	
	@Override
	public void mousePressed(MouseEvent me){
		int x = me.getX();
		int y = me.getY();
		if (selectionMode && (selectionType=="r" || selectionType=="e")){
			coordsRect[0] = x;
			coordsRect[1] = y;
			selection = false;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent me){
		if (selectionMode){
			this.dragging = false;
			selection = true;
			
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent me){
		int x = me.getX();
		int y = me.getY();
		this.dragging = true;
		if (selectionMode && (selectionType=="r" || selectionType=="e")){
			int widthImage = this.getSource().getWidth();
			int heightImage = this.getSource().getHeight();
			if (x>widthImage)
				x=widthImage;
			if (y>heightImage)
				y=heightImage;
			coordsRect[2] = x-coordsRect[0];
			coordsRect[3] = y-coordsRect[1];
			annotations.clear();
			
			Point2D center = new Point2D.Double(coordsRect[0], coordsRect[1]);
			CrossAnnotation repere = new CrossAnnotation(center, width, height);
			annotations.add(repere);
			repere.setColor(Color.RED);
			center = new Point2D.Double(x, y);
			repere = new CrossAnnotation(center, width, height);
			annotations.add(repere);
			
			if (selectionType=="r"){
				SelectionRectangle rect = new SelectionRectangle(
											coordsRect[0], coordsRect[1],
											coordsRect[2], coordsRect[3]);
				annotations.add(rect);
			}
			else{
				SelectionEllipse ellipse = new SelectionEllipse(
											coordsRect[0], coordsRect[1],
											coordsRect[2], coordsRect[3]);
				annotations.add(ellipse);
			}
			this.repaint();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent me){
		
	}
	
	@Override
	public void mouseExited(MouseEvent me){
		if (!selection){
			if (!selectionMainLevee && !polyOn)
				annotations.clear();
			this.repaint();
		}
	}

	/**
	* This method paints the component, calling the paint method of the ancestral
	* class and then painting all annotations.
	*/
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		// Paints each annotation on the list.
		for (int a=0;a<annotations.size();a++){
			DrawableAnnotation element = (DrawableAnnotation)annotations.get(a);
			element.paint(g2d);
		}
	}

	/**
	* This method adds an instance of any class that inherits from 
	* DrawableAnnotation to the list of annotations which will be painted on the 
	* image.
	*/
	public void addAnnotation(DrawableAnnotation a){
		annotations.add(a);	
  	}
	
	/**
  	This method returns the number of annotations being used by the instance.
	*/
	public int annotationsSize(){
		return annotations.size();	
	}
	
	public boolean getSelectionMode(){
		return selectionMode;
	}
	
	public void setSelectionMode(boolean state){
		selectionMode = state;
	}
	
	public String getSelectionType(){
		return selectionType;
	}
	
	public void setSelectionType(String selectionType){
		this.selectionType = selectionType;
	}
	
	public boolean getSelectionMainLevee(){
		return selectionMainLevee;
	}
	
	public void setSelectionMainLevee(boolean state){
		this.selectionMainLevee = state;
	}
	
	public void performSelection(){
		points.clear();
	}
	
	public void clearAnnotations(){
		annotations.clear();
	}
	
	public void setPolyOn(boolean state){
		this.polyOn = state;
	}
	
	public void crop(ImageMatrice imageMatrice){
		if (selection){
			if (coordsRect[2]>0 && coordsRect[3]>0){
				imageMatrice.cropImage(coordsRect[0], coordsRect[1],
									   coordsRect[2], coordsRect[3]);
			}
			else if (coordsRect[2]>0){
				imageMatrice.cropImage(coordsRect[0],
			   			   			   coordsRect[1]+coordsRect[3],
			   			   			   Math.abs(coordsRect[2]),
			   			   			   Math.abs(coordsRect[3]));
			}
			else if (coordsRect[3]>0){
				imageMatrice.cropImage(coordsRect[0]+coordsRect[2],
			   			   			   coordsRect[1],
			   			   			   Math.abs(coordsRect[2]),
			   			   			   Math.abs(coordsRect[3]));
			}
			else{
				imageMatrice.cropImage(coordsRect[0]+coordsRect[2],
						   			   coordsRect[1]+coordsRect[3],
						   			   Math.abs(coordsRect[2]),
						   			   Math.abs(coordsRect[3]));
			}
			selection = false;
		}
	}
	
	public void cleanImage(ImageDecoupee image, String type){
		if (selection){
			if (coordsRect[2]>0 && coordsRect[3]>0){
				image.cleanImage(coordsRect[0], coordsRect[1],
								 coordsRect[2], coordsRect[3], type);
			}
			else if (coordsRect[2]>0){
				image.cleanImage(coordsRect[0],
			   			   		 coordsRect[1]+coordsRect[3],
			   			   		 Math.abs(coordsRect[2]),
			   			   		 Math.abs(coordsRect[3]), type);
			}
			else if (coordsRect[3]>0){
				image.cleanImage(coordsRect[0]+coordsRect[2],
			   			   		 coordsRect[1],
			   			   		 Math.abs(coordsRect[2]),
			   			   		 Math.abs(coordsRect[3]), type);
			}
			else{
				image.cleanImage(coordsRect[0]+coordsRect[2],
						   		 coordsRect[1]+coordsRect[3],
						   		 Math.abs(coordsRect[2]),
						   		 Math.abs(coordsRect[3]), type);
			}
			this.set(image.getImage());
			selection = false;
		}
		if (this.polyOn){
			int[] x = new int[points.size()-1];
			int[] y = new int[points.size()-1];
			for (int i=0;i<points.size()-1;i++){
				x[i] = points.get(i).x;
				y[i] = points.get(i).y;
				/*
				System.out.println("x["+i+"] = "+x[i]);
				System.out.println("y["+i+"] = "+y[i]);*/
			}
			image.cleanImage(x, y, points.size()-1, "m");
			polyOn = false;
			performSelection();
			this.set(image.getImage());
			clearAnnotations();
		}
	}
	
}
