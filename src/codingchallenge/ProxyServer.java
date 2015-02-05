package codingchallenge;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ProxyServer {

	// map to store hit count
	static Map<String, Integer> count = new HashMap<String, Integer>();
	static Map<String, String> response_data = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// get hit count for all endpoints from local storage
		count = (HashMap<String, Integer>) Storage.Deserialize("count.ser");

		// server bound to port 8080
		HttpServer server = HttpServer.create(new InetSocketAddress(
				Constants.PORT_NO), 0);

		// map to store context and its corresponding endpoint
		Set<String> context = Constants.CONTEXT_ENDPOINT.keySet();

		// handle get requests
		GetHandler gh = new GetHandler();
		for (String c : context) {
			server.createContext(c, gh);
		}

		// handle info request, which returns hit count
		server.createContext("/info", new InfoHandler());

		server.setExecutor(null);
		server.start();
		System.out.println("The server is running");
	}

	// http://localhost:8080/info
	static class InfoHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {

			StringBuilder response = new StringBuilder();

			// construct response
			response.append("<html><body>");
			response.append("<table border=1><tr><th>Endpoint</th><th>Hits</th></tr>");
			Set<String> context = Constants.CONTEXT_ENDPOINT.keySet();
			for (String c : context) {
				response.append("<tr>");
				response.append("<td>" + Constants.CONTEXT_ENDPOINT.get(c)
						+ "</td>");
				response.append("<td>" + count.get(c) + "</td>");
				response.append("</tr>");
			}
			response.append("</table></body></html>");
			// send response to client
			ProxyServer.writeResponse(httpExchange, response.toString());
		}
	}

	// handler for GET requests
	static class GetHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {

			String response = "";
			String context = httpExchange.getRequestURI().getPath();

			if (count == null || count.get(context) == null) {
				count.put(context, 1);
			} else {
				// increment count
				count.put(context, count.get(context) + 1);
			}

			Storage.Serialize("count.ser", count); // flush to storage

			// retrieve endpoint based on context
			String endpoint = Constants.CONTEXT_ENDPOINT.get(context);
			
			// construct query
			String apiurl = endpoint
					+ httpExchange.getRequestURI().getRawQuery();

			if (response_data == null || response_data.get(apiurl) == null) {
				// fetch response from source
				HttpConnection conn = new HttpConnection();
				try {
					// store response
					response = conn.sendGet(apiurl);
					response_data.put(apiurl, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// fetch cached response
				response = response_data.get(apiurl);
			}

			// send response to client
			ProxyServer.writeResponse(httpExchange, response);
		}
	}

	public static void writeResponse(HttpExchange httpExchange, String response)
			throws IOException {
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	/*
	 * public static Map<String, String> queryToMap(String query) {
	 * System.out.println("Query: " + query); Map<String, String> result = new
	 * HashMap<String, String>(); for (String param : query.split("&")) { String
	 * pair[] = param.split("="); if (pair.length > 1) { result.put(pair[0],
	 * pair[1]); } else { result.put(pair[0], ""); } } return result; }
	 */

}