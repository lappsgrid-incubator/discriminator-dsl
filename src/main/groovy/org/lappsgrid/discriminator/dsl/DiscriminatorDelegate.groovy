package org.lappsgrid.discriminator.dsl

/**
 * @author Keith Suderman
 */
class DiscriminatorDelegate {
    long id
    String name
    String uri = "unassigned"
    String description = ""
    String deprecated = ""
    List<String> ancestors = []

    public DiscriminatorDelegate(long id) {
//        println "DiscriminatorDelegate: ${id}"
        this.id = id
    }

    void uri(String uri) {
        this.uri = uri
    }
    void description(String description) {
        this.description = description
    }
    void deprecated(String deprecated) {
        this.deprecated = deprecated
    }
    void parents(Object... args) {
        args.each { ancestors << it }
    }

    void parents(args) {
        if (args instanceof List) {
            ancestors.addAll(args)
        }
        else {
            ancestors << args
        }
    }

    String toString() {
        println "${ancestors.reverse().join(' -> ')} $id $uri"
        println description
    }
}
