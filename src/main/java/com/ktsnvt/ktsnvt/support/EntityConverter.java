package com.ktsnvt.ktsnvt.support;

import java.util.Collection;
import java.util.stream.Stream;

public interface EntityConverter<TFrom, TTo> extends org.springframework.core.convert.converter.Converter<TFrom, TTo> {
    Collection<TTo> convert(Collection<TFrom> from);

    Stream<TTo> convert(Stream<TFrom> from);
}
