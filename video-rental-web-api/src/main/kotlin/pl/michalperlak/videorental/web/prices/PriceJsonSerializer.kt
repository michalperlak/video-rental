package pl.michalperlak.videorental.web.prices

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import pl.michalperlak.videorental.pricing.api.Price

@JsonComponent
class PriceJsonSerializer : JsonSerializer<Price>() {
    override fun serialize(value: Price, generator: JsonGenerator, serializers: SerializerProvider) {
        generator.writeString(value.format())
    }
}