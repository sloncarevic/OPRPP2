package hr.fer.oprpp2.hw02.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;

import hr.fer.oprpp2.hw02.custom.scripting.elems.Element;
import hr.fer.oprpp2.hw02.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp2.hw02.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp2.hw02.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp2.hw02.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp2.hw02.custom.scripting.elems.ElementString;
import hr.fer.oprpp2.hw02.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.INodeVisitor;
import hr.fer.oprpp2.hw02.custom.scripting.nodes.TextNode;
import hr.fer.oprpp2.hw02.webserver.RequestContext;

public class SmartScriptEngine {

	private DocumentNode documentNode;

	private RequestContext requestContext;

	private ObjectMultistack multistack = new ObjectMultistack();

	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			ValueWrapper startExpression = new ValueWrapper(node.getStartExpression().asText());
			ValueWrapper endExpression = new ValueWrapper(node.getEndExpression().asText());
			ValueWrapper stepExpression = new ValueWrapper(node.getStepExpression().asText());

			multistack.push(variable, startExpression);

			while (multistack.peek(variable).numCompare(endExpression) <= 0) {
				for (int i = 0, l = node.numberOfChildren(); i < l; i++) {
					node.getChild(i).accept(this);
				}
				multistack.peek(variable).add(stepExpression);
			}
			multistack.pop(variable);

		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> temporaryStack = new Stack<>();
			Element[] elements = node.getElements();
			for (var element : elements) {

				if (element instanceof ElementConstantDouble)
					temporaryStack.push(((ElementConstantDouble) element).getValue());

				else if (element instanceof ElementConstantInteger)
					temporaryStack.push(((ElementConstantInteger) element).getValue());

				else if (element instanceof ElementVariable)
					temporaryStack.push(multistack.peek(((ElementVariable) element).asText()).getValue());

				else if (element instanceof ElementString)
					temporaryStack.push(((ElementString) element).asText());

				else if (element instanceof ElementOperator) {
					ValueWrapper operand1 = new ValueWrapper(temporaryStack.pop());
					ValueWrapper operand2 = new ValueWrapper(temporaryStack.pop());

					switch (((ElementOperator) element).asText()) {
					case "+":
						operand1.add(operand2);
						break;
					case "-":
						operand1.subtract(operand2);
						break;
					case "*":
						operand1.multiply(operand2);
						break;
					case "/":
						operand1.divide(operand2);
						break;
					default:
						System.err.println("Invalid operator");
						break;
					}
					temporaryStack.push(operand1.getValue());

				} else if (element instanceof ElementFunction) {
					//System.out.println(((ElementFunction) element).asText());
					switch (((ElementFunction) element).asText()) {

					case "@sin":
						ValueWrapper valueWrapper1 = new ValueWrapper(temporaryStack.pop());
						double o = Double.parseDouble(valueWrapper1.toString());
						temporaryStack.push(new ValueWrapper(Math.sin(Math.toRadians(o))).getValue());
						break;

					case "@decfmt":
						DecimalFormat decimalFormat = new DecimalFormat(temporaryStack.pop().toString());
						ValueWrapper valueWrapper2 = new ValueWrapper(temporaryStack.pop());
						temporaryStack.push(decimalFormat.format(Double.parseDouble(valueWrapper2.toString())));
						break;

					case "@dup":
						ValueWrapper valueWrapper3 = new ValueWrapper(temporaryStack.pop());
						temporaryStack.push(valueWrapper3.getValue());
						temporaryStack.push(valueWrapper3.getValue());
						break;

					case "@swap":
						ValueWrapper valueWrapper4 = new ValueWrapper(temporaryStack.pop());
						ValueWrapper valueWrapper5 = new ValueWrapper(temporaryStack.pop());
						temporaryStack.push(valueWrapper4.getValue());
						temporaryStack.push(valueWrapper5.getValue());
						break;

					case "@setMimeType":
						requestContext.setMimeType(temporaryStack.pop().toString());
						break;

					case "@paramGet":
						String defValue = temporaryStack.pop().toString();
						String name = temporaryStack.pop().toString();
						String paramGetValue = requestContext.getParameter(name);
						temporaryStack.push(paramGetValue == null ? defValue : paramGetValue);
						break;

					case "@pparamGet":
						String defValue2 = temporaryStack.pop().toString();
						String name2 = temporaryStack.pop().toString();
						String pparamGetValue = requestContext.getPersistentParameter(name2);
						temporaryStack.push(pparamGetValue == null ? defValue2 : pparamGetValue);
						break;

					case "@pparamSet":
						String name3 = temporaryStack.pop().toString();
						String value = temporaryStack.pop().toString();
						requestContext.setPersistentParameter(name3, value);
						break;

					case "@pparamDel":
						String name4 = temporaryStack.pop().toString();
						requestContext.removePersistentParameter(name4);
						break;

					case "@tparamGet":
						String defValue5 = temporaryStack.pop().toString();
						String name5 = temporaryStack.pop().toString();
						String tparamGetValue = requestContext.getTemporaryParameter(name5);
						temporaryStack.push(tparamGetValue == null ? defValue5 : tparamGetValue);
						break;

					case "@tparamSet":
						String name6 = temporaryStack.pop().toString();
						String value6 = temporaryStack.pop().toString();
						requestContext.setTemporaryParameter(name6, value6);
						break;

					case "@tparamDel":
						String name7 = temporaryStack.pop().toString();
						requestContext.removeTemporaryParameter(name7);
						break;

					default:
						System.out.println("Unsupported function! : " + ((ElementFunction) element).asText());
						break;
					}

				}

			}
			try {
				for (int i = 0; i < temporaryStack.size(); i++) {
					requestContext.write(temporaryStack.get(i).toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, l = node.numberOfChildren(); i < l; i++) {
				node.getChild(i).accept(this);
			}
		}

	};

	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
		
	}

	public void execute() {
		documentNode.accept(visitor);
	}

}

