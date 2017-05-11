VERSION=$(shell cat VERSION)
JAR=discriminator-$(VERSION).jar
TARGET_JAR=target/$(JAR)
DIST=target/dist
SCRIPT=$(HOME)/bin/ddsl


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
	if [ -d ../vocab/bin ]; then cp $(SCRIPT) ../vocab/bin; fi

release:
	if [ -e $(DIST) ] ; then rm -rf $(DIST) ; fi
	mkdir $(DIST)
	cp $(TARGET_JAR) $(DIST)
	cat src/test/resources/ddsl | sed 's/__VERSION__/'$(VERSION)'/' > $(DIST)/ddsl
	cd $(DIST) ; tar czf discriminator-$(VERSION).tgz ddsl $(JAR)

upload:
	anc-put $(DIST)/discriminators-$(VERSION).tgz /home/www/anc/downloads
	anc-put $(DIST)/discriminators-$(VERSION).tgz /home/www/anc/downloads/discriminators-latest.tgz

all: clean jar install release upload
