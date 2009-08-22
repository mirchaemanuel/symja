package org.matheclipse.core.convert;

import java.util.HashMap;
import java.util.Map;

public abstract class Converter<S, T> {
	private final static Map<Class, Map<Class, Converter>> SOURCE_TO_TARGETS_MAP = new HashMap<Class, Map<Class, Converter>>();

	/**
	 * Add a converter to the internal converter map
	 * 
	 * @param converter
	 */
	public static void add(Converter converter) {
		Class sourceType = converter.getSourceType();
		Class targetType = converter.getTargetType();

		Map<Class, Converter> type2ClassMap = getConverterMap(sourceType);

		if (type2ClassMap.containsKey(targetType)) {
			throw new IllegalArgumentException(
					"Conversion rule already registered: " + sourceType
							+ " => " + targetType);
		}
		type2ClassMap.put(targetType, converter);
	}

	/**
	 * Converts the source object into an object of the target type
	 * 
	 * @throws ConversionException
	 */
	public static <S, T> T convert(final S source, final Class targetType)
			throws ConversionException {
		return convert(source, source.getClass(), targetType);
	}

	/**
	 * Converts the source object into an object of the target type
	 * 
	 * @throws ConversionException
	 */
	public static <S, T> T convert(final S source, final Class sourceType,
			final Class targetType) throws ConversionException {
		Map<Class, Converter> type2ClassMap = getConverterMap(sourceType);

		Converter conv = type2ClassMap.get(targetType);
		if (conv == null) {
			throw new ConversionException("No conversion rule found: "
					+ sourceType + " => " + targetType);
		}
		return (T) conv.convert(source);
	}

	private static Map<Class, Converter> getConverterMap(Class sourceType) {
		Map<Class, Converter> type2ClassMap;
		type2ClassMap = SOURCE_TO_TARGETS_MAP.get(sourceType);
		if (type2ClassMap == null) {
			type2ClassMap = new HashMap<Class, Converter>();
			SOURCE_TO_TARGETS_MAP.put(sourceType, type2ClassMap);
		}
		return type2ClassMap;
	}

	private final Class fSourceClass;

	private final Class fTargetClass;

	/**
	 * Create a converter and add it to the internal converter map
	 * 
	 * @param sType
	 * @param tType
	 */
	public Converter(final Class sType, final Class tType) {
		fSourceClass = sType;
		fTargetClass = tType;
	}

	/**
	 * Converts the source object into an object of the target type
	 * 
	 * @throws ConversionException
	 */
	abstract public T convert(S source) throws ConversionException;

	public Class getSourceType() {
		return fSourceClass;
	}

	public Class getTargetType() {
		return fTargetClass;
	}

}
