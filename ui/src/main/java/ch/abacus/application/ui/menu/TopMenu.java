package ch.abacus.application.ui.menu;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

public interface TopMenu extends Component {

    void addMenuItem(String caption, Resource icon, String viewName);
}
