VERSION=$(shell cat VERSION)
#PROJECT=$(shell pwd)
JAR=discriminator-$(VERSION).jar
TARGET_JAR=target/$(JAR)
DIST=target/dist
SERVER=/home/www/anc/LAPPS/vocab
CONFIG=src/main/resources/discriminators.config
PROJECT=/Users/suderman/Workspaces/IntelliJ/Lappsgrid/org.lappsgrid.discriminator
RESOURCES=$(PROJECT)/src/main/resources
JAVA=$(PROJECT)/src/main/java/org/lappsgrid/discriminator
TYPES=target/DataTypes.txt
HTML=target/discriminators.html
SITE=target/vocab
ZIP=ns.zip
SCRIPT=$(HOME)/bin/ddsl
DISCRIMINATOR_TEMPLATE=src/main/resources/template.markup
PAGE_TEMPLATE=src/main/resources/pages.markup

help:
	@echo
	@echo "GOALS"
	@echo "   clean : removes artifacts from previous builds"
	@echo "     jar : creates an executable jar file"
	@echo " install : installs the binary to ~/bin and the vocab project"
	@echo " release : uploads a .tgz distribution to http://www.anc.org/downloads"
	@echo "    help : displays this help message"

clean:
	mvn clean

jar:
	mvn package

install:
	cp $(TARGET_JAR) $(HOME)/bin
	cat src/test/resources/ddsl | sed 's/__VERSION__/'$(VERSION)'/' > $(SCRIPT)
	if [ -d ../vocab/bin ]; then cp $(TARGET_JAR) ../vocab/bin; fi
	if [ -d ../vocab/bin ]; then cp $(SCRIPT) ../vocab/bin; if

release:
	if [ -e dist ] ; rm -rf dist ; fi
	mkdir dist
	cp $(TARGET_JAR) dist	
	cat src/test/resources/ddsl | sed 's/__VERSION__/'$(VERSION)'/' > dist/ddsl
	cd dist ; tar czf ddsl $(JAR) discriminators-$(VERSION).tgz
	anc-put dist/discriminators-$(VERSION).tgz /home/www/anc/downloads
	anc-put dist/discriminators-$(VERSION).tgz /home/www/anc/downloads/discriminators-latest.tgz

all: clean jar install release
