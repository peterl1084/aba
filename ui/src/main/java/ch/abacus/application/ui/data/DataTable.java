package ch.abacus.application.ui.data;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.server.data.DataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

import ch.abacus.application.common.repository.customer.FilterDefinition;
import ch.abacus.application.ui.data.DataUtils.PropertyDefinitionBean;

public class DataTable<DTO> extends Grid<DTO> {
	private static final long serialVersionUID = 8480919945389677558L;

	private DataProvider<DTO, FilterDefinition> dataProvider;

	@Autowired
	private I18N i18n;

	private Class<DTO> dtoType;

	@Autowired
	private EventBus.ViewEventBus eventBus;

	private HeaderRow filterRow;

	private FilterDefinition filterDef;

	DataTable(Class<DTO> dtoType) {
		setSizeFull();
		this.dtoType = dtoType;

		filterRow = addHeaderRowAt(0);
	}

	@PostConstruct
	protected void initialize() {
		filterDef = new FilterDefinition();
		eventBus.subscribe(this);
		dataProvider = GenericBeanResolver.resolveBean(DataProvider.class, dtoType, FilterDefinition.class).get();
		setupTableColumns(dtoType);
		setDataProvider(dataProvider.setFilter(filterDef));
	}

	@EventBusListenerMethod
	protected void onEditorSavedEvent(EditorSaveEvent e) {
		dataProvider.refreshAll();
	}

	private void setupTableColumns(Class<DTO> dtoType) {
		List<PropertyDefinitionBean> propertyDefinitions = DataUtils.findPropertyDefinitions(dtoType);

		propertyDefinitions.forEach(propertyDef -> {
			Column<DTO, String> column = null;
			column = addColumn(propertyDef.getPropertyName(), dto -> readPropertyValue(propertyDef.getGetter(), dto));

			if (propertyDef.isFilterable()) {
				TextField filterField = new TextField();
				filterField.addValueChangeListener(value -> onFilterChange(propertyDef.getPropertyName(), value));
				filterRow.getCell(propertyDef.getPropertyName()).setComponent(filterField);
			}
			column.setCaption(i18n.get(propertyDef.getTranslationKey()));
			column.setSortable(propertyDef.isSortable());
			if (propertyDef.isSortable()) {
				column.setSortProperty(propertyDef.getSortProperty());
			}
		});
	}

	private void onFilterChange(String propertyName, ValueChangeEvent<String> value) {
		if (!StringUtils.isEmpty(value.getValue())) {
			filterDef.setFilterParam(propertyName, value.getValue());
		} else {
			filterDef.removeFilterParam(propertyName);
		}

		dataProvider.refreshAll();
	}

	private String readPropertyValue(Method method, DTO dto) {
		try {
			return format(method.invoke(dto));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String format(Object propertyValue) {
		if (propertyValue == null) {
			return null;
		}

		Optional<DataTableConverter> converter = GenericBeanResolver.resolveBean(DataTableConverter.class,
				propertyValue.getClass());
		if (converter.isPresent()) {
			return converter.get().convertToString(getLocale(), propertyValue);
		}

		return propertyValue.toString();
	}

}
