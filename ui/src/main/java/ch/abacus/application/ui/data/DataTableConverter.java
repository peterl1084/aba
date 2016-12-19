package ch.abacus.application.ui.data;

import java.util.Locale;

public interface DataTableConverter<MODEL> {

    public String convertToString(Locale locale, MODEL value);
}
