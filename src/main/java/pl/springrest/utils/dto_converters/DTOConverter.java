package pl.springrest.utils.dto_converters;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

/**
 * This interface allows convert database objects to DTO or DTO to database
 * objects
 *
 * @param <S>
 *            source
 * @param <T>
 *            targer
 */
public interface DTOConverter<S, T> extends Converter<S, T> {

	/**
	 * Convert all elements in collection
	 * 
	 * @param elements
	 *            to convert
	 * @return list of elements converted to DTO or database objects
	 */
	default List<T> convertAll(Collection<S> elements) {
		return elements
				.stream()
				.map(e -> convert(e))
				.collect(Collectors.toList());
	}

	default Set<T> convertSet(Set<S> elements) {
		return elements
				.stream()
				.map(e -> convert(e))
				.collect(Collectors.toSet());
	}
}
