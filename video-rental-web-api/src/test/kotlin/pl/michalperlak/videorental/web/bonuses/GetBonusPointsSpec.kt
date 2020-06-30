package pl.michalperlak.videorental.web.bonuses

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import pl.michalperlak.videorental.web.rentals.addRental
import pl.michalperlak.videorental.web.util.extractBody
import java.util.UUID

@SpringBootTest(webEnvironment = RANDOM_PORT)
class GetBonusPointsSpec: StringSpec() {

    @LocalServerPort
    private var port: Int = 0

    init {
        "should return zero points when no rentals for customer" {
            val customerId = UUID.randomUUID().toString()
            Given {
                port(port)
            } When {
                get("${BonusPointsController.BONUS_POINTS_PATH}/$customerId")
            } Then {
                statusCode(200)
                val result = extract().extractBody<BonusPointsDto>()
                result.customerId shouldBe customerId
                result.points shouldBeExactly 0
            }
        }

        "should return points collected from rental" {
            val customerId = UUID.randomUUID().toString()
            addRental(port, customerId)
            Given {
                port(port)
            } When {
                get("${BonusPointsController.BONUS_POINTS_PATH}/$customerId")
            } Then {
                statusCode(200)
                val result = extract().extractBody<BonusPointsDto>()
                result.customerId shouldBe customerId
                result.points shouldBeGreaterThan  0
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(SpringListener)
}