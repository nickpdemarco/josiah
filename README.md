# josiah
Josiah is Brown University's personal assistant. Aimed to be an cohesive extension to established products like Google Home and Amazon Echo. If you don't have a personal device, you can still play with the API.ai bot at [https://bot.api.ai/josiah](https://bot.api.ai/josiah). 

## Architecture

A summary of the architecture of the Josiah pipeline is shown below.

![Architecture of Josiah pipeline](/img/josiah_architecture.png)

The Java server stage in the pipeline is stored entirely in the `server` directory of this repository. Note that there is a separate README in that directory that discusses server implementation in greater detail. 

Let's dig a bit deeper into each of these stages.

### User to Device

For the purpose of simplicity, let's blackbox the process of a user sending input to their device. There's such a great amount of variability in how input could be given. We'll see in the next section that, as long as a device follows the JSON protocol established by API.ai, there's hardly a limit on the ways in which we could receive input.

### Device to API.ai

While developing this product, I was testing and debugging with a Google Home. Fortunately, API.ai has a default integration for Google Home devices, listed [here](https://docs.api.ai/docs/integrations). If your desired device does not have a pre-built integration, you can also send a POST request to the `/query` endpoint for the Josiah API.ai service. I've intentionally restricted that access, but you can message me for credentials and instructions on how to proceed.

### API.ai to Java server

API.ai sends a JSON message in a format specified by the API.ai [docs](https://docs.api.ai/docs/webhook). A common message might look like (note that sensitive fields have been redacted):

```
{
  "id": "REDACTED",
  "timestamp": "2017-04-29T21:46:55.146Z",
  "lang": "en",
  "result": {
    "source": "agent",
    "resolvedQuery": "breakfast",
    "speech": "",
    "action": "",
    "actionIncomplete": false,
    "parameters": {
      "date": "2017-04-30",
      "meal": "Breakfast",
      "building": "Sharpe Refactory"
    },
    "contexts": [],
    "metadata": {
      "intentId": "REDACTED",
      "webhookUsed": "true",
      "webhookForSlotFillingUsed": "false",
      "intentName": "menu"
    },
    "fulfillment": {
      "speech": "",
      "messages": [
        {
          "type": 0,
          "speech": ""
        }
      ]
    },
    "score": 1.0
  },
  "status": {
    "code": 200,
    "errorType": "success"
  },
  "sessionId": "REDACTED"
}
```

Let's zoom in on the most pertinent data for the Java server:

```
"parameters": {
  "date": "2017-04-30",
  "meal": "Breakfast",
  "building": "Sharpe Refactory"
},
"metadata": {
  "intentId": "REDACTED",
  "intentName": "menu"
},
    
```

Here, we conveniently get several fields that will ultimately be passed to a request to Brown's dining API. Note that the intentName and id are both sent, so the server can choose either to determine which handler should receive the request.

The Java server will ultimately forward this request to one of several `QueryProcessor`s, discussed in greater detail in the `/server` README. 

### Java server to Brown APIs & back again

While the work done in the previous step is entirely handled by the `josiah` package, this step is entirely abstracted to the `brownapi` package (both discussed in greater detail in the `server` README). It is my goal to ultimately publish this Java package for interfacing with Brown APIs, once CIS establishes a formal method for distributing API keys.

This step simply sends web requests to RESTful APIs hosted by Brown or one of its third-party service providers. The response is parsed into meaningful POJOs (plain old Java objects) that are then used by the `josiah` package.

The `josiah` package manages the process of parsing the response into natural speech. Right now, this is not done particularly well. It simply fills out a pre-scripted response for requests. This could easily be improved by choosing between several form responses, so as not to become robotically dull. 

### Java server to API.ai & API.ai to device

These steps are mentioned together due to their minimal differences. The Java server sends a natural-language response to API.ai, which is in turn forwarded to the user. In the future, this step could be complicated and diversified with conversational contexts - dialogues between device and user that require a back and forth, as opposed to a single-request-single-response model. However, for now, no query has been sufficiently complicated to warrant such a model.


## Dependencies
I've listed any dependencies I had to install to get any and all code in this repo up and running.

### Java / Maven
See the `pom.xml` file in `./server` for any dependencies used for the server.

### Python
(Used for updating API.ai intents and entities from the command line. Not required.)
Requests : http://docs.python-requests.org/en/master/
`pip install requests`
