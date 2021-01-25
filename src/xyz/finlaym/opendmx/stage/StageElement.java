package xyz.finlaym.opendmx.stage;

import javafx.scene.paint.Color;

public class StageElement {
	private double x,y; // X and y in relative stage coordinate space;
	private String name;
	private Channel[] channels;
	private Color color;
	private int radius;
	private StageElementType type;
	private int id;
	
	public StageElement(double x, double y, StageElementType type, String name, Channel[] channels, int radius, Color color, int id) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.name = name;
		this.channels = channels;
		this.radius = radius;
		this.color = color;
		this.id = id;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public StageElementType getType() {
		return type;
	}
	public void setType(StageElementType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Channel[] getChannels() {
		return channels;
	}
	public void setChannels(Channel[] channels) {
		this.channels = channels;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public int getId() {
		return id;
	}
	@Override
	public String toString() {
		String channelS = ""+channels.length;
		for(Channel c : channels) {
			channelS += ","+c.toString();
		}
		return x+":"+y+":"+type.toString()+":"+name+":"+channelS+":"+radius+":"+(int)(color.getRed()*255)+","+(int)(color.getGreen()*255)+","+(int)(color.getBlue()*255)+":"+id;
	}
}
