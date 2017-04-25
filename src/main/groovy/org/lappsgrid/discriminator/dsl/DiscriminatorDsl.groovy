package org.lappsgrid.discriminator.dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

/**
 * @author Keith Suderman
 */
class DiscriminatorDsl {
    static final String EXTENSION = ".discriminators"

    Set<String> included
    File parentDir
    Binding bindings
    List discriminators
    int offset = 0
//    TemplateEngine engine

    public DiscriminatorDsl() {
        included = new HashSet<String>()
        bindings = new Binding()
        discriminators = []
    }

//    public DiscriminatorDsl(TemplateEngine engine) {
//        this()
//        this.engine = engine
//    }

    void run(File file, args) {
        parentDir = file.parentFile
        run(file.text, args)
    }

    ClassLoader getLoader() {
        ClassLoader loader = Thread.currentThread().contextClassLoader;
        if (loader == null) {
            loader = this.class.classLoader
        }
        return loader
    }

    CompilerConfiguration getCompilerConfiguration() {
        ImportCustomizer customizer = new ImportCustomizer()
        /*
         * Custom imports can be defined in the ImportCustomizer.
         * For example:
         *   customizer.addImport("org.anc.xml.Parser")
         *   customizer.addStarImports("org.anc.util")
         *
         * The jar files for any packages imported this way must be
         * declared as Maven dependencies so they will be available
         * at runtime.
         */

        CompilerConfiguration configuration = new CompilerConfiguration()
        configuration.addCompilationCustomizers(customizer)
        return configuration
    }

    void run(String scriptString, args) {
        ClassLoader loader = getLoader()
        CompilerConfiguration configuration = getCompilerConfiguration()
        GroovyShell shell = new GroovyShell(loader, bindings, configuration)

        Script script = shell.parse(scriptString)
        if (args != null && args.size() > 0) {
            // Parse any command line arguements into a HashMap that will
            // be passed in to the user's script.
            def params = [:]
            args.each { arg ->
                String[] parts = arg.split('=')
                String name = parts[0].startsWith('-') ? parts[0][1..-1] : parts[0]
                String value = parts.size() > 1 ? parts[1] : Boolean.TRUE
                params[name] = value
            }
            script.binding.setVariable("args", params)
        } else {
            script.binding.setVariable("args", [:])
        }
        script.binding.setVariable("bank", { it -> BankDelegate.BANK_SIZE * it })

        script.metaClass = getMetaClass(script.class, shell)
        //try {
        // Running the DSL script creates the objects needed to generate the HTML
//            println "Running script"
        script.run()
//        }
//        catch (Exception e) {
//            println()
//            println "Script execution threw an exception:"
//            e.printStackTrace()
//            println()
//        }
//        TemplateEngine htmlEngine = new HtmlTemplateEngine(load('template.html'))
    }

    String toHtml() {
        def params = [binding: bindings, discriminators: discriminators]
        return engine.generate(params)
    }

    String load(String name) {
        URL url = this.class.classLoader.getResource(name)
        return url.text

    }

    MetaClass getMetaClass(Class<?> theClass, GroovyShell shell) {
        ExpandoMetaClass meta = new ExpandoMetaClass(theClass, false)
        meta.include = { String filename ->
            // Make sure we can find the file. The default behaviour is to
            // look in the same directory as the source script.
            // TODO Allow an absolute path to be specified.

            def filemaker
            if (parentDir != null) {
                filemaker = { String name ->
                    return new File(parentDir, name)
                }
            } else {
                filemaker = { String name ->
                    new File(name)
                }
            }

            File file = filemaker(filename)
            if (!file.exists() || file.isDirectory()) {
                file = filemaker(filename + EXTENSION)
                if (!file.exists()) {
                    throw new FileNotFoundException(filename)
                }
            }
            // Don't include the same file multiple times.
            if (included.contains(filename)) {
                return
            }
            included.add(filename)

            // Parse and run the script.
            Script included = shell.parse(file)
            included.metaClass = getMetaClass(included.class, shell)

            included.run()
        }

        meta.offset = { offset, Closure cl ->
//            println "Staring block at offest $offset"
            cl.delegate = new OffsetDelegate(offset, bindings, discriminators)
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl()
        }

        meta.bank = { bankNumber, Closure cl ->
            cl.delegate = new BankDelegate(bankNumber, bindings, discriminators)
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl()
        }

        meta.methodMissing = { String name, args ->
            if (args.size() == 0) {
                throw new Exception("Type definitions require a Closure initializer: ${name}.")
            }
            if (!(args[0] instanceof Closure)) {
                throw new Exception("Type definitions require a Closure initializer: ${name}.")
            }

            Closure cl = (Closure) args[0]
            cl.delegate = new DiscriminatorDelegate(offset)
            cl.delegate.name = name
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl()
            ++offset
            binding.setVariable(name, name)
            discriminators << cl.delegate
        }

        meta.initialize()
        return meta
    }
}
    /*
    static void usage() {
        println """
USAGE

java -jar discriminators-${Version.version}.jar [-html|-builder] /path/to/template /path/to/dsl"

Specifying the -groovy flag will cause the GroovyTemplateEngine to be
used. Otherwise the MarkupBuilderTemplateEngine will be used.

"""
    }
    static void main(args) {

        if (args[0] == '-version') {
            println()
            println "LAPPS Discriminator DSL v${Version.version}"
            println "Copyright 2014 American National Corpus"
            println()
            return
        }
        else {
            def argv = null
            def engineFactory = { template ->
                return new MarkupBuilderTemplateEngine(template)
            }
            int start = 0
            if (args[0] == '-html') {
                ++start
                engineFactory = { template ->
                    return new HtmlTemplateEngine(template)
                }
            }
            else if (args[0] == '-builder') {
                ++start
            }
            if (args.size() - start < 2) {
                usage()
                return
            }

            File template = new File(args[start++])
            if (!template.exists()) {
                throw new FileNotFoundException('Could not find template: ${template.path}')
            }
            File script = new File(args[start++])
            if (!script.exists()) {
                throw new FileNotFoundException("Could not dsl file: ${script.path}")
            }
            if (args.size() > start) {
                argv = args[start..-1]
            }
            TemplateEngine engine = engineFactory(template)

            DiscriminatorDsl dsl = new DiscriminatorDsl(engine)
            dsl.run(script, argv)
            println dsl.toHtml()
        }
    }

}
*/