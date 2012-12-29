package org.matheclipse.android.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.SyntaxError;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;

/**
 * Evaluate a Symja expression through the web interface
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
		GenericUrl url = new GenericUrl("http://symjaweb.appspot.com/calc");
		url.put("evaluate", strEval);
		HttpRequest request;
		final StringBuffer buf = new StringBuffer();
		BufferedReader output = null;
		try {
			// final UrlEncodedContent content = new UrlEncodedContent(null);
			// request = rf.buildGetRequest(url);
			request = rf.buildPostRequest(url, ByteArrayContent.fromString("text/plain", ""));

			// new ByteArrayContent("plain/text", requestBody.getBytes("UTF-8")));

			// HttpHeaders headers = new HttpHeaders();
			// headers.setContentType("plain/text");
			// request.setHeaders(headers);
			HttpResponse shortUrl = request.execute();
			output = new BufferedReader(new InputStreamReader(shortUrl.getContent()));

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
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					printException(buf, e);
				}
			}
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

	public void eval(final String function) {
		String evalStr = codeString;
		try {
			Parser p = new Parser(true);
			// throws SyntaxError exception, if syntax isn't valid
			if (function != null) {
				evalStr = function + "(" + codeString + ")";
				p.parse(evalStr);
			} else {
				p.parse(evalStr);
			}
		} catch (SyntaxError e1) {
			try {
				Parser p = new Parser();
				// throws SyntaxError exception, if syntax isn't valid
				if (function != null) {
					evalStr = function + "[" + codeString + "]";
					p.parse(evalStr);
				} else {
					p.parse(evalStr);
				}
			} catch (Exception e2) {
				outStream.println(e2.getMessage());
				return;
			}
		} catch (Exception e) {
			outStream.println(e.getMessage());
			return;
		}
		String result = "";
		result = interpreter(evalStr);
		outStream.print(result);
	}
}
