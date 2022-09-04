import groovy.transform.CompileStatic
import org.codehaus.groovy.transform.GroovyASTTransformationClass

@CompileStatic
@GroovyASTTransformationClass('java/lang.String')
@interface SubClass {

}
