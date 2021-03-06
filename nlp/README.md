# NLP (Natural Language Processing) 
This directory contains all files required for configuring Josiah's natural language processing. While we are using api.ai, it contains JSON files that represent our entities and intentions. Ideally these could be extended to any other NLP API.

## Important
Throughout most of my development, I found that updating entities and intents in API.ai was faster. As such, most of the data in this directory is out of date, but I am leaving it in the repository as an example of how to update entities without interacting directly with API.ai. 

## To update api.ai's representation of your Entities
You can run `$ python3 update.py`
This will recursively search all directories under `./nlp` that end in `.json`. (TODO: Give update a configuration to change root directory)
Each .json file should contain only one Entity.
It will make a POST request to api.ai for each of these files. If the corresponding Entity already exists in api.ai's representation of our schema, any new keys in the `.json` file are added to the schema.
If no corresponding Entity exists, a new entity will be made.

TODO: Possibly provide this support for Intents
