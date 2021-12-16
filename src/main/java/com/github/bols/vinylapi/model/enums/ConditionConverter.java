package com.github.bols.vinylapi.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ConditionConverter implements AttributeConverter<Condition,  String> {
    @Override
    public String convertToDatabaseColumn(Condition condition) {
        return condition != null ? condition.getName() : null;
    }

    @Override
    public Condition convertToEntityAttribute(String s) {
        if (s == null){
            return null;
        }

        return Stream.of(Condition.values())
                .filter(c -> c.getName().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
