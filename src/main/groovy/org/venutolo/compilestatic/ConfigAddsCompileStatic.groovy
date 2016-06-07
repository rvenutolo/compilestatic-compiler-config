package org.venutolo.compilestatic

class ConfigAddsCompileStatic {

    void test() {
        ['a', 'b'].eachWithIndex {String s, int i -> println "$i: $s"}
    }

}
