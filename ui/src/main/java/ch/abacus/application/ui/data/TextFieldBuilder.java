package ch.abacus.application.ui.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.TextField;

import ch.abacus.application.ui.data.DataUtils.PropertyDefinitionBean;

@SpringComponent
public class TextFieldBuilder implements DataEditorFieldBuilder<String> {

    @Autowired
    private I18N i18n;

    @Override
    public TextField buildField(PropertyDefinitionBean fieldDefinition) {
        return new TextField(i18n.get(fieldDefinition.getTranslationKey()));
    }
}
