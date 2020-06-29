package pl.michalperlak.videorental.web.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import java.time.Duration

@JsonComponent
class DurationSerializer : JsonSerializer<Duration>() {
    override fun serialize(value: Duration, generator: JsonGenerator, serializers: SerializerProvider) {
        generator.writeString("${value.toDays()}D")
    }
}