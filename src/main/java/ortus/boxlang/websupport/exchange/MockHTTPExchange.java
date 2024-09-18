/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ortus.boxlang.websupport.exchange;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.web.context.WebRequestBoxContext;
import ortus.boxlang.web.exchange.BoxCookie;
import ortus.boxlang.web.exchange.IBoxHTTPExchange;

/**
 * A Testing class to test the HTTPExchange. Great for mocking, testing, and extending.
 * Mock the methods you need to test, and throw an UnsupportedOperationException for the rest.
 * Idea: Use the Request Scope to track data and simulate requests.
 */
public class MockHTTPExchange implements IBoxHTTPExchange {

	// Mock Server Properties
	private String					webroot;
	private String					host;
	private int						port;
	private boolean					secure;
	// Mock Request Properties
	private String					requestPath			= "/";
	private String					requestMethod		= "GET";
	private String					requestPathInfo		= "";
	private String					requestQueryString	= "";
	Object							requestBody			= null;
	String							requestContentType	= "text/html";

	/**
	 * Request attributes
	 */
	private Map<String, Object>		attributes			= new HashMap<>();

	/**
	 * Form Data
	 */
	private IStruct					mockForm			= new Struct();

	/**
	 * URL Data
	 */
	private IStruct					mockURL				= new Struct();

	/**
	 * Response Status
	 */
	private int						responseStatus		= 200;

	/**
	 * Response Text
	 */
	private String					responseText		= "Ok";

	/**
	 * Mock Response Cookies
	 */
	private IStruct					mockResponseCookies	= new Struct();

	/**
	 * Mock Request Cookies
	 */
	private IStruct					mockRequestCookies	= new Struct();

	/**
	 * Mock Request Headers
	 */
	private IStruct					mockRequestHeaders	= new Struct();

	/**
	 * Mock Response Headers
	 */
	private IStruct					mockResponseHeaders	= new Struct();

	/**
	 * PrintWriter for the response that wraps the channel
	 */
	PrintWriter						writer				= new PrintWriter( new StringWriter() );

	/**
	 * The BoxLang context for this request
	 */
	protected WebRequestBoxContext	context;

	/**
	 * The list of file uploads
	 */
	List<FileUpload>				fileUploads			= new ArrayList<>();

	/**
	 * Create a new BoxLang Mock HTTP exchange
	 *
	 * @param webroot The webroot of the application to mock
	 * @param host    The host of the application to mock
	 * @param port    The port of the application to mock
	 * @param secure  Whether the application is secure or not
	 */
	public MockHTTPExchange( String webroot, String host, int port, boolean secure ) {
		this.port		= port;
		this.host		= host;
		this.webroot	= webroot;
		this.secure		= secure;
	}

	/**
	 * ------------------------------------------------------------------------------------
	 * Getters & Setters
	 * ------------------------------------------------------------------------------------
	 */

	/**
	 * @return the webroot
	 */
	public String getWebroot() {
		return webroot;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the secure
	 */
	public boolean isSecure() {
		return secure;
	}

	/**
	 * @return the requestPath
	 */
	public String getRequestPath() {
		return requestPath;
	}

	/**
	 * Get the response status text
	 */
	public String getResponseStatusText() {
		return this.responseText;
	}

	/**
	 * Get mock request cookies
	 */
	public IStruct getMockRequestCookies() {
		return this.mockRequestCookies;
	}

	/**
	 * Get mock response cookies
	 */
	public IStruct getMockResponseCookies() {
		return this.mockResponseCookies;
	}

	/**
	 * @param webroot the webroot to set
	 */
	public IBoxHTTPExchange setWebroot( String webroot ) {
		this.webroot = webroot;
		return this;
	}

	/**
	 * @param host the host to set
	 */
	public IBoxHTTPExchange setHost( String host ) {
		this.host = host;
		return this;
	}

	/**
	 * @param text the response text to set
	 */
	public IBoxHTTPExchange setResponseText( String text ) {
		this.responseText = text;
		return this;
	}

	/**
	 * Set the request path info
	 *
	 * @param pathInfo The request path info
	 */
	public IBoxHTTPExchange setRequestPathInfo( String pathInfo ) {
		this.requestPathInfo = pathInfo;
		return this;
	}

	/**
	 * Set the request query string
	 *
	 * @param queryString The request query string
	 */
	public IBoxHTTPExchange setRequestQueryString( String queryString ) {
		this.requestQueryString = queryString;
		return this;
	}

	/**
	 * Set request method
	 *
	 * @param method The request method
	 */
	public IBoxHTTPExchange setRequestMethod( String method ) {
		this.requestMethod = method;
		return this;
	}

	/**
	 * Set the request body
	 *
	 * @param body The request body
	 */
	public IBoxHTTPExchange setRequestBody( Object body ) {
		this.requestBody = body;
		return this;
	}

	/**
	 * Set the request content type
	 *
	 * @param contentType The request content type
	 */
	public IBoxHTTPExchange setRequestContentType( String contentType ) {
		this.requestContentType = contentType;
		return this;
	}

	/**
	 * @param port the port to set
	 */
	public IBoxHTTPExchange setPort( int port ) {
		this.port = port;
		return this;
	}

	/**
	 * @param secure the secure to set
	 */
	public IBoxHTTPExchange setSecure( boolean secure ) {
		this.secure = secure;
		return this;
	}

	/**
	 * @param requestPath the requestPath to set
	 */
	public IBoxHTTPExchange setRequestPath( String requestPath ) {
		this.requestPath = requestPath;
		return this;
	}

	/**
	 * ------------------------------------------------------------------------------------
	 * Interface methods
	 * ------------------------------------------------------------------------------------
	 */

	/**
	 * ------------------------------------------------------------------------------------
	 * Response methods
	 * ------------------------------------------------------------------------------------
	 */

	@Override
	public void addResponseCookie( BoxCookie cookie ) {
		this.mockResponseCookies.put( cookie.getName(), cookie );
	}

	@Override
	public void addResponseHeader( String name, String value ) {
		this.mockResponseHeaders.put( name, value );
	}

	@Override
	public void flushResponseBuffer() {
		this.writer.flush();
	}

	@Override
	public String getResponseHeader( String name ) {
		return this.mockResponseHeaders.getAsString( Key.of( name ) );
	}

	@Override
	public Map<String, String[]> getResponseHeaderMap() {
		Map<String, String[]> headers = new HashMap<>();
		this.mockResponseHeaders.forEach( ( key, value ) -> {
			headers.put( key.getName(), new String[] { value.toString() } );
		} );
		return headers;
	}

	@Override
	public int getResponseStatus() {
		return this.responseStatus;
	}

	@Override
	public PrintWriter getResponseWriter() {
		return this.writer;
	}

	@Override
	public boolean isResponseStarted() {
		return true;
	}

	@Override
	public void resetResponseBuffer() {
		this.context.clearBuffer();
	}

	@Override
	public void sendResponseBinary( byte[] data ) {
		ByteBuffer bBuffer = ByteBuffer.wrap( data );
		try {
			this.writer.write( new String( bBuffer.array(), "UTF-8" ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendResponseFile( File arg0 ) {
		// TODO Brad need help here on what to do
		throw new UnsupportedOperationException( "Unimplemented method 'sendResponseFile'" );
	}

	@Override
	public void setResponseHeader( String name, String value ) {
		this.mockResponseHeaders.put( name, value );
	}

	@Override
	public void setResponseStatus( int sc ) {
		this.responseStatus = sc;
	}

	@Override
	public void setResponseStatus( int sc, String sm ) {
		this.responseStatus	= sc;
		this.responseText	= sm;
	}

	/**
	 * ------------------------------------------------------------------------------------
	 * Request methods
	 * ------------------------------------------------------------------------------------
	 */

	@Override
	public void forward( String URI ) {
		this.requestPath = URI;
	}

	@Override
	public Object getRequestAttribute( String name ) {
		return this.attributes.get( name );
	}

	@Override
	public Map<String, Object> getRequestAttributeMap() {
		return this.attributes;
	}

	@Override
	public String getRequestAuthType() {
		// TODO Brad need help here on what to do
		return "help";
	}

	@Override
	public Object getRequestBody() {
		return this.requestBody;
	}

	@Override
	public String getRequestCharacterEncoding() {
		return "UTF-8";
	}

	@Override
	public long getRequestContentLength() {
		return 0;
	}

	@Override
	public String getRequestContentType() {
		return this.requestContentType;
	}

	@Override
	public String getRequestContextPath() {
		return this.requestPath;
	}

	@Override
	public BoxCookie getRequestCookie( String cookie ) {
		return ( BoxCookie ) this.mockRequestCookies.get( cookie );
	}

	@Override
	public BoxCookie[] getRequestCookies() {
		return this.mockRequestCookies.values().toArray( new BoxCookie[ 0 ] );
	}

	@Override
	public Map<String, String[]> getRequestFormMap() {
		Map<String, String[]> formMap = new HashMap<>();
		this.mockForm.forEach( ( key, value ) -> {
			formMap.put( key.getName(), new String[] { value.toString() } );
		} );
		return formMap;
	}

	@Override
	public String getRequestHeader( String name ) {
		return this.mockRequestHeaders.getAsString( Key.of( name ) );
	}

	@Override
	public Map<String, String[]> getRequestHeaderMap() {
		Map<String, String[]> headers = new HashMap<>();
		this.mockRequestHeaders.forEach( ( key, value ) -> {
			headers.put( key.getName(), new String[] { value.toString() } );
		} );
		return headers;
	}

	@Override
	public String getRequestLocalAddr() {
		return "127.0.0.1";
	}

	@Override
	public String getRequestLocalName() {
		return this.host;
	}

	@Override
	public int getRequestLocalPort() {
		return this.port;
	}

	@Override
	public Locale getRequestLocale() {
		return Locale.getDefault();
	}

	@Override
	public Enumeration<Locale> getRequestLocales() {
		List<Locale> ret = List.of( getRequestLocale() );
		if ( ret.isEmpty() ) {
			return Collections.enumeration( Collections.singletonList( Locale.getDefault() ) );
		}
		return Collections.enumeration( ret );
	}

	@Override
	public String getRequestMethod() {
		return this.requestMethod;
	}

	@Override
	public String getRequestPathInfo() {
		return this.requestPathInfo;
	}

	@Override
	public String getRequestPathTranslated() {
		return Path.of( this.webroot, getRequestURI() ).toString();
	}

	@Override
	public String getRequestProtocol() {
		return "HTTP/1.1";
	}

	@Override
	public String getRequestQueryString() {
		return this.requestQueryString;
	}

	@Override
	public String getRequestRemoteAddr() {
		return "0.0.0.0";
	}

	@Override
	public String getRequestRemoteHost() {
		return "localhost";
	}

	@Override
	public int getRequestRemotePort() {
		return 0;
	}

	@Override
	public String getRequestRemoteUser() {
		return "";
	}

	@Override
	public String getRequestScheme() {
		return "";
	}

	@Override
	public String getRequestServerName() {
		return this.host;
	}

	@Override
	public int getRequestServerPort() {
		return this.port;
	}

	@Override
	public String getRequestURI() {
		if ( this.port == 80 ) {
			return "http://" + this.host + "/" + this.requestPath;
		}
		if ( this.port == 443 ) {
			return "https://" + this.host + "/" + this.requestPath;
		}
		if ( this.isSecure() ) {
			return "https://" + this.host + ":" + this.port + "/" + this.requestPath;
		}
		return "http://" + this.host + ":" + this.port + "/" + this.requestPath;
	}

	@Override
	public StringBuffer getRequestURL() {
		return new StringBuffer( getRequestURI() );
	}

	@Override
	public Map<String, String[]> getRequestURLMap() {
		Map<String, String[]> urlMap = new HashMap<>();
		this.mockURL.forEach( ( key, value ) -> {
			urlMap.put( key.getName(), new String[] { value.toString() } );
		} );
		return urlMap;
	}

	@Override
	public Principal getRequestUserPrincipal() {
		return null;
	}

	@Override
	public FileUpload[] getUploadData() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getUploadData'" );
	}

	@Override
	public WebRequestBoxContext getWebContext() {
		return this.context;
	}

	@Override
	public boolean isRequestSecure() {
		return this.secure;
	}

	@Override
	public void removeRequestAttribute( String arg0 ) {
		this.attributes.remove( arg0 );
	}

	@Override
	public void setRequestAttribute( String name, Object value ) {
		this.attributes.put( name, value );
	}

	@Override
	public void setWebContext( WebRequestBoxContext context ) {
		this.context = context;
	}

}
