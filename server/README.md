# Server
This server is responsible for responding to a webhook from api.ai, processing the JSON payload, and returning a string that should be spoken aloud to the user.

## WIP:
Run the server: `mvn package && ./run`
On another terminal : `curl -X POST localhost:4567/hello`
