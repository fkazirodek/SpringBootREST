package pl.springrest.dto_converter;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This interface allows convert database objects to DTO or DTO to database objects
 *
 * @param <F> convert from  
 * @param <T> convert to 
 */
public interface DTOConverter<F,T> {

	T convert(F from);
	
	/**
	 * Convert all elements in collection
	 * 
	 * @param elements to convert
	 * @return list of elements converted to DTO or database objects
	 */
	default Collection<T> convertAll(Collection<F> elements) {
		return elements.stream().map(e -> convert(e)).collect(Collectors.toList());
	}
}
