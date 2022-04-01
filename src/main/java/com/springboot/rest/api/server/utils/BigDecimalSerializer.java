package com.springboot.rest.api.server.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (bigDecimal != null) {
            jsonGenerator.writeString(bigDecimal.setScale(2, RoundingMode.HALF_DOWN) + "");
        } else {
            jsonGenerator.writeString(bigDecimal + "");
        }
    }
}
