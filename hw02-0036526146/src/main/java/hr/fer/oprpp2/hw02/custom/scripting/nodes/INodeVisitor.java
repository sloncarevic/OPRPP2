package hr.fer.oprpp2.hw02.custom.scripting.nodes;

/**
 * Models node visitor
 *
 */
public interface INodeVisitor {
	public void visitTextNode(TextNode node);
	public void visitForLoopNode(ForLoopNode node);
	public void visitEchoNode(EchoNode node);
	public void visitDocumentNode(DocumentNode node);

}
