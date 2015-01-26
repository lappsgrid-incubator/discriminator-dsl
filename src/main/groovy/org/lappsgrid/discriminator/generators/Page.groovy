package org.lappsgrid.discriminator.generators

import org.lappsgrid.discriminator.dsl.DiscriminatorDelegate

/**
 * The information needed to generate a single html page for one or
 * more related discriminator types. E.g. discriminators for licenses.
 *
 * @author Keith Suderman
 */
class Page implements Iterable<Info> {
    String name
    String path
    List<Info> discriminators = []

    void add(DiscriminatorDelegate discriminator, String uri, String fragment) {
        discriminators << new Info(discriminator: discriminator, uri: uri, fragment: fragment)
    }

    Iterator<Info> iterator() {
        return discriminators.iterator()
    }

    class Info {
        DiscriminatorDelegate discriminator
        String uri
        String fragment
    }
}
