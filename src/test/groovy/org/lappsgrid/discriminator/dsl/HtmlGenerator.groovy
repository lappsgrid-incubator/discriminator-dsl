package org.lappsgrid.discriminator.dsl

import org.anc.template.MarkupBuilderTemplateEngine

/**
 * @author Keith Suderman
 */

import org.anc.template.TemplateEngine
import org.junit.*

class HtmlGenerator {

    @Test
    void makeWithMarkup() {
        URL templateUrl = HtmlGenerator.classLoader.getResource('template.markup')
        TemplateEngine engine = new MarkupBuilderTemplateEngine(templateUrl)

        URL dslUrl = HtmlGenerator.classLoader.getResource('discriminators.config')
        DiscriminatorDsl dsl = new DiscriminatorDsl(engine)

        dsl.run(dslUrl.text, null)
        println dsl.toHtml()
    }
}
