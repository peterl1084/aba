package ch.abacus.application.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalSplitPanel;

import ch.abacus.application.ui.menu.TopMenu;

@SpringViewDisplay
public class DevDayViewDisplay extends VerticalSplitPanel implements ViewDisplay {
    private static final long serialVersionUID = 6924790682628024336L;

    @Autowired
    private TopMenu menu;

    public DevDayViewDisplay() {
        setSizeFull();

        setSplitPosition(50, Unit.PIXELS);
        setLocked(true);
    }

    @PostConstruct
    protected void initialize() {
        setFirstComponent(menu);
    }

    @Override
    public void showView(View view) {
        setSecondComponent((Component) view);
    }
}
