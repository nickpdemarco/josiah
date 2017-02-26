# Server
This server is responsible for responding to a webhook from api.ai, processing the JSON payload, and returning a string that should be spoken aloud to the user.

## WIP:
Run the server: `mvn package && ./run`
You can modify `test.json` to contain any user input you like.
`curl -X POST -d @test.json localhost:4567/echo --header "Content-Type:application/json"`
The application will return a response that will echo what was said to it.

## Working with API.ai code
API.ai's library appears to heavily rely on Gson's deserialization for processing requests and returning responses. To process JSON inputs, you must define a class that declares its fields with `@serialized('name_of_json_field')` annotations. It possible to omit this annotation but it is not recommended.
For example, the class `QuestionMetadata` in API.ai's library is defined as:
```java

public class QuestionMetadata implements Serializable {
	
    @SerializedName("timezone")
    private String timezone;

    @SerializedName("lang")
    private String language;

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("entities")
    private List<Entity> entities;

    @SerializedName("location")
    private Location location;

    // etc....
```

So if you were to run:

```java
QuestionMetadata qm = Gson.fromJson(STRING_OF_JSON, QuestionMetadata.class);
```
Then `qm` would be an object that had its fields `timezone`, `lang`, etc, all filled in with the cooresponding contents of the Json string. Note that the Json string can contain more fields than are "requested" by the class. These will be silently ignored by Gson.

`QuestionMetadata` is the superclass to `AIRequest` and `AIResponse`, all provided by API.ai's library. I extend the latter two to create `JosiahAIRequest` and `JosiahAIResponse`. Currently, the both add fields ("result" and "fulfillment", respectively) to demonstrate how the elective serialization pattern works. Note that as subclasses of `QuestionMetaData` and one of `AIRequest` and `AIResponse`, my implementations also have fields like `timezone`, `entities`, etc.


## TODO:
For now, this server doesn't have any SSL certificates. I'm delaying this until we solidify with Brown's Networking team. It's possible / likely that this server will be routed to from an endpoint that has Brown's SSL certification.
