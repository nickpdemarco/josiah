#!/usr/local/bin/python

import requests
import json
import csv
import os.path
import sys
import re

BROWN_API_CREDENTIAL_PATH = "../secure/officeApiCredentials"
LISTING_URL = "https://esb.brown.edu/services/cis/faculty-lookup-api/v1/faculty/listing"
SHARED_HEADERS = {"Content-Type" : "application/json"} 

def main():

	if len(sys.argv) != 2:
		print "Usage: python makeProfEntity.py <json output file>"
		return
	
	if os.path.exists(sys.argv[1]):
		print "ERROR: File exists. Please delete if the entity is out of date."
		return

	with open(sys.argv[1], 'w') as outfile:
		json.dump(getEntityObject(), outfile, sort_keys=True, indent=4, separators=(',', ': '))

def getEntityObject():
	entity = {}
	entity["name"] = "professor"
	entries = []

	data = getListingRawData()
	# skip first and last - 
	# first is the CSV header, the last is a test user.
	data = data.split("\n")[1:-2]
	for row in data:
		# We get some commas that are escaped. Ignore those, i.e "Thomas Jones, Jr."
		col = re.split("(?<!\\\\),", row)
		brown_id = col[0]
		# Replace any backslashes preserved by the above step.
		last_name = col[1].replace("\\", "")
		first_name = col[2].replace("\\", "")

		entry = {"value" : brown_id}
		entry["synonyms"] = [first_name + " " + last_name, 
												 "Professor " + last_name
												 ]
		entries.append(entry)

	entity["entries"] = entries
	
	return entity


def getListingRawData():
	# Credentials in the format "user:pass"
	credFile = open(BROWN_API_CREDENTIAL_PATH)
	credentials = credFile.read().split(':')
	user = credentials[0]
	password = credentials[1].rstrip()
	credFile.close();

	sesh = requests.Session()
	sesh.auth = (user, password)

	r = sesh.get(LISTING_URL, headers=SHARED_HEADERS)
	return r.text



if __name__ == "__main__":
	main()
