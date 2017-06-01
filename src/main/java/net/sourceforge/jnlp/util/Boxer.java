package net.sourceforge.jnlp.util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Arrays;

import static javax.swing.BorderFactory.*;

/**
 * User: alexkasko
 * Date: 5/31/17
 */
public class Boxer {

    public static final int HORIZONTAL_SPACER_WIDTH = 5;
    public static final int VERTICAL_SPACER_HEIGHT = 12;
    public static final int HORIZONTAL_ENDER_WIDTH = 12;

    private static final java.util.List<Color> COLORS = Arrays.asList(
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
            Color.PINK, Color.ORANGE, Color.MAGENTA, Color.CYAN);

    public static JPanel createTopBox(String title, Border border) {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.X_AXIS);
        jp.setLayout(bl);
        jp.add(Box.createHorizontalGlue());
        jp.setBorder(createTitledBorder(border, title));
        return jp;
    }

    public static JPanel createBottomBox(Component content) {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.X_AXIS);
        jp.setLayout(bl);
        jp.add(Box.createHorizontalGlue());
        jp.setBorder(createCompoundBorder(
                createMatteBorder(1, 0, 0, 0, jp.getBackground().darker()),
                createEmptyBorder(5, 0, 0, 0)));
        jp.add(content);
        return jp;
    }

    public static int equaliseWidth(Component... children) {
        int widthMin = 0;
        int widthPref = 0;
        int widthMax = 0;
        for (Component child : children) {
            if (child.getMinimumSize().getWidth() > widthMin) {
                widthMin = child.getMinimumSize().width;
            }
            if (child.getPreferredSize().getWidth() > widthPref) {
                widthPref = child.getPreferredSize().width;
            }
            if (child.getMaximumSize().getWidth() > widthMax) {
                widthMax = child.getMaximumSize().width;
            }
        }
        for (Component child : children) {
            child.setMinimumSize(new Dimension(widthMin, child.getMinimumSize().height));
            child.setPreferredSize(new Dimension(widthPref, child.getPreferredSize().height));
            child.setMaximumSize(new Dimension(widthMax, child.getMaximumSize().height));
        }
        return widthPref;
    }

    public static void highlightBorders(Component parent) {
        JComponent jparent = (JComponent) parent;
        int i = 0;
        for (Component child : jparent.getComponents()) {
            if (child instanceof  JComponent) {
                JComponent jchild = (JComponent) child;
                highlightBorders(jchild);
                if (child instanceof JPanel /* || child instanceof JButton */) {
                    Color co = COLORS.get(i % COLORS.size());
                    i += 1;
                    jchild.setBorder(createCompoundBorder(createMatteBorder(1, 1, 1, 1, co), jchild.getBorder()));
                    jchild.setToolTipText(jchild.getSize().width + "," + jchild.getSize().height);
                }
            }
        }
    }

    public static Box.Filler slimHorizontalStrut() {
        return slimHorizontalStrut(HORIZONTAL_SPACER_WIDTH);
    }

    public static Box.Filler slimHorizontalStrut(int width) {
        return new Box.Filler(new Dimension(width, 0), new Dimension(width, 0),
                new Dimension(width, 0));
    }

    public static JPanel horizontalPanel() {
        return horizontalPanel(null);
    }

    public static JPanel horizontalPanel(JComponent parent) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);
        if (null != parent) {
            parent.add(panel);
        }
        return panel;
    }

    public static JPanel verticalPanel() {
        return verticalPanel(null);
    }

    public static JPanel verticalPanel(JComponent parent) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        if (null != parent) {
            parent.add(panel);
        }
        return panel;
    }

}
