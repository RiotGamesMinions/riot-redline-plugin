Description
===========

Uses the pure Java [Redline](http://code.google.com/p/redline-rpm/) library to create RPMs during the `package` phase of the Maven lifecycle.

Offers an Operating System agnostic way of creating RPMs that differs from Redline's provided Ant tasks.

Requirements
============

* Java 1.6
* Maven 3.0.X

Example Usage
=============

```xml
<plugin>
	<groupId>com.riotgames.maven</groupId>
	<artifactId>riot-redline-plugin</artifactId>
	<version>1.2.0-SNAPSHOT</version>
	<configuration>
		<platform>
			<architecture>noarch</architecture>
			<os>linux</os>
		</platform>
		<packaging>
			<name>my-rpm-name</name>
            <version>1.0</version>
            <release>${maven.build.timestamp}</release>
		</packaging>
		<group>Applications/System</group>
		<vendor>My Company</vendor>
		<license>COMMERCIAL</license>
		<url>www.riotgames.com</url>
		<summary>A summary of my RPM.</summary>
		<destination>${project.basedir}/target</destination>
		<attach>true</attach>
	</configuration>
</plugin>
```

See the [Wiki](https://github.com/RiotGames/riot-redline-plugin/wiki) for more detailed configuration examples and requirements.

License and Author
==================

Author:: Kyle Allan (<kallan@riotgames.com>)

Copyright:: 2012 Riot Games Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.