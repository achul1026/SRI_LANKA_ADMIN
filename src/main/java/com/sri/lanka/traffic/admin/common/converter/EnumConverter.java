package com.sri.lanka.traffic.admin.common.converter;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;

@Converter
public abstract class EnumConverter<T extends Enum<T> & CommonEnumType<E>, E> implements AttributeConverter<T, E> {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        T[] enums = clazz.getEnumConstants();

        for (T e : enums) {
            if (e.getCode().equals(dbData)) {
                return e;
            }
        }

        throw new UnsupportedOperationException();
    }
}