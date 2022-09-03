package com.matyrobbrt.enhancedgroovy.plugin.asttassistance.inspection

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.codeInspection.GroovyLocalInspectionTool
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrCallExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod
import org.jetbrains.plugins.groovy.lang.psi.controlFlow.impl.ArgumentsInstruction

class ASTTCallInitInspection : GroovyLocalInspectionTool() {

    override fun buildGroovyVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): GroovyElementVisitor {
        return object : GroovyElementVisitor() {
            override fun visitMethod(method: GrMethod) {
                if (method.name == "visit" && method.parameterList.parameters.size == 2 && method.containingClass?.extendsList?.referencedTypes?.any {
                        it.resolve()?.qualifiedName == "org.codehaus.groovy.transform.AbstractASTTransformation"
                    } == true) {
                    val flow = method.block?.statements ?: arrayOf()
                    val firstCall = flow.firstOrNull { it is GrCallExpression }
                    if (firstCall != null) {
                        val insn = firstCall as GrCallExpression
                        val resolvedMethod = insn.resolveMethod()
                        if (resolvedMethod == null || !(resolvedMethod.name == "init" && resolvedMethod.parameterList.parameters.size == 2)) {
                            holder.registerProblem(
                                insn,
                                "Transformers extending AbstractASTTransformation should call init as the first statement in the visit method.",
                                QuickFix(method, flow[0])
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getDisplayName(): String {
        return "Missing init call"
    }
    override fun getGroupDisplayName(): String {
        return "EnhancedGroovy"
    }
    override fun getGroupKey(): String {
        return "inspection.bugs"
    }
    override fun getStaticDescription(): String {
        return "Reports if ASTTransformers call init"
    }

    private class QuickFix(private val method: GrMethod, private val stmt: GrStatement) : LocalQuickFix {
        override fun getFamilyName(): String {
            return "Add init call"
        }

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val params = method.parameterList.parameters
            val initStmt = GroovyPsiElementFactory.getInstance(method.project)
                .createStatementFromText("this.init(${params[0].name}, ${params[1].name})")
            method.block?.addStatementBefore(initStmt, stmt)
        }

    }
}