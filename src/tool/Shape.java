package tool;

import java.util.ArrayList;

public class Shape {
	
	ArrayList<Node> nodes;
	// center?

	public void addNode(Node node) {
		
		nodes.add(node);
	}
}

class Node {
	//possibly look at my other point2d stuff
	
	private Node previous;
	private Node next;
	
	float x;
	float y;
	
	public Node(float x, float y) {
		
		setPos(x, y);
	}
	
	public void setPos(float x, float y) {
		
		this.x = x;
		this.y = y;
	}
	
	public Node getNext() {
		
		return next;
	}
	
	public void setNext(Node next) {
		
		this.next = next;
	}
	
	public Node getPrevious() {
		
		return previous;
	}
	
	public void setPrevious(Node previous) {
		
		this.previous = previous;
	}
	
	public static void link (Node previous, Node next) {
		
		previous.setNext(next);
		next.setPrevious(previous);
	}
}
