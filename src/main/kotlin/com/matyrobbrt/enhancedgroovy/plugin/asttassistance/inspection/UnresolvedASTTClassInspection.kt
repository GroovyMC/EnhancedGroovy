package com.matyrobbrt.enhancedgroovy.plugin.asttassistance.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import com.matyrobbrt.enhancedgroovy.plugin.asttassistance.reference.ASTTReferenceContributor
import com.matyrobbrt.enhancedgroovy.plugin.util.implements

class UnresolvedASTTClassInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element is PsiAnnotation && element.qualifiedName == ASTTReferenceContributor.asttAnnotationName) {
                    val clazzAttr = element.findAttributeValue("value")
                    if (clazzAttr != null && clazzAttr is PsiLiteral) {
                        val clazz = JavaPsiFacade.getInstance(element.project)
                            .findClass(clazzAttr.value.toString(), element.resolveScope)
                        if (clazz == null) {
                            holder.registerProblem(
                                clazzAttr,
                                "Cannot resolve class ${clazzAttr.value}",
                                ProblemHighlightType.LIKE_UNKNOWN_SYMBOL
                            )
                        } else if (!implements(clazz, "org.codehaus.groovy.transform.ASTTransformation")) {
                            holder.registerProblem(
                                clazzAttr,
                                "Class ${clazzAttr.value} is not a transformer",
                                ProblemHighlightType.LIKE_UNKNOWN_SYMBOL
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getDisplayName(): String {
        return "Unknown transformer"
    }

    override fun getGroupDisplayName(): String {
        return "EnhancedGroovy"
    }

    override fun getGroupKey(): String {
        return "inspection.bugs"
    }

    override fun getStaticDescription(): String {
        return "Reports if unknown transformers are attempted to be used"
    }
}