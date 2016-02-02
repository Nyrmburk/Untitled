package tool;

public class Draw extends Tool {

	Shape currentShape;
	
	Tool subtool;
	
	class Line extends Tool {
		
		Node previousNode;
		
		public Node addPoint(float x, float y) {
			
			Node node = new Node(x, y);
			
			if (previousNode != null)
				Node.link(previousNode, node);
			previousNode = node;
			currentShape.addNode(node);
			
			return node;
		}
	}
}
