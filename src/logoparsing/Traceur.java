/*
  * Created on 12 may. 2018
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package logoparsing;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import logogui.GraphParameter;

public class Traceur {
	private Color color;
	private double initx = 400, inity = 300; // position initiale
	private double posx = initx, posy = inity; // position courante
	private double angle = 90;
	private double teta;
	private boolean posc = true;
	
	ObjectProperty<GraphParameter> line;

	public Traceur() {
		setTeta();
		line = new SimpleObjectProperty<GraphParameter>();
	}
	
	public Traceur(Traceur other) {
		posx = other.posx;
		posy = other.posy;
		angle = other.angle;
	}

	ObjectProperty<GraphParameter> lineProperty() {
		return line;
	}

	private void setTeta() {
		teta = Math.toRadians(angle);
	}

	private void addLine(double x1, double y1, double x2, double y2) {
		line.setValue(new GraphParameter(x1, y1, x2, y2, color));
	}

	public void avance(double r) {
		double a = posx + r * Math.cos(teta);
		double b = posy - r * Math.sin(teta);
		if (posc) {
			addLine(posx, posy, a, b);
		}
		posx = a;
		posy = b;
	}

	public void td(double r) {
		angle = (angle - r) % 360;
		setTeta();
	}
	
	public void tg(double r) {
		angle = (angle + r) % 360;
		setTeta();
	}
	
	public void setPosc(boolean b) {
		posc = b;
	}
	
	public void fpos(double x, double y) {
		if (posc) {
			addLine(posx, posy, x, y);
		}
		posx = x;
		posy = y;
	}
	
	public double getPosx() {
		return posx;
	}
	
	public double getPosy() {
		return posy;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public void setAngle(double a) {
		angle = a;
		setTeta();
	}
	
	public double getAngle() {
		return angle;
	}

}
