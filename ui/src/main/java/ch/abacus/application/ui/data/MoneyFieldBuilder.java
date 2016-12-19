package ch.abacus.application.ui.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.spring.annotation.SpringComponent;

import ch.abacus.application.common.data.Money;
import ch.abacus.application.ui.component.MoneyField;
import ch.abacus.application.ui.data.DataUtils.PropertyDefinitionBean;

@SpringComponent
public class MoneyFieldBuilder implements DataEditorFieldBuilder<Money> {

    @Autowired
    private I18N i18n;

    @Override
    public MoneyField buildField(PropertyDefinitionBean fieldDefinition) {
        return new MoneyField(i18n.get(fieldDefinition.getTranslationKey()));
    }
}
