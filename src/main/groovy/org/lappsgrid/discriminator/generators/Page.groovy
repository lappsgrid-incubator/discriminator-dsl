package org.lappsgrid.discriminator.generators

import org.lappsgrid.discriminator.dsl.DiscriminatorDelegate

/**
 * @author Keith Suderman
 */
class Page implements Iterable<Info> {
    String name
    String path
    List<Info> discriminators = []

    void add(DiscriminatorDelegate discriminator, String uri, String fragement) {
        discriminators << new Info(discriminator: discriminator, uri: uri, fragment: fragement)
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
