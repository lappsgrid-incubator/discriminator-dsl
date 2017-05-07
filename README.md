org.lappsgrid.discriminator.dsl
===============================


**Build Status**:
[![Build Status](http://grid.anc.org:9080/travis/svg/lappsgrid-incubator/org.lappsgrid.discriminator.dsl?branch=master)](https://travis-ci.org/lappsgrid-incubator/org.lappsgrid.discriminator.dsl)
[![Build Status](http://grid.anc.org:9080/travis/svg/lappsgrid-incubator/org.lappsgrid.discriminator.dsl?branch=develop)](https://travis-ci.org/lappsgrid-incubator/org.lappsgrid.discriminator.dsl)


Discriminators are published at http://vocab.lappsgrid.org/discriminators. To update those pages you need to edit discriminators.config in the https://github.com/lapps/vocabulary-pages repository (that file now only lives in the 1.1.0 branch). Then new html files for the discriminators can be created using the Makefile in the vocabulary-pages repository.

That Makefile uses a `ddsl` startup script as well as `discriminator-VERSION` jar file and both of those are created by this repository. Create startup script and jar as follows:

```
$ make jar
$ make release
```

**WARNING**: this does not work quite yet since the Makefile is still being rewritten.
