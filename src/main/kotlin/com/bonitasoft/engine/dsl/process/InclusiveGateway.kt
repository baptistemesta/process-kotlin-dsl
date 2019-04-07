package com.bonitasoft.engine.dsl.process

import org.bonitasoft.engine.bpm.flownode.GatewayType
import org.bonitasoft.engine.bpm.process.impl.FlowElementContainerBuilder
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder

class InclusiveGateway(parent: DataContainer, name: String) : FlowNode(parent, name) {
    override fun buildFlowNode(builder: ProcessDefinitionBuilder): FlowElementContainerBuilder {
        return builder.addGateway(name, GatewayType.PARALLEL)
    }
}