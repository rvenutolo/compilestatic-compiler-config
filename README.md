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
groovyc --configscript config.groovy Demo.groovy
```
### Output
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
