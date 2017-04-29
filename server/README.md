# Server
This server is responsible for responding to a webhook from api.ai, processing the JSON payload, and returning a string that should be spoken aloud to the user.

## Simple Test
Run the server: `mvn package && ./run`
You can modify `test.json` to contain any user input you like.
`curl -X POST -d @test.json localhost:4567/echo --header "Content-Type:application/json"`
The application will return a response that will echo what was said to it.

## Server Architecture
Generally speaking, the packages in this server are divided into two categories:
 * `edu.brown.cs.ndemarco.brownapi.*` : This package prefix refers to implementations of clients for various APIs provided by Brown. It knows nothing about Josiah functionality. 
 * `edu.brown.cs.ndemarco.josiah.*` : Often using `brownapi`, the `josiah` package prefix refers to Josiah specific functionality.

### josiah
First, lets define a few terms.

Term | Meaning
------------ | -------------
Personal Assistant Device (PAD) | Google Home, Amazon Echo, etc.
Client Query | The spoken request made by a user to a PAD. 
AI Request | A PAD's request made to API.ai.
AI Response | API.ai's interpretation of the request. Intent is determined, with entities optionally identified.
Josiah Query | Given an AI Response, a Josiah server attempts to create a Josiah Fulfillment by querying relevant Brown APIs.
Josiah Fulfillment | An object sent back to API.ai containing a Speech Response and optionally, metadata about the request.
Speech Response | The text that a PAD should speak in response to a user's request.

When first integrating API.ai's support code, I found that a few confusions came up regarding naming. For example, API.ai's library provides an object, `AIResponse`, that contains metadata about some request to API.ai's service, as described above. But, from the perspective of a Josiah server, this is actually a *query*, not a response. So, I've created a simple subclass of `AIResponse`, `JosiahQuery`.

`JosiahFulfillment`, on the other hand, is a straightforward subclass of API.ai's `Fulfillment`, with some static utility methods for constructing `Fulfillment` objects that only have their `SpeechResponse` field set. At the time of this writing, more complex `JosiahFulfillment` objects are not needed, though in the future they may contain metadata that instructs API.ai to gather more specific information through follow-up questions to the user.

We now address the issue of creating packages under the `edu.brown.cs.ndemarco.josiah` prefix, responsible for taking the results of a query to a Brown API (the querying itself is delegated to the `edu.brown.cs.ndemarco.brownapi` packages), and formatting the results into reasonable human speech. We create an interface for objects that perform such transformations:

```java
public interface QueryProcessor {
	JosiahFulfillment process(JosiahQuery query);	
	boolean isResponsibleFor(String intent);
}
```

Note that a `QueryProcessor` can be responsible for more than one intent name.

We can then create "subpackages" that are generally responsible for single intents or a collection of related intents. At the time of this writing, these subpackages are grouped by the Brown API that they rely on. For example, `edu.brown.cs.ndemarco.josiah.dining` relies on the API wrapper created in `edu.brown.cs.ndemarco.brownapi.dining`. 

### brownapi
There is considerably less similarity between subpackages within `brownapi`. Each package is responsible for parsing the response from an API provided by Brown, which bear little similarity to each other. 
