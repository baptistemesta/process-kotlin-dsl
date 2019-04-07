package com.bonitasoft.engine.dsl.process

import org.bonitasoft.engine.expression.Expression
import org.bonitasoft.engine.expression.ExpressionBuilder
import org.bonitasoft.engine.expression.ExpressionInterpreter
import org.bonitasoft.engine.expression.ExpressionType
import java.util.*

open class ExpressionDSLBuilder(val dataContainer: DataContainer) {

    private var type: ExpressionType? = null
    private var interpreter: String? = null
    private var content: String? = null
    private var returnType: String? = null
    private var dependenciesBuilder: DependenciesBuilder? = null

    open fun groovy(script: String) {
        type = ExpressionType.TYPE_READ_ONLY_SCRIPT
        interpreter = ExpressionInterpreter.GROOVY.name
        content = script
        returnType = "java.lang.String"
    }

    open fun groovy(script: String, init: DependenciesBuilder.() -> Unit) {
        groovy(script)
        dependenciesBuilder = DependenciesBuilder(dataContainer)
        dependenciesBuilder?.init()
    }

    fun dataRef(data: String) {
        type = ExpressionType.TYPE_VARIABLE
        content = data
    }

    fun constant(condition: Boolean) {
        type = ExpressionType.TYPE_CONSTANT
        returnType = "java.lang.Boolean"
        content = condition.toString()
    }

    fun constant(condition: String) {
        type = ExpressionType.TYPE_CONSTANT
        returnType = "java.lang.String"
        content = condition
    }
    fun groovy(script: String, type: String) {
        groovy(script)
        returnType = type
    }

    fun groovy(script: String, type: String, init: DependenciesBuilder.() -> Unit) {
        groovy(script, init)
        returnType = type
    }

    internal open fun build(): Expression {
        val builder = initBuilder()
        return builder.done()
    }

    internal fun initBuilder(): ExpressionBuilder {
        if (type == ExpressionType.TYPE_VARIABLE && content != null) {
            returnType = dataContainer.resolveData(content!!).type.type
        }

        val builder = ExpressionBuilder().createNewInstance(UUID.randomUUID().toString())
                .setReturnType(returnType)
                .setContent(content)
                .setExpressionType(type)
                .setInterpreter(interpreter)
        dependenciesBuilder?.build().apply { builder.setDependencies(this) }
        return builder
    }
}
