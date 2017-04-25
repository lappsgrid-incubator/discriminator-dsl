package org.lappsgrid.discriminator.dsl

import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static org.junit.Assert.*

/**
 * @author Keith Suderman
 */
class DslTest {

    DiscriminatorDsl dsl;

    @Before
    void setup() {
        dsl = new DiscriminatorDsl()
    }

    @After
    void cleanup() {
        dsl = null
    }

    @Test
    void helloWorld() {
        println "DslTest.helloWorld"
        compile "println 'Hello world.'"
    }

    @Ignore
    void offsetTest1() {
        println "DslTest.offsetTest1"
        compile """
offset(0) {}
"""
    }
    @Ignore
    void offsetTest2() {
        println "DslTest.offsetTest2"
        compile """
offset(0) {
    println "Passed."
}
"""
    }

    @Test
    void errorTest() {
        println "DslTest.errorTest"
        compile """
    error {
        uri 'http://ns.lappsgrid.org/1.0/error'
        description 'Base type for all errors returned by LAPPS services.'
    }
"""
        assertTrue dsl.discriminators.size() == 1
        DiscriminatorDelegate discriminator = dsl.discriminators[0]
        discriminator.uri == 'http://ns.lappsgrid.org/1.0/error'
    }

    @Ignore
    void testInheritance() {
        println "DslTest.testInheritance"
        compile """
offset(0) {
    parent {
        uri "http://vocab.lappsgrid.org/parent"
    }
    child {
        uri "http://vocan.lappsgrid.org/child"
        parents parent
    }
}
"""
    }

    @Ignore
    void deeperInheritance() {
        compile """
offset(0) {
    parent {
        uri "http://vocab.lappsgrid.org/parent"
    }
    child {
        uri "http://vocan.lappsgrid.org/child"
        parents parent
    }
    grandchild {
        uri "http://vocan.lappsgrid.org/grandchild"
        parents child
    }
}
"""
    }

    @Test
    void testHyphens() {
        compile """
offset(0) {
    text {
        uri 'http://text'
    }
    'more-text' {
        uri 'http://more-text'
    }
    string {
        uri 'http://string'
        parents 'more-text'
    }
}
"""
    }

    @Ignore
    void parseDsl() {
        println "DslTest.parseDsl"
        URL dsl = DslTest.class.classLoader.getResource('discriminators.config')
        if (dsl == null) {
            fail("Unable to locate the discriminators.config file.")
        }
        compile(dsl.text)
    }
    void compile(String source) {
        try {
            dsl.run(source, null)
        }
        catch (Exception e) {
            e.printStackTrace()
            fail(e.message)
        }
    }
}
