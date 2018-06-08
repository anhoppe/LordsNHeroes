package com.lordhero.game.model.items;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class ItemProperty {
	private Object _value;
	
	public ItemProperty(Object value) {
		_value = value;
	}
	
	public ItemProperty(Element propertyNode)  {
		String className = propertyNode.get("Class");
		try {			
			Class type = Class.forName(className);
			
			String valueAsString = propertyNode.get("Value");
			if (type.equals(String.class)) {
				_value = valueAsString;
			}
			else if (type.equals(Integer.class)) {
				_value = new Integer(Integer.parseInt(valueAsString));
			}
			else if (type.equals(Float.class)) {
				_value = new Float(Float.parseFloat(valueAsString));
			}
			else {
				System.err.println("Cannot parse string to type: " + type.getName());
			}
		}
		catch (ClassNotFoundException e) {
			System.err.println("Could not find class for name: " + className);
		}
	}

	public void write() {
		
	}

	public void write(XmlWriter writer) throws IOException {
		String className = _value.getClass().getName();
		writer.attribute("Class", className);
		writer.attribute("Value", _value.toString());		
	}
}
