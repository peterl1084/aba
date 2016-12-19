package ch.abacus.application.ui.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.vaadin.spring.events.EventBus;

import com.vaadin.data.BeanBinder;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ch.abacus.application.ui.data.DataUtils.PropertyDefinitionBean;

public class DataEditor<DTO> extends VerticalLayout {
    private static final long serialVersionUID = -48372911746378115L;

    private BeanBinder<DTO> binder;

    @Autowired
    private ApplicationContext appContext;

    private Class<DTO> dtoType;

    private List<AbstractField> boundFields;

    private DataPersister<DTO> dataPersister;

    private FormLayout formLayout;

    private DTO value;

    private Button save;

    private Button discard;

    @Autowired
    private EventBus.ViewEventBus eventBus;

    public DataEditor(Class<DTO> dtoType) {
        this.dtoType = dtoType;

        setWidth(350, Unit.PIXELS);

        boundFields = new ArrayList<>();
        binder = new BeanBinder<>(dtoType);
        formLayout = new FormLayout();
        formLayout.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        save = new Button("Save", e -> onSaveClicked());
        save.setEnabled(false);
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);

        discard = new Button("Discard", e -> onDiscard());
        discard.setEnabled(false);
        discard.addStyleName(ValoTheme.BUTTON_DANGER);

        buttonLayout.addComponents(save, discard);

        addComponents(formLayout, buttonLayout);
        setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
    }

    @PostConstruct
    protected void initialize() {
        dataPersister = GenericBeanResolver.resolveBean(DataPersister.class, dtoType).get();
        buildFields(DataUtils.findFieldDefinitions(dtoType));
    }

    private void onSaveClicked() {
        binder.writeBeanIfValid(value);
        save.setEnabled(false);
        discard.setEnabled(false);
        dataPersister.store(value);
        binder.removeBean();
        eventBus.publish(this, new EditorSaveEvent());
    }

    private void onDiscard() {
        binder.removeBean();
        boundFields.forEach(field -> field.setValue(field.getEmptyValue()));
        save.setEnabled(false);
        discard.setEnabled(false);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void buildFields(List<PropertyDefinitionBean> fieldDefinitions) {
        fieldDefinitions.forEach(fieldDef -> {
            Optional<DataEditorFieldBuilder> fieldBuilder = findFieldBuilder(fieldDef);
            if (fieldBuilder.isPresent()) {
                AbstractField field = fieldBuilder.get().buildField(fieldDef);
                formLayout.addComponent(field);
                field.setWidth(100, Unit.PERCENTAGE);
                binder.forField(field).bind(fieldDef.getPropertyName());
                boundFields.add(field);
            }
        });
    }

    public void setDTOToEdit(DTO value) {
        this.value = value;

        if (Optional.ofNullable(value).isPresent()) {
            binder.readBean(value);
            save.setEnabled(true);
            discard.setEnabled(true);
        } else {
            binder.removeBean();

            save.setEnabled(false);
            discard.setEnabled(false);
        }
    }

    @SuppressWarnings("rawtypes")
    private Optional<DataEditorFieldBuilder> findFieldBuilder(PropertyDefinitionBean fieldDefinition) {
        ResolvableType type = ResolvableType.forClassWithGenerics(DataEditorFieldBuilder.class,
                fieldDefinition.getPropertyType());
        List<String> builderBeans = Arrays.asList(appContext.getBeanNamesForType(type));
        if (builderBeans.size() != 1) {
            throw new RuntimeException("Did not find builder for " + fieldDefinition.getPropertyName() + " of type "
                    + fieldDefinition.getPropertyType().getSimpleName());
        }

        try {
            return Optional.of(appContext.getBean(builderBeans.iterator().next(), DataEditorFieldBuilder.class));
        } catch (NoSuchBeanDefinitionException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
