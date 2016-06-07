package org.venutolo.compilestatic

class NoCompileStatic {

    void test() {
        ['a', 'b'].eachWithIndex {String s, int i -> println "$i: $s"}
    }

}
