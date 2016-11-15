/**
 *  OmniPro Unit
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
	definition (name: "OmniPro Unit", namespace: "excaliburpartners", author: "Ryan Wagoner") {
		capability "Switch"
		capability "Refresh"
		capability "Switch Level"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Off', action: "switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState: "on"
			state "on", label: 'On', action: "switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#79b821", nextState: "off"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}        
		controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 2, inactiveLabel: false, backgroundColor:"#ffe71e") {
			state "level", action:"switch level.setLevel"
		}
		valueTile("lValue", "device.level", inactiveLabel: true, height:1, width:1, decoration: "flat") {
			state "levelValue", label:'${currentValue}%', unit:"", backgroundColor: "#53a7c0"
		}

		main(["button"])
		details(["button","refresh","levelSliderControl","lValue"])
    }
}

import groovy.json.JsonBuilder

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute
	// TODO: handle 'level' attribute
}

def parseFromParent(data) {
	log.debug "Parsing from parent '${data}'"
	
	if (data.level == 0)
		sendEvent(name: "switch", value: "off")
	else
		sendEvent(name: "switch", value: "on")
		
	sendEvent(name: "level", value: data.level)
}

// handle commands
def on() {
	log.debug "Executing 'on'"
	sendEvent(name: "level", value: 100)
	sendEvent(name: "switch", value: "on")
	return buildSet(100)
}

def off() {
	log.debug "Executing 'off'"
	sendEvent(name: "level", value: 0)
	sendEvent(name: "switch", value: "off")
	return buildSet(0)
}

def setLevel(level) {
	log.debug "Executing 'setLevel($level)'"
	
	if (level < 0) 
		level = 0
	else if( level > 100) 
		level = 100

	if (level == 0) {
		sendEvent(name: "level", value: level)
		sendEvent(name: "switch", value: "off")
	} else {
		sendEvent(name: "switch", value: "on")
		sendEvent(name: "level", value: level)
	}
	
    return buildSet(level)
}

def refresh() {
	log.debug "Executing 'refresh'"
	def unitid = device.deviceNetworkId.split(":")[2]
	return parent.buildAction("GET", "/GetUnit?id=${unitid}", null)
}

def buildSet(level) {
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: level
	
	return parent.buildAction("POST", "/SetUnit", body)
}