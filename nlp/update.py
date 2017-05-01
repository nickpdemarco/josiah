#!/usr/local/bin/python

import requests
import os
import fnmatch
import json

# Local config
API_AI_DEV_KEY_PATH = "../secure/apiaiDeveloperAccessToken"
ENTITIES_PATH = "./entities"
INTENTS_PATH = "./intents"

# api.ai Http
API_AI_ROOT_URL = "https://api.api.ai/v1/"
API_AI_ENTITIES_URL = "entities/"
API_AI_INTENTS_URL = "intents/"

SHARED_HEADERS = {"Content-Type" : "application/json"} # Note the addition of the Authorization field in main()
apiaiDevKey = ""


def main() :
	try:

		apiaiDevKeyFile = open(API_AI_DEV_KEY_PATH)
		apiaiDevKey = apiaiDevKeyFile.readlines()[0].rstrip()
		SHARED_HEADERS["Authorization"] = "Bearer " + apiaiDevKey
		apiaiDevKeyFile.close()	

	except FileNotFoundError:
		print("ERROR: This script can't find a developer key at the specified path. Is there an api.ai Developer key at " + API_AI_DEV_KEY_PATH + " ?")
		return -1

	updateAllEntities()
	
def allJsonFilesRecursively(path) :
	matches = []
	for root, dirnames, filenames in os.walk(path):	
		for filename in fnmatch.filter(filenames, '*.json'):
			matches.append(os.path.join(root, filename))
	return matches

def updateAllEntities() :
	# find all directories containing json in the entity constant path. Call updateEntity for each.
	for jsonEntityPath in allJsonFilesRecursively(ENTITIES_PATH) :
		entityName, _ = os.path.splitext(os.path.basename(jsonEntityPath))
		with open(jsonEntityPath, 'r') as jsonEntityFile:
			content = jsonEntityFile.read()

			if shouldCreateNewEntity(entityName) :
				r = requests.post(API_AI_ROOT_URL + API_AI_ENTITIES_URL, data = content, headers=SHARED_HEADERS)
			else :
				content = getEntriesFromFile(content)
				r = requests.put(API_AI_ROOT_URL + API_AI_ENTITIES_URL + entityName + "/entries", data = content, headers=SHARED_HEADERS)
				
			try :
				if not jsonError(r.json()) :
					print("Success updating " + entityName)
				else:
					print("Some error occurred with " + entityName)
					print r.json()
			except json.decoder.JSONDecodeError:
				print(r.text)


# Our json files contain names as well as entries - we want to sometimes 
# strip the entries for put requests (silent-on-key-conflict additions)	
def getEntriesFromFile(content) :
	try :
		parsed = json.loads(content)
		stripped = json.dumps(parsed["entries"])
		return stripped
	except KeyError:
		return content # do nothing


def updateAllIntents() :
	for jsonEntityPath in allJsonFilesRecursively(INTENTS_PATH) :
		print(jsonEntity)


def shouldCreateNewEntity(entityName):
	r = requests.get(API_AI_ROOT_URL + API_AI_ENTITIES_URL + entityName, headers=SHARED_HEADERS)
	return shouldCreateNew(r.json())
	
	
# TODO for some reason we're getting an internal server error on this.
# Putting this off because there's no real need for auomated intent updating yet.
"""
def shouldCreateNewIntent(intentName):
	r = requests.get(API_AI_ROOT_URL + API_AI_INTENTS_URL + intentName, headers=SHARED_HEADERS)
	print(r.text)
	return shouldCreateNew(r.json())
"""

def shouldCreateNew(jsonResponse) :
	err = jsonError(jsonResponse)
	if not err : # if there's no error
		return False # then we shouldn't make a new entity
	return True # otherwise the entity must exist.

def jsonError(json) :
	try:
		return json["status"]["errorType"] + " ::: " + json["status"]["errorDetails"]
	except KeyError:
		return None # There's no error in this json.

if __name__ == "__main__" : 
	main()
