import groovy.transform.CompileStatic
import yes.MyTestAnnotations

@CompileStatic
@MyTestAnnotations(helloWorld = 'helloThere')
class TestingThing {
    /**
     * HIII
     */
    private final String myField = '12'
    TestingThing() {
        println helloThere
    }
}
