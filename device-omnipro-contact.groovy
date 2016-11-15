/**
 *  OmniPro Contact
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
	definition (name: "OmniPro Contact", namespace: "excaliburpartners", author: "Ryan Wagoner") {
		capability "Contact Sensor"
		capability "Refresh"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		standardTile("button", "device.contact", width: 2, height: 2, canChangeIcon: true) {
			state "closed", label: 'closed', icon:"st.contact.contact.closed", backgroundColor:"#79b821"
			state "open", label: 'open', icon:"st.contact.contact.open", backgroundColor:"#ffa81e"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		} 

		main(["button"])
		details(["button","refresh"])
    }
}

import groovy.json.JsonBuilder

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'contact' attribute
}

def parseFromParent(data) {
	log.debug "Parsing from parent '${data}'"
	
	if (data.status == "NOT READY")
		sendEvent(name: "contact", value: "open")
	else
		sendEvent(name: "contact", value: "closed")
}

// handle commands
def refresh() {
	log.debug "Executing 'refresh'"
	def unitid = device.deviceNetworkId.split(":")[2]
	return parent.buildAction("GET", "/GetZone?id=${unitid}", null)
}