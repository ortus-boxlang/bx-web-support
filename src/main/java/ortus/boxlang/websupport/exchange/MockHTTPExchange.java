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
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import ortus.boxlang.web.context.WebRequestBoxContext;
import ortus.boxlang.web.exchange.BoxCookie;
import ortus.boxlang.web.exchange.IBoxHTTPExchange;

/**
 * A Testing class to test the HTTPExchange. Great for mocking, testing, and extending.
 */
public class MockHTTPExchange implements IBoxHTTPExchange {

	@Override
	public void addResponseCookie( BoxCookie arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'addResponseCookie'" );
	}

	@Override
	public void addResponseHeader( String arg0, String arg1 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'addResponseHeader'" );
	}

	@Override
	public void flushResponseBuffer() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'flushResponseBuffer'" );
	}

	@Override
	public void forward( String arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'forward'" );
	}

	@Override
	public Object getRequestAttribute( String arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestAttribute'" );
	}

	@Override
	public Map<String, Object> getRequestAttributeMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestAttributeMap'" );
	}

	@Override
	public String getRequestAuthType() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestAuthType'" );
	}

	@Override
	public Object getRequestBody() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestBody'" );
	}

	@Override
	public String getRequestCharacterEncoding() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestCharacterEncoding'" );
	}

	@Override
	public long getRequestContentLength() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestContentLength'" );
	}

	@Override
	public String getRequestContentType() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestContentType'" );
	}

	@Override
	public String getRequestContextPath() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestContextPath'" );
	}

	@Override
	public BoxCookie getRequestCookie( String arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestCookie'" );
	}

	@Override
	public BoxCookie[] getRequestCookies() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestCookies'" );
	}

	@Override
	public Map<String, String[]> getRequestFormMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestFormMap'" );
	}

	@Override
	public String getRequestHeader( String arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestHeader'" );
	}

	@Override
	public Map<String, String[]> getRequestHeaderMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestHeaderMap'" );
	}

	@Override
	public String getRequestLocalAddr() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestLocalAddr'" );
	}

	@Override
	public String getRequestLocalName() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestLocalName'" );
	}

	@Override
	public int getRequestLocalPort() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestLocalPort'" );
	}

	@Override
	public Locale getRequestLocale() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestLocale'" );
	}

	@Override
	public Enumeration<Locale> getRequestLocales() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestLocales'" );
	}

	@Override
	public String getRequestMethod() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestMethod'" );
	}

	@Override
	public String getRequestPathInfo() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestPathInfo'" );
	}

	@Override
	public String getRequestPathTranslated() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestPathTranslated'" );
	}

	@Override
	public String getRequestProtocol() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestProtocol'" );
	}

	@Override
	public String getRequestQueryString() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestQueryString'" );
	}

	@Override
	public String getRequestRemoteAddr() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestRemoteAddr'" );
	}

	@Override
	public String getRequestRemoteHost() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestRemoteHost'" );
	}

	@Override
	public int getRequestRemotePort() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestRemotePort'" );
	}

	@Override
	public String getRequestRemoteUser() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestRemoteUser'" );
	}

	@Override
	public String getRequestScheme() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestScheme'" );
	}

	@Override
	public String getRequestServerName() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestServerName'" );
	}

	@Override
	public int getRequestServerPort() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestServerPort'" );
	}

	@Override
	public String getRequestURI() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestURI'" );
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestURL'" );
	}

	@Override
	public Map<String, String[]> getRequestURLMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestURLMap'" );
	}

	@Override
	public Principal getRequestUserPrincipal() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getRequestUserPrincipal'" );
	}

	@Override
	public String getResponseHeader( String arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getResponseHeader'" );
	}

	@Override
	public Map<String, String[]> getResponseHeaderMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getResponseHeaderMap'" );
	}

	@Override
	public int getResponseStatus() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getResponseStatus'" );
	}

	@Override
	public PrintWriter getResponseWriter() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getResponseWriter'" );
	}

	@Override
	public FileUpload[] getUploadData() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getUploadData'" );
	}

	@Override
	public WebRequestBoxContext getWebContext() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'getWebContext'" );
	}

	@Override
	public boolean isRequestSecure() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'isRequestSecure'" );
	}

	@Override
	public boolean isResponseStarted() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'isResponseStarted'" );
	}

	@Override
	public void removeRequestAttribute( String arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'removeRequestAttribute'" );
	}

	@Override
	public void resetResponseBuffer() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'resetResponseBuffer'" );
	}

	@Override
	public void sendResponseBinary( byte[] arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'sendResponseBinary'" );
	}

	@Override
	public void sendResponseFile( File arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'sendResponseFile'" );
	}

	@Override
	public void setRequestAttribute( String arg0, Object arg1 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'setRequestAttribute'" );
	}

	@Override
	public void setResponseHeader( String arg0, String arg1 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'setResponseHeader'" );
	}

	@Override
	public void setResponseStatus( int arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'setResponseStatus'" );
	}

	@Override
	public void setResponseStatus( int arg0, String arg1 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'setResponseStatus'" );
	}

	@Override
	public void setWebContext( WebRequestBoxContext arg0 ) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException( "Unimplemented method 'setWebContext'" );
	}

}
