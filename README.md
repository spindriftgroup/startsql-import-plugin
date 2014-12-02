StartSQLImport Gradle Plugin
============================
Provides gradle plugin wrapper functionality for Oracle Web Commerce's (ATG) startSQLImport import tool which imports data into a versioned repository

Note
====
The first time startSQLImport is executed it prompts for usage acceptance requiring a yes/no answer.
If you don't accept the license via a manual execution the first time the task will hang.
This is now dealt with (since 0.2.0) by running the markLicenseAsRead task as a dependency of startSQLImport.

Environmental Requirements
==========================
To run this plugin a few environment requirements must be met. The build's integration test will also fail if these requirements are not met.

1. An Oracle Web Commerce (ATG) (10.x or later) must be installed containing the startSQLImport script in $ATG_HOME/home/bin.
	 The script must be on the system path e.g. in the users .bash_profile: 

		export ATG_HOME=/path/to/ATG/ATG11.1
		export PATH=$PATH:$ATG_HOME/home/bin
		or
		export ATG_HOME=/path/to/ATG/ATG11.1/home
		export DYNAMO_HOME=$ATG_HOME/home
		export PATH=$PATH:$DYNAMO_HOME/bin

2. The ATGJRE environment variable must be set otherwise the java security policy will not be found, e.g. in the users .bash_profile: 

		export ATGJRE=$JAVA_HOME/bin/java

3. A JDBC driver must be on the classpath when the startSQLImport script starts up. This can be achieved in different ways, e.g.
	 - Add the driver classes to $ATG_HOME/home/locallib
	 - Create a new module with a manifest that refers to the class in the gradle cache or some other location, e.g. $ATG_HOME/MyModule/META-INF/MANIFEST.MF
		
		ATG-Class-Path: /path/to/ojdbc6-11.2.0.3.0.jar

4. An existing database installation must already exist with a fully created BCC/CA schema, e.g. created by CIM or some other database management execution.

5. The ATG datasources must be configured correctly to refer the the BCC/CA schema.This can be done in a module that is included in the modules list or a local server and specified as a server option.

Usage
=====
To use the plugin, include it in your build script:


    buildscript {
      repositories {
        jcenter()
      }
      dependencies {
        classpath 'com.spindrift.atg:start-sql-import:0.2.0'
      }
    }
    apply plugin: 'start-sql-import'


Custom Task Types
-----------------

StartSQLImport - Executes the startSQLImport script for each configuration

Additional Tasks
----------------
markLicenseAsRead - This will create the licence.read file to enable automatic acceptance of the license. 
Note: Its your own responsibility to read and accept the license, but this is necessary in an automated setup.

Extension Properties
--------------------

<table>
  <thead>
    <tr>
      <th>Property</th><th>Desciption</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>imports</td><td>Container of script configurations</td>
    </tr>
  </tbody>
</table>

The *imports* is a collection(list) of script configurations.

<table>
  <thead>
    <tr>
      <th>Property</th><th>Desciption</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>parameters</td><td>A single script configuration set of parameters</td>
    </tr>
  </tbody>
</table>

Each script configuration *parameters* includes the basic required parameters plus optional extras.

<table>
  <thead>
    <tr>
      <th>Property</th><th>Desciption</th>
    </tr>
  </thead>
  <tbody>
    <tr><td>file</td><td>The XML file to import</td></tr>
    <tr><td>workspace</td><td>The workspace name, mutually exclusive with project</td></tr>
    <tr><td>project</td><td>The project name, mutually exclusive with workspace</td></tr>
    <tr><td>options</td><td>The extra optional parameters</td></tr>
  </tbody>
</table>

The *options* contain basic required options and any additional optional parameters

<table>
  <thead>
    <tr>
      <th>Property</th><th>Desciption</th>
    </tr>
  </thead>
  <tbody>
    <tr><td>modules</td><td>The list of modules (default is Publishing)</td></tr>
    <tr><td>server</td><td>The ATG server to include in the script run</td></tr>
    <tr><td>repository</td><td>The repository to import into</td></tr>
    <tr><td>options</td><td>The extra optional parameters</td></tr>
  </tbody>
</table>

Example Configuration
=====================

The following example imports data from 2 local files into the Site and ProductCatalog repositories, using the Core Commerce DCS.Versioned, a custom module and a custom server.


    startSQLImport {
      imports {
        parameters {
          file = 'importSite.xml'
          workspace = 'default-site'
          options {
            modules = ['DCS.Versioned','MyModule']
            server = 'MyServer'
            repository = '/atg/multisite/SiteRepository'
          }
        }
        parameters {
          file = 'importCatalog.xml'
          workspace = 'default-catalog'
          options {
            modules = ['DCS.Versioned','MyModule']
            server = 'MyServer'
            repository = '/atg/commerce/catalog/ProductCatalog'
          }
        }
      }
    }

Execution
=========

    gradle startSQLImport
    
Build Notes
===========
The default task is set to a local maven install so that the plugin is available for testing in local builds 
``gradle | ./gradlew | gradle install``

To execute the integration test you must have all the environment dependencies set up. I have other external tasks 
which generate the JDBCDriver module and the gradleImport server.
From the root of this project execute the remote gradle build inside the integration-test folder: 
``gradle integrationTask``

Example integration test output
-------------------------------

    :integration-test:markLicenseAsRead
    :integration-test:startSQLImport
    Executing: [startSQLImport, -file, integration-test/importSite.xml, -workspace, default-site1417493451580, -m, DCS.Versioned, JDBCDriver, -s, gradleImport, -repository, /atg/multisite/SiteRepository]
    Imported integration-test/importSite.xml in 30.594 secs
    Executing: [startSQLImport, -file, integration-test/importCatalog.xml, -workspace, default-catalog14174934515922, -m, DCS.Versioned, JDBCDriver, -s, gradleImport, -repository, /atg/commerce/catalog/ProductCatalog]
    Imported integration-test/importCatalog.xml in 26.38 secs
    
    







