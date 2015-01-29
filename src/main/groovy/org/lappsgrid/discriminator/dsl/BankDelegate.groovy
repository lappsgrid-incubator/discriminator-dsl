package org.lappsgrid.discriminator.dsl

/**
 * Discriminators are organized into banks of 1024.  This permits banks of
 * discriminators to be reserved for organizations or uses while leaving room
 * for future discriminators to be added.
 *
 * @author Keith Suderman
 */
class BankDelegate {
    // The number of discriminators per bank.
    public static final int BANK_SIZE = 1024

    List discriminators
    Binding binding
    long offset

    public BankDelegate(long bankNumber, Binding binding, List discriminators) {
//        println "OffsetDelegate for offset ${offset}"
        this.discriminators = discriminators
        this.binding = binding
        this.offset = bankNumber * BANK_SIZE
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
