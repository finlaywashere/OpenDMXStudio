/*
    Copyright (C) 2021 Finlay Maroney
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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
		return name;
	}
	public String encode() {
		String channelS = ""+channels.length;
		for(Channel c : channels) {
			channelS += ","+c.encode();
		}
		return x+":"+y+":"+type.toString()+":"+name+":"+channelS+":"+radius+":"+(int)(color.getRed()*255)+","+(int)(color.getGreen()*255)+","+(int)(color.getBlue()*255)+":"+id;
	}
}
