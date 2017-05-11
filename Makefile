VERSION=$(shell cat VERSION)
JAR=discriminator-$(VERSION).jar
TARGET_JAR=target/$(JAR)
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
	cp $(TARGET_JAR) ../vocab/bin
	cat src/test/resources/ddsl | sed 's/__VERSION__/'$(VERSION)'/' > $(SCRIPT)
	cp $(SCRIPT) ../vocab/bin

release:
	if [ -e dist ] ; then rm -rf dist ; fi
	mkdir dist
	cp $(TARGET_JAR) dist	
	cat src/test/resources/ddsl | sed 's/__VERSION__/'$(VERSION)'/' > dist/ddsl
	cd dist ; tar czf discriminators-$(VERSION).tgz ddsl $(JAR)
	anc-put dist/discriminators-$(VERSION).tgz /home/www/anc/downloads
	anc-put dist/discriminators-$(VERSION).tgz /home/www/anc/downloads/discriminators-latest.tgz
	
all: clean jar install release



	

	