# SmartThings-OmniPro
The included SmartApp and Device Handlers provide integration between Samsung SmartThings and HAI/Leviton OmniPro II controllers utilizing the HAILogger application as a middle layer. The [HAILogger](https://github.com/excaliburpartners/HAILogger) connects using the HAI SDK to the controller and provides a web service for SmartThings to interface with.

##Installation
1. Login to the [SmartThings IDE](https://graph.api.smartthings.com/)
2. Click My Locations, then select your location
2. Click My Device Handlers, select From Code, paste the contents from GitHub and repeat for all device files
3. Click My SmartApps, select From Code, paste the contents from GitHub
4. Open the SmartThings app on your phone
5. Select Marketplace -> SmartApps -> scroll to bottom My Apps
6. Select OmniPro Integration and enter the IP address of the computer running HAILogger and port 8000

## Discovery
The SmartApp will auto discover and add the devices from the OmniPro controller. The current hard coded mapping is below. By configuring the SmartThings Smart Home Monitor as armed and monitoring the area contact sensor SmartThings can react to OmniPro II area burlgary alarms.

Unit -> Switch, Switch Level

Thermostat -> Thermostat, Relative Humidity Measurement

Area
- Burglary Alarm -> Contact Sensor
- Water Alarm -> Water Sensor
- Fire Alarm -> Smoke Detector
- Gas Alarm -> Carbon Monoxide Detector
- Security Mode -> No capability mapping
 
When the OmniPro Area 1 security mode changes the SmartThings hub mode will change as well
OFF, DAY -> Home
NIGHT -> Night
AWAY -> Away
VACATION -> Vacation 
 
Zone
- Entry/Exit, X2EntryDelay, X24EntryDelay, Perimeter -> Contact Sensor
- AwayInt -> Motion Sensor
- Water -> Water Sensor
- Fire -> Smoke Detector, Tamper Alert (for trouble condition)
- Gas -> Carbon Monoxide Detector, Tamper Alert (for trouble condition)