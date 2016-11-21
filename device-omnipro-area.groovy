/**
 *  OmniPro Area
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
	definition (name: "OmniPro Area", namespace: "excaliburpartners", author: "Ryan Wagoner") {
		capability "Contact Sensor"
		capability "Smoke Detector"
		capability "Carbon Monoxide Detector"
		capability "Water Sensor"
		capability "Refresh"
		
		attribute "mode", "string"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
	
		standardTile("button", "device.mode", width: 3, height: 2, canChangeIcon: true) {
			state "off", label: 'off', backgroundColor:"#79b821"
			state "day", label: 'day', backgroundColor:"#ffa81e"
			state "night", label: 'night', backgroundColor:"#ffa81e"
			state "away", label: 'away', backgroundColor:"#ffa81e"
			state "vacation", label: 'vacation', backgroundColor:"#ffa81e"
		}	
		standardTile("burglary", "device.contact") {
			state "closed", label: 'OK', icon:"st.contact.contact.closed", backgroundColor:"#79b821"
			state "open", label: 'BURGLARY', icon:"st.contact.contact.open", backgroundColor:"#ffa81e"
		}		
		standardTile("fire", "device.smoke") {
			state "clear", label: 'OK', icon:"st.alarm.smoke.clear", backgroundColor:"#ffffff"
			state "detected", label: 'FIRE', icon:"st.alarm.smoke.smoke", backgroundColor:"#e86d13"
		}	
		standardTile("co", "device.carbonMonoxide") {
			state "clear", label: 'OK', icon: "st.alarm.carbon-monoxide.clear", backgroundColor: "#ffffff"
			state "detected", label: 'MONOXIDE', icon: "st.alarm.carbon-monoxide.carbon-monoxide", backgroundColor:"#e86d13"
		}		
		standardTile("water", "device.water") {
			state "dry", icon:"st.alarm.water.dry", backgroundColor:"#ffffff"
			state "wet", icon:"st.alarm.water.wet", backgroundColor:"#53a7c0"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		} 

		main(["button"])
		details(["button","burglary","fire","co","water","refresh"])
    }
}

import groovy.json.JsonBuilder

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'contact' attribute
	// TODO: handle 'smoke' attribute
}

def parseFromParent(data) {
	log.debug "Parsing from parent '${data}'"
	
	sendEvent(name: "mode", value: data.mode.toLowerCase())
	
	if (data.id == 1) {
		if (data.mode == "NIGHT")
			parent.setLocationMode("Night")
		else if (data.mode == "AWAY")
			parent.setLocationMode("Away")
		else if (data.mode == "VACATION")
			parent.setLocationMode("Vacation")
		else
			parent.setLocationMode("Home")
	}
	
	if (data.burglary == "ALARM")
		sendEvent(name: "contact", value: "open")
	else
		sendEvent(name: "contact", value: "closed")
		
	if (data.co == "ALARM")
		sendEvent(name: "carbonMonoxide", value: "detected")
	else
		sendEvent(name: "carbonMonoxide", value: "clear")
		
	if (data.fire == "ALARM")
		sendEvent(name: "smoke", value: "detected")
	else
		sendEvent(name: "smoke", value: "clear")
		
	if (data.water == "ALARM")
		sendEvent(name: "water", value: "wet")
	else
		sendEvent(name: "water", value: "dry")
}

// handle commands
def refresh() {
	log.debug "Executing 'refresh'"
	def unitid = device.deviceNetworkId.split(":")[2]
	return parent.buildAction("GET", "/GetArea?id=${unitid}", null)
}