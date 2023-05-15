package hr.fer.oprpp2.hw02.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Models context of request
 *
 */
public class RequestContext {
	
	/**
	 * Models cookie 
	 *
	 */
	public static class RCCookie {
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		
		public RCCookie(String name, String value, String domain, String path, Integer maxAge) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String getDomain() {
			return domain;
		}

		public String getPath() {
			return path;
		}

		public Integer getMaxAge() {
			return maxAge;
		}
			
	}
	
	
	private OutputStream outputStream;
	
	private Charset charset;
	
	public String encoding = "UTF-8";
	public int statusCode = 200;
	public String statusText = "OK"; 
	public String mimeType = "text/html";
	public Long contentLength = null;
	
	private Map<String,String> parameters;
	private Map<String,String> temporaryParameters;
	private Map<String,String> persistentParameters;
	private List<RCCookie> outputCookies;
	private boolean headerGenerated = false;
	
	private IDispatcher dispatcher;
	
	private String SID;
	

	public RequestContext(OutputStream outputStream, Map<String,String> parameters, 
			Map<String,String> persistentParameters, List<RCCookie> outputCookies) {
		
		this(outputStream, parameters, persistentParameters, outputCookies, null, null, null);
	}
	
	/**
	 * Default constructor
	 * @param outputStream
	 * @param parameters
	 * @param persistentParameters
	 * @param outputCookies
	 * @param temporaryParameters
	 * @param dispatcher
	 * @param SID
	 */
	public RequestContext(OutputStream outputStream, Map<String,String> parameters, 
			Map<String,String> persistentParameters, List<RCCookie> outputCookies,
			Map<String,String> temporaryParameters, IDispatcher dispatcher, String SID) {
		
		if (outputStream == null)
			throw new NullPointerException("OutputStream can not be null!");
		this.outputStream = outputStream;
		
		this.parameters = parameters == null ? new HashMap<>() : parameters;
		
		this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
		
		this.outputCookies = outputCookies == null ? new ArrayList<>() : outputCookies;
		
		this.temporaryParameters = temporaryParameters == null ? new HashMap<>() : temporaryParameters;
		
		this.dispatcher = dispatcher;
		
		this.SID = SID == null ? null : SID;
	}
	
	/**
	 * @param name
	 * @return Returns parameter by name
	 */
	public String getParameter(String name) {
		return this.parameters.get(name);
	}
	
	/**
	 * @return Returns all parameters names
	 */
	public Set<String> getParameterNames() {
		return new HashSet<>(this.parameters.keySet());
	}
	
	/**
	 * @param name
	 * @return Returns persistent parameter by name
	 */
	public String getPersistentParameter(String name) {
		return this.persistentParameters.get(name);
	}
	
	/**
	 * @return Returns all persistent parameters names
	 */
	public Set<String> getPersistentParameterNames(){
		return new HashSet<>(this.persistentParameters.keySet());
	}

	/**
	 * Persistent parameter setter
	 * @param name
	 * @param value
	 */
	public void setPersistentParameter(String name, String value) {
		this.persistentParameters.put(name, value);
	}
	
	/**
	 * Remove persistent parameter
	 * @param name
	 */
	public void removePersistentParameter(String name) {
		this.persistentParameters.remove(name);
	}

	/**
	 * @param name
	 * @return Return temporary parameter by name
	 */
	public String getTemporaryParameter(String name) {
		return this.temporaryParameters.get(name);
	}
	
	/**
	 * @return  Returns all temporary parameters names
	 */
	public Set<String> getTemporaryParameterNames(){
		return new HashSet<>(this.temporaryParameters.keySet());
	}
	
	/**
	 * @return Returns session id
	 */
	public String getSessionID() {
		return SID;
	}
	
	/**Temporary parameter setter
	 * @param name
	 * @param value
	 */
	public void setTemporaryParameter(String name, String value) {
		this.temporaryParameters.put(name, value);
	}
	
	/**Remove temporary parameter by name
	 * @param name
	 */
	public void removeTemporaryParameter(String name) {
		this.temporaryParameters.remove(name);
	}
	
	/**Writes data into output stream
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated)
			generateHeader();
		
		this.outputStream.write(data);
		return this;
		
	}
	
	/**Writes data into output stream
	 * @param data
	 * @param offset
	 * @param len
	 * @return
	 * @throws IOException
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if (!headerGenerated)
			generateHeader();
		
		this.outputStream.write(data, offset, len);
		return this;
	}
	
	/**Writes data into output stream
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated)
			generateHeader();
		
		this.outputStream.write(text.getBytes(charset));
		return this;
	}

	/**Generate header and write it to output stream
	 * @throws IOException
	 */
	private void generateHeader() throws IOException {
		charset = Charset.forName(encoding);
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
        sb.append("Content-Type: " + mimeType);
        sb.append(mimeType.startsWith("text/") ? "; charset=" + this.encoding : "");
        sb.append(contentLength != null && contentLength != 0 ? "\r\nContent-Length: " + this.contentLength : "");
        sb.append("\r\n");
        for (var rccookie : outputCookies) {
        	sb.append("Set-Cookie: " + rccookie.getName() + "=\"" + rccookie.getValue() + "\"");
        	sb.append(rccookie.getDomain() != null ? "; Domain="+rccookie.getDomain() : "");
        	sb.append(rccookie.getPath() != null ? "; Path="+rccookie.getPath() : "");
        	sb.append(rccookie.getMaxAge() != null ? "; Max-Age=" + rccookie.getMaxAge() : "");
        	sb.append("\r\n");
        }
        sb.append("\r\n");
        
        this.outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
        headerGenerated = true;
		
	}

	public void setEncoding(String encoding) {
		checkHeader();
		this.encoding = encoding;
	}

	public void setStatusCode(int statusCode) {
		checkHeader();
		this.statusCode = statusCode;
	}

	public void setStatusText(String statusText) {
		checkHeader();
		this.statusText = statusText;
	}

	public void setMimeType(String mimeType) {
		checkHeader();
		this.mimeType = mimeType;
	}

	public void setContentLength(Long contentLength) {
		checkHeader();
		this.contentLength = contentLength;
	}
	
		
	public void addRCCookie(RCCookie rcCookie) {
		checkHeader();
		this.outputCookies.add(rcCookie);
	}
	
	private void checkHeader() {
		if (headerGenerated)
			throw new RuntimeException("Header was generated. Can not change property");
	}

	public IDispatcher getDispatcher() {
		return this.dispatcher;
	}
	
	
}
