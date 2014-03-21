package com.imgedit;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JButton;

import javax.swing.BoxLayout;
import java.awt.Dimension;

public class ToolBar extends JPanel{
	JButton zoomPlus = new JButton("ZoomPlus");
	JButton zoomMoins = new JButton("ZoomMoins");
	JButton rotationGauche = new JButton("RotationGauche");
	JButton rotationDroite = new JButton("RotationDroite");
	JButton selectionRect = new JButton("Sélection");
	JButton selectionEllipse = new JButton("Ellipse");
	JButton selectionMainLevee = new JButton("Main levée");
	JButton crop = new JButton("Crop");
	JButton cleanButton = new JButton("Clean");
	JComboBox format = new JComboBox();
	
	public JButton getZoomPlus() {
		return zoomPlus;
	}
	
	public JButton getZoomMoins() {
		return zoomMoins;
	}

	public JButton getRotationGauche() {
		return rotationGauche;
	}

	public JButton getRotationDroite() {
		return rotationDroite;
	}
	
	public JButton getSelectionRect(){
		return selectionRect;
	}
	
	public JButton getSelectionEllispe(){
		return selectionEllipse;
	}
	
	public JButton getSelectionMainLevee(){
		return selectionMainLevee;
	}
	
	public JButton getCrop(){
		return crop;
	}
	
	public JButton getCleanButton(){
		return cleanButton;
	}
	
	public JComboBox getFormat(){
		return format;
	}

	/* Constructor of ToolBar
	 * 
	 */
	public ToolBar(){
		this.setMaximumSize(new Dimension(400, 50));
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		this.add(zoomPlus);
		this.add(zoomMoins);
		this.add(rotationGauche);
		this.add(rotationDroite);
		this.add(selectionRect);
		this.add(selectionEllipse);
		this.add(selectionMainLevee);
		this.add(crop);
		this.add(cleanButton);
		
		format.setPreferredSize(new Dimension(200, 20));
		format.addItem("Portrait");
		format.addItem("Paysage");
		
		this.add(format);
	}
}
