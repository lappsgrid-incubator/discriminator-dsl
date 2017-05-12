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

release:
	if [ -e $(DIST) ] ; then rm -rf $(DIST) ; fi
	mkdir $(DIST)
	cp $(TARGET_JAR) $(DIST)
	cat src/test/resources/ddsl | sed 's/__VERSION__/'$(VERSION)'/' > $(DIST)/ddsl
	cd $(DIST) ; tar czf discriminator-$(VERSION).tgz ddsl $(JAR)

ifeq ($(TOKEN),)
commit:
	@echo
	@echo "Please set the TOKEN variable with your GitHub API token."
	@echo
	@echo "If you just ran the 'make all' goal then you only need to do"
	@echo "'make commit' after setting the TOKEN variable."
	@echo
else
commit:
	ghc -f vocabulary.commit -t $(TOKEN)
endif

upload: 
	scp -P 22022 $(DIST)/discriminator-$(VERSION).tgz anc.org:/home/www/anc/downloads
	scp -P 22022 $(DIST)/discriminator-$(VERSION).tgz anc.org:/home/www/anc/downloads/discriminators-latest.tgz

all: clean jar install release upload commit
