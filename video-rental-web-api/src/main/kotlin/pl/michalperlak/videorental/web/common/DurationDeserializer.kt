package pl.michalperlak.videorental.web.common

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.boot.jackson.JsonComponent
import java.time.Duration

@JsonComponent
class DurationDeserializer : JsonDeserializer<Duration>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Duration {
        val days = parser
            .readValueAs(String::class.java)
            .dropLast(1)
            .toLong()
        return Duration.ofDays(days)
    }
}