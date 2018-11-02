# SmartThings-OmniPro
The included SmartApp and Device Handlers provide integration between Samsung SmartThings and HAI/Leviton OmniPro II controllers utilizing [OmniLinkBridge](https://github.com/excaliburpartners/OmniLinkBridge). The bridge connects to the controller and provides a web service API for the SmartThings hub to interface with.

## Installation
1. Login to the [SmartThings IDE](https://graph.api.smartthings.com/)
2. Under My SmartApps, click Enable GitHub Integration
3. Under My SmartApps, click Settings, Add new repository
	- Owner: excaliburpartners
	- Name: SmartThings-OmniPro
	- Branch: master
4. Under My SmartApps, click Update from Repo, check excaliburpartners:OmniPro Integration, check publish, click Execute Update
5. Under My Device Handlers, click Update from Repo, check all for excaliburpartners:OmniPro, check publish, click Execute Update
6. Open the SmartThings app on your phone
7. Select Marketplace, SmartApps, scroll to bottom My Apps
8. Select OmniPro Integration and enter the IP address of the computer running OmniLinkBridge and port 8000

## Discovery
The SmartApp will auto discover and add the devices from the OmniPro controller. The current hard coded mapping is below. By configuring the SmartThings Smart Home Monitor as armed and monitoring the area contact sensor SmartThings can react to OmniPro II area burglary alarms.

Unit -> Switch, Switch Level

Thermostat -> Thermostat, Relative Humidity Measurement

Area
- Burglary Alarm -> Contact Sensor
- Water Alarm -> Water Sensor
- Fire Alarm -> Smoke Detector
- Gas Alarm -> Carbon Monoxide Detector
- Security Mode -> No capability mapping
 
When the OmniPro Area 1 security mode changes the SmartThings hub mode will change as well
- OFF, DAY -> Home
- NIGHT -> Night
- AWAY -> Away
- VACATION -> Vacation 
 
Zone
- Entry/Exit, X2EntryDelay, X24EntryDelay, Perimeter, Tamper, Auxiliary -> Contact Sensor
- AwayInt, NightInt -> Motion Sensor
- Water -> Water Sensor
- Fire -> Smoke Detector, Tamper Alert (for trouble condition)
- Gas -> Carbon Monoxide Detector, Tamper Alert (for trouble condition)