package ch.abacus.application.ui.data;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import ch.abacus.application.common.data.ColumnDefinition;
import ch.abacus.application.common.data.FieldDefinition;

public class DataUtils {

	/**
	 * Finds all properties from given DTO annotated with @ColumnDefinition
	 * 
	 * @param dtoType
	 * @return List of property definitions
	 */
	public static <DTO> List<PropertyDefinitionBean> findPropertyDefinitions(Class<DTO> dtoType) {
		Map<Method, ColumnDefinition> columnDefinitionProperties = Arrays.asList(dtoType.getDeclaredMethods()).stream()
				.filter(method -> method.isAnnotationPresent(ColumnDefinition.class))
				.collect(Collectors.toMap(Function.identity(), method -> method.getAnnotation(ColumnDefinition.class)));

		return columnDefinitionProperties.entrySet().stream()
				.map(entry -> new PropertyDefinitionBean(entry.getKey(), entry.getValue().translationKey(),
						entry.getValue().order(), entry.getValue().sortProperty(), entry.getValue().filterable()))
				.sorted().collect(Collectors.toList());
	}

	/**
	 * Finds all field definitions from given DTO annotated
	 * with @FieldDefinition
	 * 
	 * @param dtoType
	 * @return List of field definitions
	 */
	public static <DTO> List<PropertyDefinitionBean> findFieldDefinitions(Class<DTO> dtoType) {
		List<Method> fieldMethods = Arrays.asList(dtoType.getDeclaredMethods()).stream()
				.filter(method -> method.isAnnotationPresent(FieldDefinition.class)).collect(Collectors.toList());

		List<PropertyDefinitionBean> propertyDefinitions = new LinkedList<>();
		for (Method method : fieldMethods) {
			FieldDefinition fieldDefintion = method.getAnnotation(FieldDefinition.class);
			propertyDefinitions.add(new PropertyDefinitionBean(method, fieldDefintion.translationKey(),
					fieldDefintion.order(), null, false));
		}

		return propertyDefinitions;
	}

	public static class PropertyDefinitionBean implements Comparable<PropertyDefinitionBean> {
		private final String propertyName;
		private final Class<?> propertyType;
		private final String translationKey;
		private final int order;
		private final Method getter;
		private String sortProperty;
		private boolean filterable;

		public PropertyDefinitionBean(Method getter, String translationKey, int order, String sortProperty,
				boolean filterable) {
			this.propertyName = parsePropertyName(getter);
			this.propertyType = getter.getReturnType();
			this.translationKey = translationKey;
			this.order = order;
			this.getter = getter;
			this.sortProperty = sortProperty;
			this.filterable = filterable;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public Class<?> getPropertyType() {
			return propertyType;
		}

		public String getTranslationKey() {
			return translationKey;
		}

		public int getOrder() {
			return order;
		}

		public Method getGetter() {
			return getter;
		}

		public boolean isFilterable() {
			return filterable;
		}

		@Override
		public int compareTo(PropertyDefinitionBean other) {
			return this.order - other.order;
		}

		public boolean isSortable() {
			return !StringUtils.isEmpty(sortProperty);
		}

		public String getSortProperty() {
			return sortProperty;
		}

		private static String parsePropertyName(Method method) {
			return Introspector.decapitalize(method.getName().replace("set", "").replace("get", "").replace("is", ""));
		}
	}
}
