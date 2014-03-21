package com.imgedit;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* Main Window
 * 
 */

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener, ChangeListener {
	
	private DisplayJAIWithAnnotations dj;
	private Pannel pannel;
	private ToolBar toolBar;
	private Toolkit toolkit = this.getToolkit();
	
	private ImageMatrice imageMatrice;
	private ImageDecoupee imageDecoupee;
	
	private Dimension dimScreen = toolkit.getScreenSize();
	private int w = (int)dimScreen.getWidth();
	private int h = (int)dimScreen.getHeight();
	
	private Image cursorPlus;
	private Image cursorMoins;
	private Image rectCursorImage;
	private Point hotSpot = new Point(0, 0);
	private String name = "zoomCursor";
	private Cursor zoomPlusCursor;
	private Cursor zoomMoinsCursor;
	private Cursor rectCursor;
	
	private JTabbedPane onglet;
	private Pannel pannel2;
	private DisplayJAIWithAnnotations dj2;
	
	/* Constructor of MainWindow
	 * 
	 */
	public MainWindow(){
		this.setSize(w, h);
		this.setTitle("JAIIMP");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		imageMatrice = new ImageMatrice("image/yalelawlibrary.tif", w, h);
		
		dj = new DisplayJAIWithAnnotations(imageMatrice.getImage());
		toolBar = new ToolBar();
		pannel = new Pannel(dj);
		
		try {
			cursorPlus = ImageIO.read(new File("image/zoomplus.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			cursorMoins = ImageIO.read(new File("image/zoommoins.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			rectCursorImage = ImageIO.read(new File("image/rectcursor.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		zoomPlusCursor = toolkit.createCustomCursor(cursorPlus, hotSpot, name);
		zoomMoinsCursor = toolkit.createCustomCursor(cursorMoins, hotSpot, name);
		rectCursor = toolkit.createCustomCursor(rectCursorImage, hotSpot, name);
		
		onglet = new JTabbedPane();
		
		this.add(toolBar, BorderLayout.NORTH);
		onglet.addTab("Onglet", pannel);
		
		this.add(onglet, BorderLayout.CENTER);
		this.setVisible(true);
		
		toolBar.getZoomPlus().addActionListener(this);
		toolBar.getZoomMoins().addActionListener(this);
		toolBar.getRotationGauche().addActionListener(this);
		toolBar.getRotationDroite().addActionListener(this);
		toolBar.getSelectionRect().addActionListener(this);
		toolBar.getSelectionEllispe().addActionListener(this);
		toolBar.getSelectionMainLevee().addActionListener(this);
		toolBar.getCrop().addActionListener(this);
		toolBar.getCleanButton().addActionListener(this);
		toolBar.getFormat().addActionListener(this);
		
		onglet.addChangeListener(this);
		
		toolBar.getCleanButton().setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==toolBar.getZoomPlus()){
			if (onglet.getSelectedIndex()==0){
				imageMatrice.zoomPlus();
				dj.set(imageMatrice.getImage());
				dj.setSelectionMode(false);
				dj.setCursor(zoomPlusCursor);
			}
			if (onglet.getSelectedIndex()==1){
				imageDecoupee.zoomPlus();
				dj2.set(imageDecoupee.getImage());
				dj2.setSelectionMode(false);
				dj2.setCursor(zoomPlusCursor);
			}
		}
	
		if (e.getSource()==toolBar.getZoomMoins()){
			if (onglet.getSelectedIndex()==0){
				imageMatrice.zoomMoins();
				dj.set(imageMatrice.getImage());
				dj.setSelectionMode(false);
				dj.setCursor(zoomMoinsCursor);
			}
			if (onglet.getSelectedIndex()==1){
				imageDecoupee.zoomMoins();
				dj2.set(imageDecoupee.getImage());
				dj2.setSelectionMode(false);
				dj2.setCursor(zoomMoinsCursor);
			}
		}
		
		if (e.getSource()==toolBar.getRotationGauche()){
			imageMatrice.rotationGaughe();
			dj.set(imageMatrice.getImage());
			dj.setSelectionMode(false);
		}
		
		if (e.getSource()==toolBar.getRotationDroite()){
			imageMatrice.rotationDroite();
			dj.set(imageMatrice.getImage());
			dj.setSelectionMode(false);
		}
		
		if (e.getSource()==toolBar.getSelectionRect()){
			if (onglet.getSelectedIndex()==0){
				dj.setSelectionMainLevee(false);
				dj.setPolyOn(false);
				if (!dj.getSelectionMode()){
					dj.setSelectionMode(true);
					dj.setSelectionType("r");
					dj.setCursor(rectCursor);
				}
				else{
					if (dj.getSelectionType().equals("r")){
						dj.setSelectionMode(false);
						dj.setCursor(null);
						dj.clearAnnotations();
						dj.repaint();
					}
					else{
						dj.setSelectionType("r");
						dj.setCursor(rectCursor);
					}
				}
			}
			if (onglet.getSelectedIndex()==1){
				dj2.setSelectionMainLevee(false);
				dj2.setPolyOn(false);
				if (!dj2.getSelectionMode()){
					dj2.setSelectionMode(true);
					dj2.setSelectionType("r");
					dj2.setCursor(rectCursor);
				}
				else{
					if (dj2.getSelectionType().equals("r")){
						dj2.setSelectionMode(false);
						dj2.setCursor(null);
						dj2.clearAnnotations();
						dj2.repaint();
					}
					else{
						dj2.setSelectionType("r");
						dj2.setCursor(rectCursor);
					}
				}
			}
		}
		
		if (e.getSource()==toolBar.getSelectionEllispe()){
			if (onglet.getSelectedIndex()==0){
				dj.setSelectionMainLevee(false);
				dj.setPolyOn(false);
				if (!dj.getSelectionMode()){
					dj.setSelectionMode(true);
					dj.setSelectionType("e");
					dj.setCursor(rectCursor);
				}
				else{
					if (dj.getSelectionType().equals("e")){
						dj.setSelectionMode(false);
						dj.setCursor(null);
						dj.clearAnnotations();
						dj.repaint();
					}
					else
						dj.setSelectionType("e");
				}
			}
			if (onglet.getSelectedIndex()==1){
				dj2.setSelectionMainLevee(false);
				dj2.setPolyOn(false);
				if (!dj2.getSelectionMode()){
					dj2.setSelectionMode(true);
					dj2.setSelectionType("e");
					dj2.setCursor(rectCursor);
				}
				else{
					if (dj2.getSelectionType().equals("e")){
						dj2.setSelectionMode(false);
						dj2.setCursor(null);
						dj2.clearAnnotations();
						dj2.repaint();
					}
					else
						dj2.setSelectionType("e");
				}
			}
		}
		
		if (e.getSource()==toolBar.getSelectionMainLevee()){
			if (!dj.getSelectionMainLevee()){
				dj.setCursor(null);
				dj.setSelectionType("m");
				dj.setSelectionMainLevee(true);
			}
			if (!dj2.getSelectionMainLevee()){
				dj2.setCursor(null);
				dj2.setSelectionType("m");
				dj2.setSelectionMainLevee(true);
			}
		}
		
		if (e.getSource()==toolBar.getCrop()){
			dj.crop(imageMatrice);
			imageDecoupee = new ImageDecoupee("image/crop.tif", w, h);
			dj2 = new DisplayJAIWithAnnotations(imageDecoupee.getImage());
			pannel2 = new Pannel(dj2);
			onglet.addTab("Onglet 2", pannel2);
			onglet.setSelectedIndex(1);
		}
		
		if (e.getSource()==toolBar.getCleanButton()){
			if (onglet.getSelectedIndex()==1){
				dj2.cleanImage(imageDecoupee, dj2.getSelectionType());
			}
		}
		
		if (e.getSource()==toolBar.getFormat()){
			if (toolBar.getFormat().getSelectedIndex()==0){
				imageMatrice.setFormat("portrait");
			}
			else{
				imageMatrice.setFormat("paysage");
			}
		}
		
	}
	
	@Override
	public void stateChanged(ChangeEvent ce){
		if (onglet.getSelectedIndex()==0){
			toolBar.getCrop().setEnabled(true);
			toolBar.getCleanButton().setEnabled(false);
		}
		if (onglet.getSelectedIndex()==1){
			toolBar.getCleanButton().setEnabled(true);
			toolBar.getCrop().setEnabled(false);
		}
		
	}
	
	/* main function
	 * @param args arguments of the command line
	 */
	public static void main(String[] args){
		MainWindow window = new MainWindow();
	}
}
