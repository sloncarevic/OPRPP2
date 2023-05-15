package hr.fer.oprpp2.hw02.custom.scripting.nodes;

/**
 * A node representing an entire document
 *
 */
public class DocumentNode extends Node {

	@Override
	public String toString() {
		String value = "";
		for(int i = 0; i < this.numberOfChildren(); i++) {
			value = value + this.getChild(i).toString();
			
		}
		return value; 
	}
	
	@Override
		public void accept(INodeVisitor visitor) {
			visitor.visitDocumentNode(this);
			
		}
	

}
