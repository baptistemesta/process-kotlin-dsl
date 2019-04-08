/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.bonitasoft.engine.dsl.process

import com.bonitasoft.engine.dsl.process.DataType.Companion.string
import com.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.dataRef
import com.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.groovy
import com.winterbe.expekt.should
import org.bonitasoft.engine.bpm.bar.BusinessArchiveFactory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File

object ConnectorTest : Spek({

    describe("A task having a connector") {

        val process = process("MyProcess", "1.0") {

            initiator("john")
            data {
                name = "myData"
                type = string()
            }
            data {
                name = "myOtherData"
                type = string()
            }
            automaticTask("taskWithOps") {
                connector{
//                    restCall(groovy("my url build")) saveToData "restCallresult"
//                    connectorClass = MyConnector::class
                    inputs {
                        "input1" takes dataRef("myData")
                        "input2" takes groovy("myData+myOtherData") {
                            dataRef("myOtherData")
                            dataRef("myData")
                        }
                    }
                    outputs {
                        "output1" saveToData "myOtherOutput"
                        update("myOtherOutput").with(outputRef("output1"))
                    }
                }
//                connector {
//                    execute { input1:String?, input2:String? ->
//                        return@execute input1+input2
//                    }
//                    inputs(
//                        dataRef("myData"),
//                        groovy("'toto'+myOtherData") {
//                            dataRef("myOtherData")
//                        }
//                    )
//                    outputs {
//                        "result" saveToData "myOtherOutput"
//                    }
//                }
            }

        }
        val businessArchive = process.export()
        val processDefinition = businessArchive.processDefinition

//        BusinessArchiveFactory.writeBusinessArchiveToFile(businessArchive, File("/Users/baptiste/git/process-kotlin-dsl/test.bar"))
        it("should have the right name and version") {
            val task = processDefinition.flowElementContainer.getActivity("taskWithOps")
//            task.connectors.should.have.size(1)

        }
    }
})
