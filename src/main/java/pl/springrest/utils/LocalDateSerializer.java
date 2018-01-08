package pl.springrest.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

	@Override
	public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeString(value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		
	}

}
