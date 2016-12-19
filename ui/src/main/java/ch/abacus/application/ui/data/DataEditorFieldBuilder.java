package ch.abacus.application.ui.data;

import com.vaadin.ui.AbstractField;

import ch.abacus.application.ui.data.DataUtils.PropertyDefinitionBean;

public interface DataEditorFieldBuilder<TYPE> {

    AbstractField<TYPE> buildField(PropertyDefinitionBean fieldDefinition);
}
