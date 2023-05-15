package hr.fer.oprpp2.hw02.webserver.workers;

import hr.fer.oprpp2.hw02.webserver.IWebWorker;
import hr.fer.oprpp2.hw02.webserver.RequestContext;

public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		String html = """
				<!DOCTYPE html>\n
				<html>\n
					<head>\n
					</head>\n
					<style> table, th, td { border: 1px solid black; } </style>\n
					<body>\n
						<table>\n
							<tr>\n
								<th>Argument name</th>\n
								<th>Argument value</th>\n
							</tr>\n
				""";
		for (var name : context.getParameterNames()) {
			html += """
					<tr>\n
					<td> parametername </td>\n
					<td> parametervalue </td>\n
					</tr>\n
					""".replace("parametername", name).replace("parametervalue", context.getParameter(name));
			
		}
		
		html += """
				</table>\n
				</body>\n
				</html>\n
				""";
		
		context.write(html);
			
	}
}
