/**
 *  OmniPro Thermostat
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
	definition (name: "OmniPro Thermostat", namespace: "excaliburpartners", author: "Ryan Wagoner") {
		capability "Temperature Measurement"
		capability "Thermostat"
		capability "Relative Humidity Measurement"
		capability "Refresh"
		
		command "heatLevelUp"
        command "heatLevelDown"
        command "coolLevelUp"
        command "coolLevelDown"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		valueTile("temperature", "device.temperature", width: 2, height: 2) {
			state("temperature", label:'${currentValue}°', unit:'F',
				backgroundColors:[
					[value: 31, color: "#153591"],
					[value: 44, color: "#1e9cbb"],
					[value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 95, color: "#d04e00"],
					[value: 96, color: "#bc2323"]
				]
			)
		}
		standardTile("mode", "device.thermostatMode", inactiveLabel: false, decoration: "flat") {
			state "off", label:'', action:"thermostat.heat", icon:"st.thermostat.heating-cooling-off", nextState: "heat"
			state "heat", label:'', action:"thermostat.emergencyHeat", icon:"st.thermostat.heat", nextState: "emergency heat"
			state "emergency heat", label:'', action:"thermostat.cool", icon:"st.thermostat.emergency-heat", nextState: "cool"
			state "cool", label:'', action:"thermostat.auto", icon:"st.thermostat.cool", nextState: "auto"
			state "auto", label:'', action:"thermostat.off", icon:"st.thermostat.auto", nextState: "off"
		}
		standardTile("fanMode", "device.thermostatFanMode", inactiveLabel: false, decoration: "flat") {
			state "auto", label:'', action:"thermostat.fanOn", icon:"st.thermostat.fan-auto", nextState: "on"
			state "on", label:'', action:"thermostat.fanCirculate", icon:"st.thermostat.fan-on", nextState: "circulate"
			state "circulate", label:'  ', action:"thermostat.fanAuto", icon:"st.thermostat.fan-circulate", nextState: "auto"
		}
		valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "heat", label:'${currentValue}° heat', unit:"F", backgroundColor:"#ffffff"
		}
		valueTile("coolingSetpoint", "device.coolingSetpoint", inactiveLabel: false, decoration: "flat") {
			state "cool", label:'${currentValue}° cool', unit:"F", backgroundColor:"#ffffff"
		}
		standardTile("refresh", "device.thermostatMode", inactiveLabel: false, decoration: "flat") {
			state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		standardTile("heatLevelUp", "device.heatingSetpoint", canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "default", label:'  ', action:"heatLevelUp", icon:"st.thermostat.thermostat-up"
		}
		standardTile("heatLevelDown", "device.heatingSetpoint", canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "default", label:'  ', action:"heatLevelDown", icon:"st.thermostat.thermostat-down"
		}
		standardTile("coolLevelUp", "device.heatingSetpoint", canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "default", label:'  ', action:"coolLevelUp", icon:"st.thermostat.thermostat-up"
		}
		standardTile("coolLevelDown", "device.heatingSetpoint", canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "default", label:'  ', action:"coolLevelDown", icon:"st.thermostat.thermostat-down"
		}
		valueTile("relativeHumidityMeasurement", "device.relativeHumidityMeasurement", inactiveLabel: false, decoration: "flat") {
			state "humidity", label:'${currentValue}% humidity', unit:"", backgroundColor:"#ffffff"
		}
		main "temperature"
		details(["temperature", "mode", "fanMode", "heatLevelDown", "heatingSetpoint", "heatLevelUp", "coolLevelDown", "coolingSetpoint", "coolLevelUp", "refresh", "relativeHumidityMeasurement"])
	}
}

import groovy.json.JsonBuilder

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'temperature' attribute
	// TODO: handle 'coolingSetpoint' attribute
	// TODO: handle 'thermostatFanMode' attribute
	// TODO: handle 'heatingSetpoint' attribute
	// TODO: handle 'thermostatMode' attribute
	// TODO: handle 'thermostatOperatingState' attribute
	// TODO: handle 'relativeHumidityMeasurement' attribute
}

def parseFromParent(data) {
	log.debug "Parsing from parent '${data}'"
	
	switch (data.mode) {
		case "0":        
			sendEvent(name: "thermostatMode", value: "off", displayed: false)
			break	
		case "1":
			sendEvent(name: "thermostatMode", value: "heat", displayed: false)
			break	
		case "2":
			sendEvent(name: "thermostatMode", value: "cool", displayed: false)
			break	
		case "3":
			sendEvent(name: "thermostatMode", value: "auto", displayed: false)
			break	
		case "4":
			sendEvent(name: "thermostatMode", value: "emergency heat", displayed: false)
			break	
    }
	
	switch (data.fanmode) {
		case "0":        
			sendEvent(name: "thermostatFanMode", value: "auto", displayed: false)
			break	
		case "1":
			sendEvent(name: "thermostatFanMode", value: "on", displayed: false)
			break	
		case "2":
			sendEvent(name: "thermostatFanMode", value: "circulate", displayed: false)
			break	
    }
	
	if (data.status == "HEATING")
		sendEvent(name: "thermostatOperatingState", value: "heating", displayed: false)
	else if (data.status == "COOLING")
		sendEvent(name: "thermostatOperatingState", value: "cooling", displayed: false)
	else
		sendEvent(name: "thermostatOperatingState", value: "idle", displayed: false)
	
	sendEvent(name: "temperature", value: data.temp, displayed: false)
	sendEvent(name: "coolingSetpoint", value: data.coolsetpoint, displayed: false)
	sendEvent(name: "heatingSetpoint", value: data.heatsetpoint, displayed: false)
	
	sendEvent(name: "relativeHumidityMeasurement", value: data.humidity, displayed: false)
}

// handle commands

def coolLevelUp(){
    int nextLevel = device.currentValue("coolingSetpoint") + 1
    
    if( nextLevel > 99){
    	nextLevel = 99
    }
    log.debug "Setting cool set point up to: ${nextLevel}"
    setCoolingSetpoint(nextLevel)
}

def coolLevelDown(){
    int nextLevel = device.currentValue("coolingSetpoint") - 1
    
    if( nextLevel < 50){
    	nextLevel = 50
    }
    log.debug "Setting cool set point down to: ${nextLevel}"
    setCoolingSetpoint(nextLevel)
}

def heatLevelUp(){
    int nextLevel = device.currentValue("heatingSetpoint") + 1
    
    if( nextLevel > 90){
    	nextLevel = 90
    }
    log.debug "Setting heat set point up to: ${nextLevel}"
    setHeatingSetpoint(nextLevel)
}

def heatLevelDown(){
    int nextLevel = device.currentValue("heatingSetpoint") - 1
    
    if( nextLevel < 40){
    	nextLevel = 40
    }
    log.debug "Setting heat set point down to: ${nextLevel}"
    setHeatingSetpoint(nextLevel)
}

def setCoolingSetpoint(tempCool) {
	log.debug "Executing 'setCoolingSetpoint'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: tempCool
	
	sendEvent(name: "coolingSetpoint", value: tempCool)
	return parent.buildAction("POST", "/SetThermostatCoolSetpoint", body)
}

def setHeatingSetpoint(tempHeat) {
	log.debug "Executing 'setHeatingSetpoint'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: tempHeat
	
	sendEvent(name: "heatingSetpoint", value: tempHeat)
	return parent.buildAction("POST", "/SetThermostatHeatSetpoint", body)
}

def fanOn() {
	log.debug "Executing 'fanOn'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: 1
	
	sendEvent(name: "thermostatFanMode", value: "on")
	return parent.buildAction("POST", "/SetThermostatFanMode", body)
}

def fanAuto() {
	log.debug "Executing 'fanAuto'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: 0
	
	sendEvent(name: "thermostatFanMode", value: "auto")
	return parent.buildAction("POST", "/SetThermostatFanMode", body)
}

def fanCirculate() {
	log.debug "Executing 'fanCirculate'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: 2
	
	sendEvent(name: "thermostatFanMode", value: "circulate")
	return parent.buildAction("POST", "/SetThermostatFanMode", body)
}

def setThermostatFanMode(fanMode) {
	log.debug "Executing 'setThermostatFanMode'"
	
    switch (fanMode) {
		case "auto":        return fanAuto()
		case "circulate":   return fanCirculate()
		case "on":          return fanOn()
    }

    log.error "Invalid fan mode: \'${fanMode}\'"
}

def off() {
	log.debug "Executing 'off'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: 0
	
	sendEvent(name: "thermostatMode", value: "off")
	return parent.buildAction("POST", "/SetThermostatMode", body)
}

def heat() {
	log.debug "Executing 'heat'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: 1
	
	sendEvent(name: "thermostatMode", value: "heat")
	return parent.buildAction("POST", "/SetThermostatMode", body)
}

def emergencyHeat() {
	log.debug "Executing 'emergencyHeat'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: 4
	
	sendEvent(name: "thermostatMode", value: "emergency heat")
	return parent.buildAction("POST", "/SetThermostatMode", body)
}

def cool() {
	log.debug "Executing 'cool'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: 2
	
	sendEvent(name: "thermostatMode", value: "cool")
	return parent.buildAction("POST", "/SetThermostatMode", body)
}

def auto() {
	log.debug "Executing 'auto'"
	
	def unitid = device.deviceNetworkId.split(":")[2]
	
	def json = new JsonBuilder()
	def body = json id: unitid, value: 3
	
	sendEvent(name: "thermostatMode", value: "auto")
	return parent.buildAction("POST", "/SetThermostatMode", body)
}

def setThermostatMode() {
	log.debug "Executing 'setThermostatMode'"

    switch (mode) {
		case "off":             return off()
		case "heat":            return heat()
		case "cool":            return cool()
		case "auto":            return auto()
		case "emergency heat":  return emergencyHeat()
    }

    log.error "Invalid thermostat mode: \'${mode}\'"
}

def refresh() {
	log.debug "Executing 'refresh'"
	def unitid = device.deviceNetworkId.split(":")[2]
	return parent.buildAction("GET", "/GetThermostat?id=${unitid}", null)
}