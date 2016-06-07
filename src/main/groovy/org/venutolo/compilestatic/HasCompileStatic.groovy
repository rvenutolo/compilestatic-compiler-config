package org.venutolo.compilestatic

import groovy.transform.CompileStatic

@CompileStatic
class HasCompileStatic {

    void test() {
        ['a', 'b'].eachWithIndex {String s, int i -> println "$i: $s"}
    }

}
