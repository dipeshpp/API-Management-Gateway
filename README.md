# API-Management-Gateway-

DESIGN AND IMPLEMENTATION
I've designed a minimalistic API gateway (using native Java libraries) that incorporates the following features:

1. Extensibility: It is compatible with any API that supports GET requests and that doesn't require authentication. This can be done by simply adding the endpoint and context mapping to the Constants class.

Example: mapping.put("/wiki", "http://en.wikipedia.org/w/api.php?");

The context is a unique string for every endpoint. I chose not to send the endpoint as part of the GET request to minimize complexity at the client side and for ease of testing. The GET parameters are appended to the endpoint as is the norm. 

2. Analytics: The info context returns the hit count for each endpoint presented as an html table.  
Usage: http://localhost:8080/info

3. Caching: A basic implementation wherein the data retrieved from the actual source is cached and served from memory for the session. 


RUNNING
The source code can be executed directly and doesn't require any command-line parameters.
ProxyServer.java contains the main function.
The HTTP server runs on port 8080 by default. This can be changed in the Constants class.
The Wikipedia endpoint is preconfigured as a constant.

The following example returns the HTML representation of the JSON format of the Main Page:
http://localhost:8000/wiki?format=jsonfm&action=query&titles=Main%20Page&prop=revisions&rvprop=content

Source: http://en.wikipedia.org/w/api.php?format=jsonfm&action=query&titles=Main%20Page&prop=revisions&rvprop=content


TESTING
I ensured data integrity by comparing output from the source to the output returned by the proxy server.

WIKIPEDIA API REFERENCES:
https://www.mediawiki.org/wiki/API:Main_page
http://en.wikipedia.org/w/api.php?

