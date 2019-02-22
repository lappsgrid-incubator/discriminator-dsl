LAPPS Discriminator DSL
===============================


**Build Status**:
[![Build Status](http://grid.anc.org:9080/travis/svg/lappsgrid-incubator/discriminator-dsl?branch=master)](https://travis-ci.org/lappsgrid-incubator/discriminator-dsl)
[![Build Status](http://grid.anc.org:9080/travis/svg/lappsgrid-incubator/discriminator-dsl?branch=develop)](https://travis-ci.org/lappsgrid-incubator/discriminator-dsl)


Discriminators are published at http://vocab.lappsgrid.org/discriminators. To update those pages you need to edit discriminators.config in the https://github.com/lapps/vocabulary-pages repository. Then new html files for the discriminators can be created using the Makefile in the vocabulary-pages repository.

That Makefile uses a `ddsl` startup script as well as `discriminator-VERSION` jar file and both of those are created by this repository. Create startup script and jar as follows:

```
$ make jar
$ make release
```

After this, there will be an archive named `target/dist/discriminator-VERSION.tgz` and the contents of that archive need to be put in the `bin` directory of https://github.com/lapps/vocabulary-pages.
