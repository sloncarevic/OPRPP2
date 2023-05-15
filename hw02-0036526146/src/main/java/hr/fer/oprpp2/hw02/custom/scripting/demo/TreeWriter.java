package hr.fer.oprpp2.hw02.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.oprpp2.hw02.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.INodeVisitor;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.TextNode;
import hr.fer.oprpp2.hw02.custom.scripting.parser.SmartScriptParser;

public class TreeWriter {
	
	private static class WriterVisitor implements INodeVisitor {
		
		private StringBuilder sb = new StringBuilder();

		@Override
		public void visitTextNode(TextNode node) {
			String text = node.getText();
			
			sb.append(text);
			
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			sb.append("{FOR "
					+ node.getVariable() + " " 
					+ node.getStartExpression() + " "
					+ node.getEndExpression() + " ");
			
			if (node.getStepExpression() != null) {
                sb.append(node.getStepExpression().asText() + " ");
            }
			
            sb.append("$}");
			
            for (int i = 0; i < node.numberOfChildren(); i++) {
            	node.getChild(i).accept(this);
            }
			
            sb.append("{$END}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			sb.append("{$=");
			
			for (var element : node.getElements()) {
				sb.append(element.asText() + " ");
			}
			
			sb.append("$}");
			
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, l = node.numberOfChildren(); i < l; i++) {
				node.getChild(i).accept(this);
			}
			
			System.out.println(sb.toString());
		}
				
	}

	
	
	public static void main(String[] args) throws IOException {
		
		if (args.length != 1) 
			throw new IllegalArgumentException("Invalid number of arguments!");
		
		Path path = Paths.get(args[0]);
		
		
		
		String docBody = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
		// by the time the previous line completes its job, the document should have been written
		// on the standard output
		
		
	}
	
}
