package com.codeit.mini.config;

import com.codeit.mini.entity.comm.VendingType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false) // 명시적으로 적용할 경우 false
public class VendingTypeConverter implements AttributeConverter<VendingType, String> {

    @Override
    public String convertToDatabaseColumn(VendingType attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public VendingType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return VendingType.fromType(dbData);
    }
}
