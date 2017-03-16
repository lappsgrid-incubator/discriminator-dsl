org.lappsgrid.discriminator.dsl
===============================

### Build Status

| Branch | Status |
|:-------|--------|
|master|[![Build Status](http://grid.anc.org:9080/travis/svg/lappsgrid-incubator/org.lappsgrid.discriminator.dsl?branch=master)](https://travis-ci.org/lappsgrid-incubator/org.lappsgrid.discriminator.dsl)|
|develop|[![Build Status](http://grid.anc.org:9080/travis/svg/lappsgrid-incubator/org.lappsgrid.discriminator.dsl)](https://travis-ci.org/lappsgrid-incubator/org.lappsgrid.discriminator.dsl)|

DSL used to describe everything related to LAPPS discriminators.

Discriminators are published at http://vocab.lappsgrid.org/discriminators. To update those pages first edit [discriminators.config](https://github.com/lappsgrid-incubator/org.lappsgrid.discriminator.dsl/blob/master/src/main/resources/discriminators.config) as needed. Then create the new pages:

```
$ make clean jar html site
```

This cleans out the previous build (`clean`), creates a new jar (`jar`) and creates the elements of the new discriminators (`html` and `site`). Note that the main page of the discriminators site also points at annotation types in the vocabulary, those are defined elsewhere in https://github.com/lapps/vocabulary-pages and https://github.com/lapps/org.lappsgrid.vocabulary..
