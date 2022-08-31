//file:noinspection GrPackage
import com.matyrobbrt.enhancedgroovy.dsl.ClassTransformer
import com.matyrobbrt.enhancedgroovy.dsl.members.Annotation

final annotation = ((Annotation) this.annotation)
final transformer = ((ClassTransformer) this.transformer)

transformer.addField([
        'name': annotation.getAttribute('helloWorld').toString(),
        'type': "java.util.Optional<${transformer.className}>",
        'modifiers': ['public', 'static', 'final'],
        'doc': '/** This is a test */'
])