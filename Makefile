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
DISCRIMINATOR_TEMPLATE=src/main/resources/template.markup
PAGE_TEMPLATE=src/main/resources/pages.markup

help:
	@echo
	@echo "GOALS"
	@echo "   help : prints this help message."
	@echo "  clean : removed artifacts from previous builds"
	@echo "    jar : generates an executable jar file."
	@echo "install : copies jar and start script to $(HOME)/bin" 
	@echo "   java : generates the Constants.java file."
	@echo "   html : generates HTML documentation for the discriminators."
	@echo "  pages : generates indivdual html pages for each discriminator." 
	@echo "  types : generates the DataTypes.txt file for the discriminators."
	@echo "   site : generates vocabulary website."
	@echo "    zip : creates a zip archive of the vocab website."
	@echo " upload : uploads the discriminators.html file to the server."
	@echo "   copy : copies the Constants.java and DataTypes.txt file to the Discriminators project."
	@echo "   docs : generates all documentation (html, types, site) and uploads/copies"
	@echo "    css : copies css files to target directory for testing."
	@echo " remote : copies the discriminators.css file to the server."
	@echo "    all : does all of the above."
	@echo
	 
clean:
	mvn clean
	
jar:
	mvn package
	
html:
	java -jar $(TARGET_JAR) -h $(HTML) -t $(DISCRIMINATOR_TEMPLATE) $(CONFIG)
	
pages:
	java -jar $(TARGET_JAR) -p $(SITE) -t $(PAGE_TEMPLATE) $(CONFIG)

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

site: pages
	pushd $(SITE) > /dev/null ; zip -r $(ZIP) ns ; popd > /dev/null
	#zip -r $(ZIP) $(SITE)/ns
		
upload:
	anc-put $(HTML) $(SERVER)
	if [ -e $(SITE)/$(ZIP) ] ; then anc-put $(SITE)/$(ZIP) $(SERVER) ; fi
	anc-put src/main/resources/discriminators.css $(SERVER)

css:
	cp src/main/resources/*.css target
	
remote:
	anc-put src/main/resources/discriminators.css $(SERVER)
	
copy:
	cp $(TYPES) $(RESOURCES)
	cp target/Constants.java $(JAVA)

docs: html types site zip upload copy

oldall: clean jar html types site zip upload copy

all: clean jar docs


	

	