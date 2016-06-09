I have run into an issue when adding the `CompileStatic` AST transformation to a class via the Groovy compiler `configscript` argument.

### `Demo.groovy`
```groovy
class Demo {
    void test() {
        ['a', 'b'].eachWithIndex {String s, int i -> println "$i: $s"}
    }
}
```
### `config.groovy`
```groovy
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ClassNode

withConfig(configuration) {
    source(classValidator: {ClassNode classNode -> classNode.nameWithoutPackage == 'Demo'}) {
        ast(CompileStatic)
    }
}
```
### Compile using `--configscript`
```
$ groovyc --configscript config.groovy Demo.groovy
```
### Output (abbreviated)
```
>>> a serious error occurred: BUG! exception in phase 'instruction selection' in source unit 'Demo.groovy' unexpected NullpointerException
>>> stacktrace:
BUG! exception in phase 'instruction selection' in source unit 'Demo.groovy' unexpected NullpointerException
	at org.codehaus.groovy.control.CompilationUnit.applyToPrimaryClassNodes(CompilationUnit.java:1058)
	at org.codehaus.groovy.control.CompilationUnit.doPhaseOperation(CompilationUnit.java:591)
	at org.codehaus.groovy.control.CompilationUnit.processPhaseOperations(CompilationUnit.java:569)
	...
Caused by: java.lang.NullPointerException
	at org.codehaus.groovy.control.ClassNodeResolver.tryAsLoaderClassOrScript(ClassNodeResolver.java:180)
	at org.codehaus.groovy.control.ClassNodeResolver.findClassNode(ClassNodeResolver.java:170)
	at org.codehaus.groovy.control.ClassNodeResolver.resolveName(ClassNodeResolver.java:126)
  ...
```

### Notes
  * Groovy & JVM versions: `Groovy Version: 2.4.7 JVM: 1.8.0_92 Vendor: Oracle Corporation OS: Linux`
    * Have tried Groovy versions going back to 2.4.3 and Oracle JVM 1.7.0_79
  * Changing any of the following will result in no compilation error:
    * Remove the `source(classValidator: ...){ }` code around `ast`, leaving `ast(CompileStatic)`, in the compiler config script
    * Replace the `eachWithIndex` call in `Demo` with `each`
    * Annotate `Demo` with `@CompileStatic` and compile without `--configscript`
  * This repo contains a bit more thorough demo of the issue.
    * You can do a Maven build with `$ mvn clean compile` and it will fail
    * You can run `$ ./compile.sh` to compile three times
      * Once with no `--configscript`, which succeeds
      * Once with `--configscript` [applying `CompileStatic` to all classes](https://github.com/rvenutolo/compilestatic-compiler-config/blob/master/src/conf/alwaysApplyCompileStatic.groovy), which succeeds
      * Once with `--configscript` [selectively applying `CompileStatic` to only one class](https://github.com/rvenutolo/compilestatic-compiler-config/blob/master/src/conf/selectivelyApplyCompileStatic.groovy), which fails
    * There are three class
      * [ConfigAddsCompileStatic](https://github.com/rvenutolo/compilestatic-compiler-config/blob/master/src/main/groovy/org/venutolo/compilestatic/ConfigAddsCompileStatic.groovy), which is not annotated with `CompileStatic`, and is the class I want to add `CompileStatic` to
      * [HasCompileStatic](https://github.com/rvenutolo/compilestatic-compiler-config/blob/master/src/main/groovy/org/venutolo/compilestatic/HasCompileStatic.groovy), which is a class already annotated with `CompileStatic`
      * [NoCompileStatic](https://github.com/rvenutolo/compilestatic-compiler-config/blob/master/src/main/groovy/org/venutolo/compilestatic/NoCompileStatic.groovy), which is not anotated with `CompileStatic`, nor do I want to add `CompileStatic` to it
