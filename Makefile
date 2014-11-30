VERSION=$(shell cat VERSION)
JAR=target/discriminator-dsl-$(VERSION).jar
SERVER=/home/www/anc/LAPPS/vocab
CONFIG=src/main/resources/discriminators.config
PROJECT=/Users/suderman/Workspaces/IntelliJ/Lappsgrid/org.lappsgrid.discriminator
RESOURCES=$(PROJECT)/src/main/resources
JAVA=$(PROJECT)/src/main/java/org/lappsgrid/discriminator
TYPES=target/DataTypes.txt
HTML=target/discriminators.html
SITE=target/vocab
ZIP=ns.zip

help:
	@echo
	@echo "GOALS"
	@echo "   help : prints this help message."
	@echo "  clean : removed artifacts from previous builds"
	@echo "    jar : generates an executable jar file."
	@echo "   java : generates the Constants.java file."
	@echo "   html : generates HTML documentation for the discriminators."
	@echo "  types : generates the DataTypes.txt file for the discriminators."
	@echo "   site : generates vocabulary website."
	@echo " upload : uploads the discriminators.html file to the server."
	@echo "   copy : copies the Constants.java and DataTypes.txt file to the Discriminators project."
	@echo "    all : does all of the above."
	@echo
	 
clean:
	mvn clean
	
jar:
	mvn package
	
html:
	java -jar $(JAR) -h $(HTML) $(CONFIG)
	
java:
	java -jar $(JAR) -j $(CONFIG)
	
types:
	java -jar $(JAR) -d $(TYPES) $(CONFIG)
	
zip:
	pushd $(SITE) > /dev/null ; zip -r $(ZIP) ns ; popd > /dev/null

site:
	java -jar $(JAR) -p $(SITE) $(CONFIG)
	pushd $(SITE) > /dev/null ; zip -r $(ZIP) ns ; popd > /dev/null
	#zip -r $(ZIP) $(SITE)/ns
		
upload:
	anc-put $(HTML) $(SERVER)
	if [ -e $(SITE)/$(ZIP) ] ; then anc-put $(SITE)/$(ZIP) $(SERVER) ; fi
	
copy:
	cp $(TYPES) $(RESOURCES)
	cp target/Constants.java $(JAVA)

docs: html types site zip upload copy

oldall: clean jar html types site zip upload copy

all: clean jar docs


	

	