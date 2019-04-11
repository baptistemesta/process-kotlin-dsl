/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.bonitasoft.engine.dsl.process

import com.bonitasoft.engine.dsl.process.DataType.Companion.string
import com.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.dataRef
import com.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.groovy
import com.winterbe.expekt.should
import org.awaitility.kotlin.await
import org.awaitility.kotlin.ignoreException
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.test.TestEngine
import org.bonitasoft.engine.test.TestEngineImpl
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object EngineIT : Spek({

    val testEngine: TestEngine = TestEngineImpl.getInstance()
    val apiClient = APIClient()
    var walter: User? = null

    beforeGroup {
        testEngine.start()
        apiClient.login("install", "install")
        walter = apiClient.identityAPI.createUser("walter.bates", "bpm")
        apiClient.logout()
        apiClient.login("walter.bates", "bpm")
        println("logged in")
    }
    afterGroup {
        try {

            testEngine.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    describe("A deployed process") {

        it("should deploy a process that works with connectors") {

            val process = process("MyProcess", "1.0") {
                initiator("john")
                data {
                    name = "myData"
                    type = string()
                    initialValue {
                        constant("myDataValue")
                    }
                }
                data {
                    name = "myOtherData"
                    type = string()
                    initialValue {
                        constant("myOtherDataValue")
                    }
                }
                data {
                    name = "connectorResult"
                    type = string()
                }
                automaticTask("taskWithOps") {
//                    connector {
//                        className = "com.acme.MyConnector"
//                        inputs {
//                            "input1" takes dataRef("myData")
//                            "input2" takes groovy("'toto'+myOtherData") {
//                                dataRef("myOtherData")
//                            }
//                        }
//                        outputs {
//                            "output1" saveToData "myOtherOutput"
//                            update("myOtherOutput").with(outputRef("output1"))
//                        }
//                    }
//                    connector {
//                        execute { input1: String?, input2: String? ->
//                            return@execute input1 + input2
//                        }
//                        inputs(
//                                dataRef("myData"),
//                                groovy("'toto'+myOtherData") {
//                                    dataRef("myOtherData")
//                                }
//                        )
//                        outputs {
//                            "result" saveToData "connectorResult"
//                        }
//                    }
                }

            }
            val businessArchive = process.export()
            val processDefinition = businessArchive.processDefinition
            val deploy = apiClient.processAPI.deploy(businessArchive)
            apiClient.processAPI.addUserToActor("john", deploy, walter!!.id)
            apiClient.processAPI.enableProcess(deploy.id)
            val case = apiClient.processAPI.startProcess(deploy.id)

            await ignoreException ArchivedProcessInstanceNotFoundException::class untilCallTo
                    { apiClient.processAPI.getFinalArchivedProcessInstance(case.id) } matches
                    { archivedCase -> archivedCase?.endDate != null }

            apiClient.processAPI.getArchivedProcessDataInstance("connectorResult", case.id)

        }
    }
})