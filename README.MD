# EnhancedGroovy
EnhancedGroovy is an IntelliJ plugin which allows Groovy annotations to add their fields to your IDE.
## Dev usage
First, add the DSL as a compileOnly dependency to the project adding the annotations and their transformer:
```groovy
repositories {
    maven { url = 'https://maven.moddinginquisition.org/releases' }
}
dependencies {
    compileOnly "com.matyrobbrt.enhancedgroovy:dsl:$dslVersion" // Each plugin version has its own dsl version with the same ID
}
```
Now, in order to add support for annotations, in your `resources` folder create a GroovyScript named `enhancedgroovy/_${annotationName}.groovy`,
where `annotationName` is the name of the annotation you're adding support to.  
Example: given the annotation `testing.MyAnnotation`, the configuration script will be in `enhancedgroovy/_testing.MyAnnotation.groovy`.  
You can ignore the package warning, adding this at the top of your script:
```groovy
//file:noinspection GrPackage
```
When found, the plugin will execute your script, by giving it the `transformer` as a `com.matyrobbrt.enhancedgroovy.dsl.ClassTransformer` and
the `annotation` in question as a `com.matyrobbrt.enhancedgroovy.dsl.members.Annotation`. Those properties can be accessed using the `this.<propertyName>`
syntax, and for better IDE support you may uselessly cast them to their respective types.  
The example below will add a `private final java.lang.String testingField` field when applied to a class:
```groovy
import com.matyrobbrt.enhancedgroovy.dsl.ClassTransformer

final transformer = ((ClassTransformer) this.transformer)

transformer.addField([
        'name': 'testingField',
        'type': 'java.lang.String',
        'modifiers': ['private', 'final']
])
```
