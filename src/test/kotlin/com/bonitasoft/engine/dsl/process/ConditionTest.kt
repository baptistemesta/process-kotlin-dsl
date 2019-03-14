package com.bonitasoft.engine.dsl.process

import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

/**
 * @author Danila Mazour
 */
object ConditionTest : Spek({

    describe("Verify that conditions are correctly created") {
        var condition = Condition()
        it("should have generated the correct expression from simple condition") {
            condition.groovy("return true")
            condition.toExpression().apply {
                returnType.should.equal("java.lang.Boolean")
                content.should.equal("return true")
                interpreter.should.equal("GROOVY")
                dependencies.should.be.empty
            }
        }
        it ("should generate groovy script expression with dependencies"){

        }
    }

})