Unique ID
=========

An attempt to answer the question "how can I fiud out the unique ID of my device".

There is no single answer.

For telephones, the hardware IMEI or similar is available. May be null.

For tablets and phones, the hardware serial number is requested. May be null.

For all, the "Android ID" is requested; this is software-generated, may not be null,
but will change if the user does a "factory reset" or "security wipe".

An alternate would be to check the hardware Mac address, but some devices
don't have WiFi, some don't have phone hardware, some have more than one.

I did say there is no single answer.
