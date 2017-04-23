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

### brownapi

### josiah

## TODO
For now, this server doesn't have any SSL certificates. I'm delaying this until we solidify with Brown's Networking team. It's possible / likely that this server will be routed to from an endpoint that has Brown's SSL certification.
