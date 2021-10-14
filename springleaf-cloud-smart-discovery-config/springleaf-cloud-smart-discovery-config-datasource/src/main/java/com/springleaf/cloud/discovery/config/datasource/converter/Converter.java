package com.springleaf.cloud.discovery.config.datasource.converter;

public interface Converter<S, T> {
    T convert(S var1);
}