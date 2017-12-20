package pl.springrest.converters;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * This class is implementation of AttributeConverter and allows convert
 * LocalDate to java.sql.Date that can be save in database and also convert
 * java.sql.Date to LocalDate
 * 
 * @see AttributeConverter
 */
@Converter(autoApply = true)
public class LocalDateConventer implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return Date.valueOf(localDate);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date dbData) {
		return dbData.toLocalDate();
	}

}