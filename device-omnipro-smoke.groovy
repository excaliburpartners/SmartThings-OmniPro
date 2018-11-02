/**
 *  OmniPro Smoke
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
	definition (name: "OmniPro Smoke", namespace: "excaliburpartners", author: "Ryan Wagoner") {
		capability "Smoke Detector"
		capability "Tamper Alert"
		capability "Sensor"
		capability "Refresh"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		standardTile("button", "device.smoke", width: 2, height: 2, canChangeIcon: true) {
			state "clear", label: 'clear', icon:"st.alarm.smoke.clear", backgroundColor:"#ffffff"
			state "detected", label: 'SMOKE', icon:"st.alarm.smoke.smoke", backgroundColor:"#e86d13"
		}
		standardTile("tamper", "device.tamper") {
			state "clear", label: 'clear', icon:"st.locks.lock.locked",   backgroundColor:"#ffffff"
			state "detected", label: 'TROUBLE', icon:"st.locks.lock.unlocked", backgroundColor:"#e86d13"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		} 

		main(["button"])
		details(["button","tamper","refresh"])
    }
}

import groovy.json.JsonBuilder

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'smoke' attribute
}

def parseFromParent(data) {
	log.debug "Parsing from parent '${data}'"
	
	if (data.status == "NOT READY")
		sendEvent(name: "smoke", value: "detected")
	else if (data.status == "TROUBLE")
		sendEvent(name: "tamper", value: "detected")
	else {
		sendEvent(name: "smoke", value: "clear")
		sendEvent(name: "tamper", value: "clear")
	}
}

// handle commands
def refresh() {
	log.debug "Executing 'refresh'"
	def unitid = device.deviceNetworkId.split(":")[2]
	return parent.buildAction("GET", "/GetZone?id=${unitid}", null)
}
