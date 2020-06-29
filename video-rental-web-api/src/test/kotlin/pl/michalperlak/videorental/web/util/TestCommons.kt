package pl.michalperlak.videorental.web.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response

val mapper: ObjectMapper = ObjectMapper()
    .registerKotlinModule()
    .findAndRegisterModules()

inline fun <reified T : Any> ExtractableResponse<Response>.extractBody(): T {
    val content = body().asString()
    return mapper.readValue(content, T::class.java)
}