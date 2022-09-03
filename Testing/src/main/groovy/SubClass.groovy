import groovy.transform.CompileStatic
import org.codehaus.groovy.transform.GroovyASTTransformationClass

@CompileStatic
@GroovyASTTransformationClass('com')
@interface SubClass {}
