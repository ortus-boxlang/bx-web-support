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

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.web.WebRequestExecutor;
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
	protected String				webroot;
	protected String				host;
	protected int					port;
	protected boolean				secure;
	// Mock Request Properties
	protected String				requestPath			= "/";
	protected String				requestMethod		= "GET";
	protected String				requestPathInfo		= "";
	protected String				requestQueryString	= "";
	protected Object				requestBody			= null;
	protected String				requestContentType	= "text/html";

	/**
	 * Request attributes
	 */
	protected Map<String, Object>	attributes			= new HashMap<>();

	/**
	 * Form Data
	 */
	protected IStruct				mockForm			= new Struct();

	/**
	 * URL Data
	 */
	protected IStruct				mockURL				= new Struct();

	/**
	 * Response Status
	 */
	protected int					responseStatus		= 200;

	/**
	 * Response Text
	 */
	protected String				responseText		= "Ok";

	/**
	 * Mock Response Cookies
	 */
	protected IStruct				mockResponseCookies	= new Struct();

	/**
	 * Mock Request Cookies
	 */
	protected IStruct				mockRequestCookies	= new Struct();

	/**
	 * Mock Request Headers
	 */
	protected IStruct				mockRequestHeaders	= new Struct();

	/**
	 * Mock Response Headers
	 */
	protected IStruct				mockResponseHeaders	= new Struct();

	/**
	 * PrintWriter for the response that wraps the channel
	 */
	protected PrintWriter			writer				= new PrintWriter( new StringWriter() );

	/**
	 * The BoxLang context for this request
	 */
	protected WebRequestBoxContext	context;

	/**
	 * The caller request context
	 */
	protected IBoxContext			requestContext;

	/**
	 * The list of file uploads
	 */
	protected List<FileUpload>		fileUploads			= new ArrayList<>();

	/**
	 * The Box Runtime
	 */
	private static final BoxRuntime	runtime				= BoxRuntime.getInstance();

	/**
	 * Create a new BoxLang Mock HTTP exchange
	 *
	 * @param webroot        The webroot of the application to mock
	 * @param host           The host of the application to mock
	 * @param port           The port of the application to mock
	 * @param secure         Whether the application is secure or not
	 * @param requestContext The request context to associate with this mock exchange
	 */
	public MockHTTPExchange( String webroot, String host, int port, boolean secure, IBoxContext requestContext ) {
		this.port			= port;
		this.host			= host;
		this.webroot		= webroot;
		this.secure			= secure;
		this.requestContext	= requestContext;
		// Initialize the web context
		initializeWebContext( requestContext );
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
	 * Get the response body as a string
	 *
	 * @return The response body content
	 */
	public String getResponseBody() {
		return this.writer.toString();
	}

	/**
	 * Get the form data struct for inspection
	 *
	 * @return The form data struct
	 */
	public IStruct getMockForm() {
		return this.mockForm;
	}

	/**
	 * Get the URL data struct for inspection
	 *
	 * @return The URL data struct
	 */
	public IStruct getMockURL() {
		return this.mockURL;
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
	 * Get mock request headers
	 */
	public IStruct getMockRequestHeaders() {
		return this.mockRequestHeaders;
	}

	/**
	 * Get mock response headers
	 */
	public IStruct getMockResponseHeaders() {
		return this.mockResponseHeaders;
	}

	/**
	 * Add a request header (fluent)
	 *
	 * @param name  The header name
	 * @param value The header value
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addRequestHeader( String name, String value ) {
		this.mockRequestHeaders.put( name, value );
		return this;
	}

	/**
	 * Add multiple request headers at once (fluent)
	 *
	 * @param headers Map of header names to values
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addRequestHeaders( IStruct headers ) {
		headers.forEach( ( name, value ) -> this.mockRequestHeaders.put( name, value ) );
		return this;
	}

	/**
	 * Add a URL parameter (fluent)
	 *
	 * @param name  The parameter name
	 * @param value The parameter value
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addURLParam( String name, Object value ) {
		this.mockURL.put( name, value );
		return this;
	}

	/**
	 * Add multiple URL parameters at once (fluent)
	 *
	 * @param params Map of parameter names to values
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addURLParams( IStruct params ) {
		params.forEach( ( name, value ) -> this.mockURL.put( name, value ) );
		return this;
	}

	/**
	 * Add a form field (fluent)
	 *
	 * @param name  The field name
	 * @param value The field value
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addFormField( String name, Object value ) {
		this.mockForm.put( name, value );
		return this;
	}

	/**
	 * Add multiple form fields at once (fluent)
	 *
	 * @param fields Map of field names to values
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addFormFields( IStruct fields ) {
		fields.forEach( ( name, value ) -> this.mockForm.put( name, value ) );
		return this;
	}

	/**
	 * Add a request cookie (fluent)
	 *
	 * @param cookie The cookie to add
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addRequestCookie( BoxCookie cookie ) {
		this.mockRequestCookies.put( cookie.getName(), cookie );
		return this;
	}

	/**
	 * Add a request cookie by name and value (fluent)
	 *
	 * @param name  The cookie name
	 * @param value The cookie value
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addRequestCookie( String name, String value ) {
		this.mockRequestCookies.put( name, new BoxCookie( name, value ) );
		return this;
	}

	/**
	 * Add request cookies using a struct (fluent)
	 *
	 * @param cookies Struct of cookie names to values
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange addRequestCookies( IStruct cookies ) {
		cookies.forEach( ( name, value ) -> this.mockRequestCookies.put( name, new BoxCookie( name.getName(), value.toString() ) ) );
		return this;
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
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange setRequestBody( Object body ) {
		this.requestBody = body;
		return this;
	}

	/**
	 * Set request body as JSON (convenience method that also sets content type)
	 *
	 * @param json The JSON string or object
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange setRequestBodyJSON( Object json ) {
		this.requestBody		= json;
		this.requestContentType	= "application/json";
		this.addRequestHeader( "Content-Type", "application/json" );
		return this;
	}

	/**
	 * Set request body as XML (convenience method that also sets content type)
	 *
	 * @param xml The XML string
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange setRequestBodyXML( String xml ) {
		this.requestBody		= xml;
		this.requestContentType	= "application/xml";
		this.addRequestHeader( "Content-Type", "application/xml" );
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
		// Does nothing for now
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
		return new FileUpload[ 0 ];
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

	/**
	 * Initialize the web context for this mock exchange.
	 * This allows web-aware BIFs like getHTTPRequestData() to work properly.
	 *
	 * @param parentContext The parent context to attach this web context to
	 */
	public MockHTTPExchange initializeWebContext( ortus.boxlang.runtime.context.IBoxContext parentContext ) {
		this.context = new WebRequestBoxContext( runtime.getRuntimeContext(), this, this.webroot );
		parentContext.setParent( this.context );
		return this;
	}

	@Override
	public void reset() {
		this.context.clearBuffer();
	}

	/**
	 * Clear all request data (headers, cookies, form, URL params)
	 * Useful for resetting the mock between tests
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange clearRequestData() {
		this.mockRequestHeaders.clear();
		this.mockRequestCookies.clear();
		this.mockForm.clear();
		this.mockURL.clear();
		this.requestBody = null;
		this.attributes.clear();
		return this;
	}

	/**
	 * Clear all response data (headers, cookies, body, status)
	 * Useful for resetting the mock between tests
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange clearResponseData() {
		this.mockResponseHeaders.clear();
		this.mockResponseCookies.clear();
		this.responseStatus	= 200;
		this.responseText	= "Ok";
		this.writer			= new PrintWriter( new StringWriter() );
		return this;
	}

	/**
	 * Clear all mock data (request and response)
	 * Useful for resetting the mock between tests
	 *
	 * @return This exchange for chaining
	 */
	public IBoxHTTPExchange clearAll() {
		clearRequestData();
		clearResponseData();
		return this;
	}

	/**
	 * Execute a full life-cycle request using this exchange
	 * and return itself for inspection
	 */
	public MockHTTPExchange execute() {
		return execute( this.requestPath, this.requestMethod, this.mockRequestHeaders, this.requestBody );
	}

	/**
	 * Execute a full life-cycle request using this exchange
	 * and return itself for inspection
	 *
	 * @param path   The request path
	 * @param method The request method
	 *
	 * @return This exchange for inspection
	 */
	public MockHTTPExchange execute( String path, String method ) {
		return execute( path, method, this.mockRequestHeaders, this.requestBody );
	}

	/**
	 * Execute a full life-cycle request using this exchange
	 * and return itself for inspection
	 *
	 * @param path    The request path
	 * @param method  The request method
	 * @param headers The request headers
	 * @param body    The request body
	 *
	 * @return This exchange for inspection
	 */
	public MockHTTPExchange execute( String path, String method, IStruct headers, Object body ) {
		// Set up the request
		this.requestPath	= path;
		this.requestMethod	= method;
		if ( headers != null ) {
			this.addRequestHeaders( headers );
		}
		if ( body != null ) {
			this.requestBody = body;
		}
		// Use the WebRequestExecutor to process the request
		WebRequestExecutor.execute( this, this.webroot, true );
		return this;
	}
}
