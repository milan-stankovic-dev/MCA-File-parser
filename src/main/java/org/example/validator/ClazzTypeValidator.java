package org.example.validator;

import lombok.Getter;

import java.util.zip.DataFormatException;

@SuppressWarnings("unchecked")
public class ClazzTypeValidator {
    @Getter
    private static final ClazzTypeValidator instance = new ClazzTypeValidator();
    
    private ClazzTypeValidator() { }
    
    public <T> T returnValueIfOfType(Class<T> desiredType, Object value) throws DataFormatException {
        final Class<?> configValueClazz = value.getClass();

        if(configValueClazz != desiredType) {
            throw new DataFormatException("Found data under said key but data is of unknown class: "
                    + configValueClazz);
        }

        return (T) value;
    }
}
