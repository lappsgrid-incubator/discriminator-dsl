package org.lappsgrid.discriminator.generators

import org.anc.template.MarkupBuilderTemplateEngine
import org.anc.template.TemplateEngine
import org.lappsgrid.discriminator.dsl.DiscriminatorDelegate
import org.lappsgrid.discriminator.dsl.DiscriminatorDsl
import org.lappsgrid.discriminator.dsl.Version

/**
 * @author Keith Suderman
 */
class Main {
    ClassLoader loader = Main.classLoader
    DiscriminatorDsl dsl

    DiscriminatorDsl run(String filename) {
        DiscriminatorDsl dsl = new DiscriminatorDsl()
        File file = new File(filename)
        if (!file.exists()) {
            println "Unable to find discriminator configuration file: ${file.path}"
            return null
        }
        dsl.run(file.text, null)
        return dsl
    }

    void generatePages(String filename, String outputPath) {
        Map<String, Page> pages = [:]
        DiscriminatorDsl dsl = run(filename)
        if (dsl == null) {
            println "ERROR: DSL failed to run."
            return
        }
        dsl.discriminators.each { DiscriminatorDelegate discriminator ->
//            println discriminator.name
            String uri = discriminator.uri.replace("http://vocab.lappsgrid.org/", '')
            if (uri.startsWith('ns')) {
                String path = uri
                String fragment = null
                int hash = uri.indexOf('#')
                if (hash > 0) {
                    path = uri.substring(0, hash)
                    fragment = uri.substring(hash)
                }
                Page page = pages[path]
                if (page == null) {
                    page = new Page()
                    page.name = path.replace('ns/', '')
                    page.path = path
//                    page.fragment = fragment
                    pages[path] = page
                }
                page.add(discriminator, uri, fragment)
            }
        }
        File root = new File(outputPath)
        if (!root.exists()) {
            if (!root.mkdirs()) {
                throw new IOException("Output directory does not exist and cannot be created: ${root.path}")
            }
        }
        println "There are ${pages.size()} pages."
        URL templateUrl = loader.getResource('pages.markup')
        TemplateEngine engine = new MarkupBuilderTemplateEngine(templateUrl)
        pages.each { String Path, Page page ->
            File file = new File(root, "${page.path}.html")
            File directory = file.parentFile
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("Unable to create directory ${directory.path}")
                }
            }
            println "Generating page ${file.path}"
            file.text = engine.generate(page:page)
//            page.each { Page.Info info -> println "${info.discriminator.name} ${info.uri}"}
        }
    }

    void generateHtml(String filename, String outputPath) {

        DiscriminatorDsl dsl = run(filename)
        if (dsl == null) {
            return
        }
        URL templateUrl = loader.getResource('alternate.markup')
        TemplateEngine engine = new MarkupBuilderTemplateEngine(templateUrl)
        String html = engine.generate([bindings:dsl.bindings, discriminators:dsl.discriminators])
        File file = new File(outputPath)
        file.text = html
        println "Wrote ${file.path}"
    }

    void generateDataTypes(String filename, String outputPath) {
        DiscriminatorDsl dsl = run(filename)
        if (dsl == null) {
            return
        }
        File file = new File(outputPath)
        PrintWriter out = new PrintWriter(file)
        int prev=Integer.MIN_VALUE
        out.println "### DO NO EDIT THIS FILE"
        out.println "### This file is machine generated and any changes are"
        out.println "### likely to be overwritten by future updates."
        dsl.discriminators.each { DiscriminatorDelegate delegate ->
            String uri
            if (delegate.uri.startsWith('http')) {
                uri = delegate.uri
            }
            else {
                uri = ':' + delegate.uri.replaceAll(' ', '')
            }
            if (delegate.id == (prev+1)) {
                out.println "${delegate.name} ${delegate.ancestors.join(' ')} ${uri}"
            }
            else {
                out.println()
                out.println "${delegate.id}: ${delegate.name} ${delegate.ancestors.join('\t')} ${uri}"
                //println "${delegate.id}: ${delegate.name} ${delegate.ancestors.join(' ')} ${uri}"
            }
            prev = delegate.id
        }
        out.close()
        println "Wrote ${file.path}"
    }

    static void main(args) {
//        new Main().generateDataTypes()
        CliBuilder cli = new CliBuilder()
        cli.usage = "java -jar Generator-${Version.version}.jar [-d <filename>] [-h <filename>] [-?] -o <path>"
        cli.header = "Generates documentation for the LAPPS discriminator URI."
        cli.d(longOpt:'datatypes', args:1, required:false, 'generate the DataTypes.txt file')
        cli.h(longOpt:'html', args:1, required:false, 'generate html documentation')
        cli.p(longOpt:'pages', args:1, required: false, 'generate vocab site')
        cli.'?'(longOpt:'help', required:false, 'this help message')
        cli.v(longOpt: 'version', required: false, 'display the current version number')

        def params = cli.parse(args)
        if (!params) {
            return
        }
        if (params['?']) {
            cli.usage()
            return
        }
        if (params.v) {
           println """
LAPPS Discriminator Documentation Generator v${Version.version}
Copyright 2014 American National Corpus

"""
           return
        }
        List<String> fileNames = params.arguments()
        if (fileNames.size() == 0) {
            println 'No discriminator configuration file provided.'
            return
        }
        if (fileNames.size() > 1) {
            println 'More than one discriminator configuration file provided.'
            println 'Using the first and ignoring the rest.'
        }

        String filename = fileNames[0]
        int actions = 0
        Main app = new Main()
        if (params.d) {
            app.generateDataTypes(filename, params.d)
            ++actions
        }
        if (params.h) {
            app.generateHtml(filename, params.h)
            ++actions
        }
        if (params.p) {
            println "Generating vocab web site."
            app.generatePages(filename, params.p)
            ++actions
        }
        if (actions == 0) {
            cli.usage()
        }
    }
}
