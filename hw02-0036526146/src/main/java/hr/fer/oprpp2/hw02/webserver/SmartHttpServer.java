package hr.fer.oprpp2.hw02.webserver;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.oprpp2.hw02.custom.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp2.hw02.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp2.hw02.webserver.RequestContext.RCCookie;

/**
 * Smart HTTP server
 *
 */
public class SmartHttpServer {

	private String address;
	private String domainName;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	private ServerThread serverThread;
	private ExecutorService threadPool;
	private Path documentRoot;
	
	private Map<String,IWebWorker> workersMap = new HashMap<>();
	
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	private Random sessionRandom = new Random();
	
	private MemoryCleanThread memoryCleanThread = new MemoryCleanThread();
	private boolean stopMemoryCleaning = false;

	/**Constructor
	 * @param configFileName
	 */
	public SmartHttpServer(String configFileName) {
		
		try {
			setup(Paths.get(configFileName));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**Properties setup
	 * @param path
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private void setup(Path path) throws IOException {
		Properties properties = new Properties();
		properties.load(Files.newInputStream(path));
		
		this.address = properties.getProperty("server.address");
        this.domainName = properties.getProperty("server.domainName");
        this.port = Integer.parseInt(properties.getProperty("server.port"));
        this.workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
        this.sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
        this.documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
        
        Properties mimeProperties = new Properties();
        mimeProperties.load(Files.newInputStream(Paths.get(properties.getProperty("server.mimeConfig"))));
        for (var e : mimeProperties.entrySet())
        	this.mimeTypes.put(e.getKey().toString(), e.getValue().toString());
        
        Properties workersProperties = new Properties();
        workersProperties.load(Files.newInputStream(Paths.get(properties.getProperty("server.workers"))));
        
        for (var e : workersProperties.entrySet()) {
        	String cpath = e.getKey().toString();
        	String fqcn = e.getValue().toString();
        	
        	Class<?> referenceToClass = null;
			try {
				referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
        	Object newObject = null;
			try {
				newObject = referenceToClass.newInstance();
	        	IWebWorker iww = (IWebWorker)newObject;
	        	workersMap.put(cpath, iww);
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

        }
       
	}

	/**
	 * Server start
	 */
	protected synchronized void start() {
		this.serverThread = new ServerThread();
		this.serverThread.start();
		this.threadPool = Executors.newFixedThreadPool(workerThreads);
		this.memoryCleanThread.start();
	}

	/**
	 * Server stop
	 */
	@SuppressWarnings("deprecation")
	protected synchronized void stop() {
		this.stopMemoryCleaning = true;
		this.serverThread.stop();
		this.threadPool.shutdown();
		
	}

	/**
	 * Server thread with job
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			try {
				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));

				while (true) {
					Socket client = serverSocket.accept();
					ClientWorker clientWorker = new ClientWorker(client);
					threadPool.submit(clientWorker);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Thread for cleaning memory
	 *
	 */
	private class MemoryCleanThread extends Thread {
		
		private int periodms = 5 * 60 * 1000;
		
		public MemoryCleanThread() {
			setDaemon(true);
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(periodms);
				} catch (InterruptedException e) {
				}
				for (var s : sessions.entrySet()) {
					if (s.getValue().validUntil < System.currentTimeMillis())
						sessions.remove(s.getKey());
				}
				if (stopMemoryCleaning)
					break;
			}
		}
	}
	

	/**
	 * Client worker job
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		private Socket csocket;
		private InputStream istream;
		private OutputStream ostream;
		@SuppressWarnings("unused")
		private String version;
		@SuppressWarnings("unused")
		private String method;
		private String host;
		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> tempParams = new HashMap<String, String>();
		private Map<String, String> permPrams = new HashMap<String, String>();
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		private String SID;
		
		private RequestContext requestContext = null;
		

		/**
		 * Constructor
		 * @param csocket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
			
		}
		
		/**
		 * Close socket
		 * @throws IOException
		 */
		private void closeSocket() throws IOException {
			ostream.flush();
			csocket.close();
		}

		@Override
		public void run() {
			try {
				istream = csocket.getInputStream();
				ostream = csocket.getOutputStream();
				List<String> request = readRequest();
								
				if (request == null || !checkHeaderFirstLine(request.get(0).split(" ")))
					return;
				
				checkSession(request);
				
				String path = extractPath(request);
								
				internalDispatchRequest(path, true);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**Check session
		 * @param requestHeader
		 */
		private void checkSession(List<String> requestHeader) {
			String sidCandidate = null;
			for (var h : requestHeader) {
				if (h.startsWith("Cookie: ")) {
					String[] cookieArr = h.substring("Cookie: ".length()).split(";");
					for (var c : cookieArr) {
						if (c.startsWith("sid=")) {
							sidCandidate = c.substring(5, c.length()-1);
						}
					}
				}
			}
			
			if (sidCandidate == null) {
				createSession();
			}
			else {
				SessionMapEntry sessionMapEntry = sessions.get(sidCandidate);
				if (sessionMapEntry == null || !sessionMapEntry.host.equals(host)) {
					createSession();
					return;
				}
				if (sessionMapEntry.validUntil < System.currentTimeMillis()) {
					sessions.remove(sidCandidate);
					createSession();
					return;
				}
				sessionMapEntry.sid = sidCandidate;
				SID = sidCandidate;
				sessionMapEntry.validUntil = System.currentTimeMillis() + sessionTimeout*1000;
				permPrams = sessionMapEntry.map;
				
			}
		}
		
		/**
		 * Create session, new sid, new session entry
		 */
		private void createSession() {
			
			StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 20; i++) {
                sb.append((char) ((int) 'A' + sessionRandom.nextDouble() * ((int) 'Z' - (int) 'A' + 1)));
            }
            SID = sb.toString();
            
            SessionMapEntry sessionMapEntry = new SessionMapEntry(SID, host, System.currentTimeMillis() + sessionTimeout * 1000);
			sessions.put(SID, sessionMapEntry);
			permPrams = sessionMapEntry.map;
			outputCookies.add(new RCCookie("sid", SID, host, "/", null));
		}
		
		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
		
		/**
		 * @param urlPath
		 * @param directCall
		 * @throws Exception
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			
			if (requestContext == null)
				requestContext = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
		
			Path requestedPath = documentRoot.resolve(urlPath.substring(1));
			
			if (urlPath.startsWith("/private") && directCall == true) {
				sendError(403, "Forbidden");
				closeSocket();
				return;
			}
			
			if (urlPath.startsWith("/ext/")) {
				startExtWorker(urlPath);
				closeSocket();
				return;
			}
			
			IWebWorker webWorker = workersMap.get(urlPath);
			if (webWorker != null) {
				webWorker.processRequest(requestContext);
				closeSocket();
				return;
			}
			
			if (!requestedPath.startsWith(documentRoot)) {
				sendError(403, "Forbidden");
				closeSocket();
				return;
			}
			
			if (!Files.exists(requestedPath) || !Files.isRegularFile(requestedPath) || !Files.isReadable(requestedPath)) {
				sendError(404, "File not found");
				closeSocket();
				return;
			}
			
			
			if (requestedPath.toString().endsWith(".smscr")) {
				String docBody = new String(Files.readAllBytes(requestedPath), StandardCharsets.UTF_8);
                new SmartScriptEngine(new SmartScriptParser(docBody).getDocumentNode(), requestContext).execute();
                
                closeSocket();
                return;
			}
			
			String mimeType = getMimeType(requestedPath.toString().substring(requestedPath.toString().lastIndexOf(".")+1));
			requestContext.setMimeType(mimeType);
			try (InputStream is = new BufferedInputStream(Files.newInputStream(requestedPath))) {
				byte[] buf = new byte[1024];
				while (true) {
					int r = is.read(buf);
					if (r < 0) {
						break;
					}
					requestContext.write(buf, 0, r);
				}
			}
			
			closeSocket();
		}
		
		/**
		 * @param extension
		 * @return Returns String mime type
		 */
		private String getMimeType(String extension) {
			String mime = mimeTypes.get(extension);
			if (mime == null)
				return "application/octet-stream";
			return mime;
		}
		
		/**
		 * @param urlPath
		 * @throws Exception
		 */
		private void startExtWorker(String urlPath) throws Exception {
			String fqcn = "hr.fer.oprpp2.hw02.webserver.workers." + urlPath.substring(5);
			
			Class<?> classRef = this.getClass().getClassLoader().loadClass(fqcn);
			
			@SuppressWarnings("deprecation")
			Object newObject = classRef.newInstance();
			
			IWebWorker iww = (IWebWorker) newObject;
			iww.processRequest(requestContext);
		}

		
		/**
		 * @return List<String> request header
		 * @throws IOException
		 */
		private List<String> readRequest() throws IOException{
			byte[] request = readRequest(istream);
			if (request == null || request.length < 1) {
				sendError(400, "Bad request");
				return null;
			}
			List<String> headerList = extractHeaders(new String(request, StandardCharsets.US_ASCII));
			
			assignHost(headerList);
			
			if (headerList != null) {
				String[] firstLine = headerList.get(0).split(" ");
				if (firstLine.length != 3) {
					sendError(400, "Bad request");
					return null;
				}
				this.method = firstLine[0].toUpperCase();
				this.version = firstLine[2].toUpperCase();

			}
			

			return headerList;
		}
				
		private byte[] readRequest(InputStream is) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
	l:		while(true) {
				int b = is.read();
				if(b==-1) {
					if(bos.size()!=0) {
						throw new IOException("Incomplete header received.");
					}
					return null;
				}
				if(b!=13) {
					bos.write(b);
				}
				switch(state) {
				case 0: 
					if(b==13) { state=1; } else if(b==10) state=4;
					break;
				case 1: 
					if(b==10) { state=2; } else state=0;
					break;
				case 2: 
					if(b==13) { state=3; } else state=0;
					break;
				case 3: 
					if(b==10) { break l; } else state=0;
					break;
				case 4: 
					if(b==10) { break l; } else state=0;
					break;
				}
			}
			return bos.toByteArray();
			
		}
		
		private static List<String> extractHeaders(String requestHeader) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for(String s : requestHeader.split("\n")) {
				if(s.isEmpty()) break;
				char c = s.charAt(0);
				if(c==9 || c==32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}
		
		/** Check if header line valid
		 * @param headerFirstLine
		 * @return
		 * @throws IOException
		 */
		private boolean checkHeaderFirstLine(String[] headerFirstLine) throws IOException {
			if (headerFirstLine.length != 3) {
				sendError(400, "Bad request");
				return false;
			} else if (!headerFirstLine[0].toLowerCase().equals("get")) {
				sendError(405, "Method not allowed");
				return false;
			}else if (!headerFirstLine[2].toLowerCase().equals("http/1.0") &&
					!headerFirstLine[2].toLowerCase().equals("http/1.1")) {
				sendError(505, "HTTP version not supported");
				return false;
			}
			return true;
		}
		
		private void assignHost(List<String> requestHeader) {
			for (var l : requestHeader) {
				if (l.startsWith("Host: "))
					this.host = l.split(":")[1].trim();
			}
			if (this.host == null)
				this.host = domainName;
		}
		
		private String extractPath(List<String> requestHeader) throws IOException {
			String pathLine = requestHeader.get(0).split(" ")[1];
			String[] pathLineArr = pathLine.split("\\?");
			if (pathLineArr.length > 1)
				parseParameters(pathLineArr[1]);
			return pathLineArr[0];
		}
		
		private void parseParameters(String parametersString) throws IOException {
			String[] parts = parametersString.split("&");
			for (var part: parts) {
				String[] pairNameValue = part.strip().split("=");
				if (pairNameValue.length % 2 != 0) {
					sendError(400, "Bad request");
					return;
				}
					
				params.put(pairNameValue[0].strip(), pairNameValue[1].strip());
			}
		}
		
		/** Send error with parameters
		 * @param statusCode
		 * @param statusText
		 * @throws IOException
		 */
		private void sendError(int statusCode, String statusText) throws IOException {
			ostream.write(
					("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
					"Server: simple java server\r\n"+
					"Content-Type: "+ "text/html;charset=UTF-8"+"\r\n"+
					"Content-Length: "+"0"+"\r\n"+
					"Connection: close\r\n"+
					"\r\n").getBytes(StandardCharsets.US_ASCII)
				);
			ostream.flush();
			closeSocket();
		}

		
	}
	
	
	/**
	 * Model of session entry
	 *
	 */
	private static class SessionMapEntry {
		
		@SuppressWarnings("unused")
		String sid;
		String host;
		long validUntil;
		Map<String,String> map;
	
		public SessionMapEntry(String sid, String host, long validUntil) {
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			map = new ConcurrentHashMap<String, String>();
		}
	
	}
	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected path to file!");
			return;
		}
		
//		String t = "C:/Users/user/eclipse-workspace/hw02-0036526146/config/server.properties";
		
		SmartHttpServer server = new SmartHttpServer(args[0]);
		server.start();
		
	}
}
