package io.mservice.todo;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.springframework.beans.factory.annotation.Value;

@Converter(autoApply = true)
public class LocaleConverter implements AttributeConverter<OffsetDateTime, OffsetDateTime> {

	private final ZoneId LOCALE;

	public LocaleConverter(@Value("${locale.zone}") /* Asia/Kolkata */ String zoneId) {
		LOCALE = ZoneId.of(zoneId);
	}

	@Override
	public OffsetDateTime convertToDatabaseColumn(OffsetDateTime attribute) {
		return attribute != null ? attribute.atZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime() : null;
	}

	@Override
	public OffsetDateTime convertToEntityAttribute(OffsetDateTime dbData) {
		return dbData != null ? dbData.atZoneSameInstant(LOCALE).toOffsetDateTime() : null;
	}
}
