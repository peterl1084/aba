package ch.abacus.application.ui;

import com.vaadin.navigator.View;

public interface NavigationManager {

    void navigateTo(View view);

    void navigateTo(String viewName);
}
