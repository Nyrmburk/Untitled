package gui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import main.Engine;
import main.Settings;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

public class MenuBar extends Panel {
	
	InputStream inputStream;
	FormattedFont font;
	TextBox time;
	
	public MenuBar() {
		
		super();
		
		this.setlayout(new GUIMenuBarLayout(this));
		
		this.setBounds(0,
				Settings.windowHeight - GUI.content - 2 * GUI.padding,
				Settings.windowWidth, GUI.content + 2 * GUI.padding);
		
		font = GUI.mainFont;
		font.horizontalAlignment = FormattedFont.HorizontalAlignment.RIGHT;
		font.verticalAlignment = FormattedFont.VerticalAlignment.CENTER;
		time = new TextBox(font);
		time.getFont().horizontalAlignment = FormattedFont.HorizontalAlignment.RIGHT;
		time.setInsets(0, 0, 0, 5);
		time.setName("txt_time");
		this.addChild(time, "RIGHT:START");
	}
	
	@Override
	public void draw() {
		
		GUI.awtToGL(GUI.systemColor);
		GUI.drawQuad(this);
		
		renderChildren();
		
		GL11.glEnable(GL11.GL_BLEND);
		
		time.setText(new Date().toString());
		// time.setRenderAsTexture(true);
		time.draw(0, Color.white);
		font.drawString(0, 0, String.valueOf(Engine.currentFPS + ", "
				+ Engine.frameQuality));
		GL11.glDisable(GL11.GL_BLEND);
	}
	
//	private void printEvent(java.awt.event.ActionEvent evt) {
//		
////		System.out.println("menu button " + evt.getActionCommand());
//		if (evt.getActionCommand() == Button.CLICKED) {
//			AssetManager.getScript("menu_click.js").eval();
//		}
//	}
}

class GUIMenuBarLayout extends GUILayoutManager {

//	public static final String LEFT_OF = "leftof";
//	public static final String RIGHT_OF = "rightof";
	
	
	//input will look like this: LEFT:btn_entityInfo
	//On left side on the colon is the side of the menubar 
	//that the object will go to. On the right side is the adjacent object.
	//"START" is used for the first index.
	public static final String SIDE_RIGHT = "RIGHT";
	public static final String SIDE_LEFT = "LEFT";
	public static final String START = "START";
	
	private boolean verified = false;
	
	HashMap<GUIElement, MenuBarConstraint> constraints = 
			new HashMap<GUIElement, MenuBarConstraint>();
	
	public GUIMenuBarLayout(Container parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void layout() {
		
		int leftDistance = GUI.padding;
		int rightDistance = GUI.padding;
		
		verifyConstraints();
		
		for (GUIElement child : sortChildren()) {
			
			MenuBarConstraint constraint = constraints.get(child);
			
			if (child instanceof Menu) {
				
				if (constraint == null) continue;
				
				if (constraint.orientation.equals(SIDE_LEFT)) {
					
					child.x = parent.x;
					
				} else {
					
					child.x = parent.width - child.width;
				}
				
				child.y = parent.y - child.height;
				
				//TODO child.pack
				child.height = 250;
				
				continue;
			}
			
			if (constraint != null && constraint.orientation.equals(SIDE_RIGHT)) {
					
				child.x = parent.width - rightDistance - child.width;
				child.y = parent.y + GUI.padding;
				
				rightDistance += child.getWidth() + GUI.padding;
				
			} else {
				
				child.x = leftDistance;
				child.y = parent.y + GUI.padding;
				
				leftDistance += child.getWidth() + GUI.padding;
			}			
		}
	}
	
	public void verifyConstraints() {
		
		if (verified) return;
		
		for (GUIElement child : parent.children) {
			
			MenuBarConstraint constraint = constraints.get(child);
			
			if (constraint != null) {
				
				if (constraint.element == null) {
					
					constraint = new MenuBarConstraint(
							constraint.orientation,
							getFromName(parent.children, constraint.elementName));
					constraints.put(child, constraint);
				}
			}
		}
		
		verified = true;
	}
	
	public ArrayList<GUIElement> sortChildren() {
		
		ArrayList<GUIElement> sortedChildren = new ArrayList<GUIElement>();
		HashMap<GUIElement, GUIElement> unmatchedChildren = 
				new HashMap<GUIElement, GUIElement>();
		
		for (GUIElement child : parent.children) {
			
			MenuBarConstraint constraint = constraints.get(child);
			
			if (constraint != null) {
				
				if (constraint.element != null) {
					
					int index = sortedChildren.indexOf(constraint.element);
					
					if (index > 0) {
						
						sortedChildren.add(index + 1, child);
					} else {
						
						sortedChildren.add(child);
						unmatchedChildren.put(constraint.element, child);
					}
				} else {
					
					if (constraint.elementName.equals(START))
						sortedChildren.add(0, child);
				}
			} else {
				
				sortedChildren.add(child);
			}
			
			if (unmatchedChildren.containsKey(child)) {
				GUIElement unmatched = unmatchedChildren.get(child);
				sortedChildren.remove(unmatched);
				sortedChildren.add(sortedChildren.indexOf(child) + 1, unmatched);
				unmatchedChildren.remove(child);
			}
		}
		
		return sortedChildren;
	}

	@Override
	public void setConstraint(GUIElement element, Object constraint) {
		
		if (constraint == null)
			return;
		
		String[] temp = ((String) constraint).split(":");
		
		constraints.put(element, new MenuBarConstraint(temp[0], 
				getFromName(parent.children, temp[1])));
		
		verified = false;
	}
	
	private Object getFromName(java.util.ArrayList<GUIElement> list, String name) {
		
		for (GUIElement element : list) {
			
			if (element.getName().equals(name))
				return element;
		}
		
		return name;
	}
	
	class MenuBarConstraint {
		
		String orientation;
		GUIElement element;
		String elementName;
		
		MenuBarConstraint(String orientation, Object element) {
			
			this.orientation = orientation;
			
			if (element instanceof String)
				elementName = (String) element;
			else if (element instanceof GUIElement)
				this.element = (GUIElement) element;
		}
	}
}
