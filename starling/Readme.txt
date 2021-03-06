Technical Challenge:

I have created a Maven project named Starling containing Java source, the class name App has a main method with the following components:
StarlingController - to coordinate the steps, get Accounts, get feed units for a week, calculate the roundup total, create a new savings goal, add the round up total to the new savings goal.
StarlingApiService - to generate HTTP GET and PUT requests with the Starling REST API, and extract the responses.
RoundUpCalculator - to calculate the round up total for a list of feed units.
Accouts - a model entity to store Account accoutUID and defaultCategoryUID

Environment:
I used Eclipse Photon Release (4.8.0) IDE
My laptop is running Windows10 and has Maven installed

Compile and Build:
A fat JAR can be generated by running the following Maven command line command for the same folder as the POM.xml:
maven clean install. JARs generated in the /target folder 
There is a pre-built starling-0.0.1-jar-with-dependencies.jar in the same folder as this Readme.txt

Excution:
Before execution a properties file used to hold the Access Token from the Developer Sandbox should be created. 
File absolute path -  c:/properties/properties.txt
There should be one property in the property file named authorization with the Access Token as the value i.e:
authorization=eyJhbGciOiJQUzI1NiJ9.eyJp......
....Un3TkkDftVboMkr3c
There is an example properties.txt file in the same folder as this Readme.txt


The fat JAR named starling-0.0.1-jar-with-dependencies.jar can be executed by running the following command line command:
java -jar starling-0.0.1-jar-with-dependencies.jar <<week beginning date>> <<savings goal name>>

The week beginning date should be in the format yyyy-MM-dd e.g. 2019-11-28. The application will use this a the first day of the week of feed units you are interested in and will calculate week start and end data time entries used in the GET request 'Get the account holder's feed items which were created between two timestamps'
The savings goal name is the name you wish to give to the new savings goal

Unit Testing:
The code contains some unit tests, which can be run from within Eclipse and will also be run as part of the Maven compile and build process.
There is a RoundUpCalculator unit Test. The AppTest unit test class contains a commented out test, to enable the Maven build to complete, which I used during development to run a complete end to end test.