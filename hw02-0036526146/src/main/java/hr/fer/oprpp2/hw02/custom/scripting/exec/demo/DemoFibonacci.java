package hr.fer.oprpp2.hw02.custom.scripting.exec.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.oprpp2.hw02.custom.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp2.hw02.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp2.hw02.webserver.RequestContext;
import hr.fer.oprpp2.hw02.webserver.RequestContext.RCCookie;

public class DemoFibonacci {

	public static void main(String[] args) throws IOException {
		
		String documentBody = new String(Files.readAllBytes(Paths.get("src/main/resources/fibonacci.smscr")), StandardCharsets.UTF_8);
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
		new SmartScriptParser(documentBody).getDocumentNode(), 
		new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
		
		
	}
}
