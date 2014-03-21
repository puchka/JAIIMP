package com.imgedit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.TransposeDescriptor;

public class ImageDecoupee {
	private float scale;
	private int angleRotation;
	private RenderedOp image;
	private float w, h;
	
	/*
	 * Contructeur de l'image matrice
	 * @param nomImage nom de l'image matrice
	 * @param w largeur de la fenêtre
	 * @param h hauteur de la fenêtre
	 */
	public ImageDecoupee(String nomImage, int w, int h){
		image = JAI.create("fileload", nomImage);
		scale = (float)(h-60)/(float)(image.getHeight());
		angleRotation = 0;
		this.w = (float)w;
		this.h = (float)h;
		
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
	
	public void cleanImage(int x, int y, int w, int h, String type){
		int width = image.getWidth();
		int height = image.getHeight();
		SampleModel sm = image.getSampleModel();
		int nbands = sm.getNumBands();
		Raster inputRaster = image.getData();
		int[] pixels = new int[nbands*width*height];
		inputRaster.getPixels(0, 0, width, height, pixels);
		WritableRaster outputRaster = inputRaster.createCompatibleWritableRaster();
		outputRaster.setPixels(0, 0, width, height, pixels);
		
		TiledImage cleanedImage = new TiledImage(image, 20, 20);
		cleanedImage.setData(outputRaster);
		Graphics2D g2d = cleanedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		x/=scale;
		y/=scale;
		w/=scale;
		h/=scale;
		if (type=="r")
			g2d.fillRect(x, y, w, h);
		if (type=="e")
			g2d.fillOval(x, y, w, h);
		
		JAI.create("filestore", cleanedImage, "image/crop.tif", "TIFF");
		image = JAI.create("fileload", "image/crop.tif");
	}
	
	public void cleanImage(int x[], int y[], int nbPoints, String type){
		System.out.println("cleanImagePolygon");
		int width = image.getWidth();
		int height = image.getHeight();
		SampleModel sm = image.getSampleModel();
		int nbands = sm.getNumBands();
		Raster inputRaster = image.getData();
		int[] pixels = new int[nbands*width*height];
		inputRaster.getPixels(0, 0, width, height, pixels);
		WritableRaster outputRaster = inputRaster.createCompatibleWritableRaster();
		outputRaster.setPixels(0, 0, width, height, pixels);
		
		TiledImage cleanedImage = new TiledImage(image, 20, 20);
		cleanedImage.setData(outputRaster);
		Graphics2D g2d = cleanedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		
		System.out.println("sclale = "+this.scale);
		
		for (int i=0;i<x.length;i++){
			System.out.println("x["+i+"] = "+x[i]);
			x[i]=(int)Math.floor((float)x[i]/scale);
			System.out.println("x["+i+"] = "+x[i]);
		}
		for (int i=0;i<y.length;i++){
			System.out.println("y["+i+"] = "+y[i]);
			y[i]=(int)Math.floor((float)y[i]/scale);
			System.out.println("y["+i+"] = "+y[i]);
		}
		
		g2d.fillPolygon(x, y, nbPoints);
		
		JAI.create("filestore", cleanedImage, "image/crop.tif", "TIFF");
		image = JAI.create("fileload", "image/crop.tif");
	}
}
