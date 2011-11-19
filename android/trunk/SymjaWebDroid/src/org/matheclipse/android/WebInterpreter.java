package org.matheclipse.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;

/**
 * Evaluate a symja expression through the web interface
 * 
 */
public class WebInterpreter {

	String codeString;
	PrintStream outStream;

	private final HttpTransport transport = AndroidHttp.newCompatibleTransport();

	/**
	 * Create a new command interpreter attached to the passed in streams.
	 */
	public WebInterpreter(String codeString, OutputStream out) {
		this.codeString = codeString;
		if (out instanceof PrintStream) {
			this.outStream = (PrintStream) out;
		} else {
			this.outStream = new PrintStream(out);
		}
	}

	public String interpreter(final String strEval) {
		HttpRequestFactory rf = transport.createRequestFactory();
		GenericUrl url = new GenericUrl("http://mobmath.appspot.com/calc");
		url.put("evaluate", strEval);
		HttpRequest request;
		final StringBuffer buf = new StringBuffer();
		try {
			// final UrlEncodedContent content = new UrlEncodedContent(null);
			// request = rf.buildGetRequest(url);
			request = rf.buildPostRequest(url, ByteArrayContent.fromString("text/plain", ""));

			// new ByteArrayContent("plain/text", requestBody.getBytes("UTF-8")));

			// HttpHeaders headers = new HttpHeaders();
			// headers.setContentType("plain/text");
			// request.setHeaders(headers);
			HttpResponse shortUrl = request.execute();
			BufferedReader output = new BufferedReader(new InputStreamReader(shortUrl.getContent()));

			for (String line = output.readLine(); line != null; line = output.readLine()) {
				buf.append(line);
			}
			String resultStr = buf.toString();

			int index1 = resultStr.indexOf(';');
			if (index1 >= 0) {
				int index2 = resultStr.indexOf(';', index1 + 1);
				if (index2 >= 0) {
					String rType = resultStr.substring(index1 + 1, index2);
					if (rType.equals("error")) {
						resultStr = resultStr.substring(index2 + 1);
					} else {
						resultStr = resultStr.substring(index2 + 1);
					}
				}
			}
			return resultStr;

		} catch (UnsupportedEncodingException e) {
			printException(buf, e);
		} catch (IOException e) {
			printException(buf, e);
		}
		return buf.toString();
	}

	private void printException(final StringBuffer buf, final Throwable e) {
		String msg = e.getMessage();
		if (msg != null) {
			buf.append("\nError: " + msg);
		} else {
			buf.append("\nError: " + e.getClass().getSimpleName());
		}
	}

	public void eval() {
		String result = interpreter(codeString);
		outStream.println(result);
	}
}
