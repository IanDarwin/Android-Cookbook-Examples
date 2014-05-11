Building and Running the Server Application.

This app has been tested with JBoss AS (jboss-as-7.1.1.Final) only.

JBoss AS 7 cannot be configured completely automatically because things like 
DataSources may be shared, they go in the application server's 
configuration instead of where you might think it belongs.

This directory contains files for AS7 config to make this application run.

Steps:

0) Install JBoss AS7 App Server
1) Install DB Driver
2) Configure DataSource
3) Install and configure Database.
3a) Start JBoss
4) Build and Deploy the Web App

0) Install App Server

Download JBoss AS7 from http://www.jboss.org/jbossas/downloads/. 
Unzip into your home directory (for development) or some other suitable 
spot (for production). Don't start it yet!

1) Install DB Driver

Simple, just copy the Postgresql JDBC 4 driver from this jbossas7 folder
to  $JBOSS_HOME/standalone/deployments/

Since we're now using PostGreSQL, whose JDBC driver actually works as a JDBC4 driver,
this does not need any additional configuration.

2) Configure App Server (including DataSource).

The file datasource.xml.add needs to be inserted into
 JBOSS_HOME/standalone/configuration/standalone.xml in the correct spot;
 see the comment at the top of the file for where to place it.

(This was created by following the steps here: 
https://community.jboss.org/wiki/JBossAS7-DatasourceConfigurationForPostgresql
Then manually extracting the <datasource> element from standalone.xml.

N.B. While you have the standalone.xml file open for editing, find the line

<virtual-server name="default-host" enable-welcome-root="true">

and change "true" to "false". We want to serve the web app at "/" not
under a subdirectory.

See also:

https://community.jboss.org/wiki/DataSourceConfigurationinAS7

http://docs.jboss.org/hibernate/orm/3.3/reference/en/html/session-configuration.html#configuration-sessionfactory

3) Download and install the Database

I use PostGres 9.1, which you can download from 
http://www.postgresql.org/download/.

For OS X use the "one-click installer" option. N.B. See the README for updates
to /etc/sysctl.conf. This requires a reboot to apply.  After that, run the installer
in the DMG file. Do pick a password for the admin account; remember it!
You do not need anything from the "Stack Builder".

Run the "SQL Shell" from the "Applications/Postgresql 9.1" folder (you may
wish to save this in your Dock, but it does behave strangely, starting a new
window when you click on it, so click on the normal Terminal icon if you
just want to bring it to the front).  Create the application-specific
user and database, as shown in this script:

Server [localhost]: 
Database [postgres]: 
Port [5432]: 
Username [postgres]: 
Password for user postgres: 
psql (9.1.3)
Type "help" for help.

<!-- Note that the username and password used here must agree with the 
datasource.xml.add that you used in the previous step, NOT the
persistence.xml file (this is the real username only!) -->
postgres=# create role telemon_db password 'XXX' login nosuperuser;
CREATE ROLE
postgres=# create database telemonitoring owner telemon_db;
CREATE DATABASE
postgres=# 

3a) Start JBoss

NOW you can start jboss:

cd JBOSS_HOME
bin/standalone.sh

4) Build and Deploy the Web App

All the information is in ServerJavaEE/pom.xml.

All you need to do is 

	cd git/Razorback/RazorbackServer
	mvn package jboss-as:deploy

It should package and deploy the server.

To test it (remember there is NO PORT 8009 anymore with the Java-based server)
a) visit http://localhost:8080/ and you should see an dummy HTML output.
b) visit http://localhost:8080/rest/status and you should see JSON output.
c) visit http://localhost:8080/rest/options/trace/true to enable REST call logging