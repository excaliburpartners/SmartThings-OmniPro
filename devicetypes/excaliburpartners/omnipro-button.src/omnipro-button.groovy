/**
 *  OmniPro Button
 *
 *  Copyright 2016 Ryan Wagoner
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "OmniPro Button", namespace: "excaliburpartners", author: "Ryan Wagoner") {
		capability "Momentary"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Push', action: "momentary.push", backgroundColor:"#ffffff"
		}

		main(["button"])
		details(["button"])
    }
}

import groovy.json.JsonBuilder

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def parseFromParent(data) {
	log.debug "Parsing from parent '${data}'"
}

// handle commands
def push() {
	log.debug "Executing 'push'"
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid
	
	return parent.buildAction("POST", "/PushButton", body)
}