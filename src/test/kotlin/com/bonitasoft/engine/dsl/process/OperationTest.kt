/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.bonitasoft.engine.dsl.process

import com.bonitasoft.engine.dsl.process.DataType.Companion.string
import com.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.dataRef
import com.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.groovy
import com.winterbe.expekt.should
import org.bonitasoft.engine.operation.OperatorType
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object OperationTest : Spek({

    describe("A task having a connector") {

        val process = process("MyProcess", "1.0") {
            data {
                name = "myData"
                type = string()
            }
            data {
                name = "myOtherData"
                type = string()
            }
            automaticTask("taskWithOps") {
                operations {
                    update("myData").with(groovy("'toto'"))
                    update("myOtherData").with(groovy("myData + 'toto'") {
                        dataRef("myData")
                    })
                }
            }

        }
        val processDefinition = process.export()

        it("should have the right name and version") {
            val task = processDefinition.flowElementContainer.getActivity("taskWithOps")
            task.operations.should.have.size(2)
            task.operations[0].leftOperand.name.should.equal("myData")
            task.operations[0].leftOperand.type.should.equal("DATA")
            task.operations[0].rightOperand.content.should.equal("'toto'")
            task.operations[0].type.should.equal(OperatorType.ASSIGNMENT)

            task.operations[1].leftOperand.name.should.equal("myOtherData")
            task.operations[1].leftOperand.type.should.equal("DATA")
            task.operations[1].rightOperand.content.should.equal("myData + 'toto'")
            task.operations[1].rightOperand.dependencies.should.have.size(1)
            task.operations[1].type.should.equal(OperatorType.ASSIGNMENT)
        }
    }
})