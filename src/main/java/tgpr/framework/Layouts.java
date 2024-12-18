package tgpr.framework;

import com.googlecode.lanterna.gui2.LayoutData;
import com.googlecode.lanterna.gui2.LinearLayout;

public abstract class Layouts {
    public static final LayoutData LINEAR_BEGIN = LinearLayout.createLayoutData(LinearLayout.Alignment.Beginning);
    public static final LayoutData LINEAR_END = LinearLayout.createLayoutData(LinearLayout.Alignment.End);
    public static final LayoutData LINEAR_CENTER = LinearLayout.createLayoutData(LinearLayout.Alignment.Center);
    public static final LayoutData LINEAR_FILL = LinearLayout.createLayoutData(LinearLayout.Alignment.Fill);
}
