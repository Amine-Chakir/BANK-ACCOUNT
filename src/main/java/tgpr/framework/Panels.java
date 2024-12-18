package tgpr.framework;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;

public abstract class Panels {
    public static Panel verticalPanel(LinearLayout.Alignment alignment, Component... components) {
        var layout = LinearLayout.createLayoutData(alignment);
        var panel = new Panel().setLayoutManager(new LinearLayout(Direction.VERTICAL));
        for (var component : components)
            panel.addComponent(component, layout);
        return panel;
    }
}
