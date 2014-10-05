package org.lappsgrid.discriminator.dsl

/**
 * @author Keith Suderman
 */
class OffsetDelegate {
    List discriminators
    Binding binding
    long offset

    public OffsetDelegate(long offset, Binding binding, List discriminators) {
//        println "OffsetDelegate for offset ${offset}"
        this.discriminators = discriminators
        this.binding = binding
        this.offset = offset
    }

    void all(Closure cl) {
//        println "Calling the any method."
        cl.delegate = new DiscriminatorDelegate(offset)
        cl.delegate.name = 'any'
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        ++offset
        binding.setVariable('any', 'any')
        discriminators << cl.delegate
    }

    void methodMissing(String name, args) {
//        println "Missing method ${name}"
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
//        println "Discriminator $offset $name"
        ++offset
        binding.setVariable(name, name)
        discriminators << cl.delegate
    }
}
