VERSION=$(shell cat VERSION)
JAR=discriminator-dsl-$(VERSION).jar
TARGET_JAR=target/$(JAR)
SERVER=/home/www/anc/LAPPS/vocab
CONFIG=src/main/resources/discriminators.config
PROJECT=/Users/suderman/Workspaces/IntelliJ/Lappsgrid/org.lappsgrid.discriminator
RESOURCES=$(PROJECT)/src/main/resources
JAVA=$(PROJECT)/src/main/java/org/lappsgrid/discriminator
TYPES=target/DataTypes.txt
HTML=target/discriminators.html
SITE=target/vocab
ZIP=ns.zip
SCRIPT=$(HOME)/bin/d

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
	java -jar $(TARGET_JAR) -h $(HTML) $(CONFIG)
	
install:
	cp $(TARGET_JAR) $(HOME)/bin
	echo "#!/bin/bash" > $(SCRIPT)
	/bin/echo -n "java -jar $(HOME)/bin/$(JAR) " >> $(SCRIPT)
	/bin/echo -n $$ >> $(SCRIPT)
	/bin/echo "@" >> $(SCRIPT)
	
java:
	java -jar $(TARGET_JAR) -j $(CONFIG)
	
types:
	java -jar $(TARGET_JAR) -d $(TYPES) $(CONFIG)
	
zip:
	pushd $(SITE) > /dev/null ; zip -r $(ZIP) ns ; popd > /dev/null

site:
	java -jar $(TARGET_JAR) -p $(SITE) $(CONFIG)
	pushd $(SITE) > /dev/null ; zip -r $(ZIP) ns ; popd > /dev/null
	#zip -r $(ZIP) $(SITE)/ns
		
upload:
	anc-put $(HTML) $(SERVER)
	if [ -e $(SITE)/$(ZIP) ] ; then anc-put $(SITE)/$(ZIP) $(SERVER) ; fi

css:
	cp src/main/resources/*.css target
	
remote:
	anc-put src/main/resources/discriminators.css /home/www/anc/LAPPS/vocab
	
copy:
	cp $(TYPES) $(RESOURCES)
	cp target/Constants.java $(JAVA)

docs: html types site zip upload copy

oldall: clean jar html types site zip upload copy

all: clean jar docs


	

	