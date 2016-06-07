import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ClassNode

withConfig(configuration) {
    source(
        classValidator: {ClassNode classNode ->
            boolean applyCompileStatic = classNode.nameWithoutPackage == 'ConfigAddsCompileStatic'
            if (applyCompileStatic) {
                println("Applying @CompileStatic to $classNode")
            }
            applyCompileStatic
        }
    ) {
        ast(CompileStatic)
    }
}
