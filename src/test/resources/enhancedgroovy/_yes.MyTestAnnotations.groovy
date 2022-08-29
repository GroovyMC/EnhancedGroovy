//file:noinspection GrPackage
import com.matyrobbrt.enhancedgroovy.dsl.ClassTransformer

((ClassTransformer) this.transformer).with {
    it.addField("helloThere", "java.lang.Integer")
    it.addField("yep", "java.util.Optional<java.lang.String>")
}