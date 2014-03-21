package com.imgedit;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.BorderExtender;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import javax.media.jai.operator.TransposeDescriptor;

public class ImageMatrice {
	private float scale;
	private int angleRotation;
	private RenderedOp image;
	private float w, h;
	private String format;
	/*
	 * Contructeur de l'image matrice
	 * @param nomImage nom de l'image matrice
	 * @param w largeur de la fenêtre
	 * @param h hauteur de la fenêtre
	 */
	public ImageMatrice(String nomImage, int w, int h){
		image = JAI.create("fileload", nomImage);
		scale = (float)(h-60)/(float)(image.getHeight());
		angleRotation = 0;
		this.w = (float)w;
		this.h = (float)h;
		format = "portrait";
	}
	
	public void zoomPlus(){
		this.scale*=1.1;
	}
	
	public void rotationGaughe(){
		if (this.angleRotation==0)
			this.angleRotation = this.angleRotation+360-90;
		else
			this.angleRotation = this.angleRotation-90;
	}
	
	public void rotationDroite(){
		this.angleRotation = (this.angleRotation+90)%360;
	}
	
	public void zoomMoins(){
		this.scale*=0.9;
	}
	
	public void setFormat(String format){
		this.format = format;
	}
	
	public RenderedOp getImage(){
		RenderedOp imageActuel;
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(scale);
		pb.add(scale);
		pb.add(0.0f);
		pb.add(0.0f);
		pb.add(new InterpolationBilinear());
		imageActuel = JAI.create("scale", pb);
		if (angleRotation!=0){
		pb = new ParameterBlock();
			pb.addSource(imageActuel);
			if (this.angleRotation==90)
				pb.add(TransposeDescriptor.ROTATE_90);
			if (this.angleRotation==180)
				pb.add(TransposeDescriptor.ROTATE_180);
			if (this.angleRotation==270)
				pb.add(TransposeDescriptor.ROTATE_270);
			imageActuel = JAI.create("transpose", pb);
		}
		return imageActuel;
	}
	
	public void cropImage(float x, float y, float w, float h){
		// Changement de repère
		x=x/scale;
		y=y/scale;
		w=w/scale;
		h=h/scale;
		
		if (angleRotation==90){
			x = y;
			y = image.getHeight()-x-w;
			float wtmp = w;
			w = h;
			h = wtmp;
		}
		if (angleRotation==180){
			x = image.getWidth()-x-w;
			y = image.getHeight()-y-h;
		}
		if (angleRotation==270){
			x = image.getWidth()-y-h;
			y = x;
			float wtmp = w;
			w = h;
			h = wtmp;
		}
		RenderedOp cropedImage;
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(x);
		pb.add(y);
		pb.add(w);
		pb.add(h);
		cropedImage = JAI.create("crop", pb);
		
		int leftPad = 200, rightPad = 100;
		int topPad = 200, bottomPad = 200;
		RenderedOp scaledImage;
		if (format=="portrait"){
			float scaleA4 = (float)(2480-leftPad-rightPad)/cropedImage.getWidth();
			pb = new ParameterBlock();
			pb.addSource(cropedImage);
			pb.add(scaleA4);
			pb.add(scaleA4);
			pb.add(0.0f);
			pb.add(0.0f);
			scaledImage = JAI.create("scale", pb);
			bottomPad = 3508-scaledImage.getHeight()-topPad;
			
		}else{
			float scaleA4 = (float)(3580-leftPad-rightPad)/cropedImage.getWidth();
			pb = new ParameterBlock();
			pb.addSource(cropedImage);
			pb.add(scaleA4);
			pb.add(scaleA4);
			pb.add(0.0f);
			pb.add(0.0f);
			scaledImage = JAI.create("scale", pb);
			bottomPad = 2480-scaledImage.getHeight()-topPad;
		}
		pb = new ParameterBlock();
		pb.addSource(scaledImage);
		pb.add(leftPad);
		pb.add(rightPad);
		pb.add(topPad);
		pb.add(bottomPad);
		BorderExtender be = BorderExtender.createInstance(BorderExtender.BORDER_ZERO) ;
		pb.add(be);
		RenderedOp dest = JAI.create("border", pb);
		JAI.create("filestore", dest, "image/crop.tif", "TIFF", null);
	}
}
