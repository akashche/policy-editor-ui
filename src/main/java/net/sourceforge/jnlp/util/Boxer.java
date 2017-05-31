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
        content.setMaximumSize(content.getMinimumSize());
        content.setPreferredSize(content.getMinimumSize());
        jp.add(content);
        return jp;
    }

    public static void equaliseWidth(JComponent parent, Class<? extends JComponent> childClass) {
        int widthMin = 0;
        int widthPref = 0;
        int widthMax = 0;
        for (Component child : parent.getComponents()) {
            if (!(childClass.isAssignableFrom(child.getClass()))) continue;
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
        for (Component child : parent.getComponents()) {
            if (!(childClass.isAssignableFrom(child.getClass()))) continue;
            child.setMinimumSize(new Dimension(widthMin, child.getMinimumSize().height));
            child.setPreferredSize(new Dimension(widthPref, child.getPreferredSize().height));
            child.setMaximumSize(new Dimension(widthMax, child.getMaximumSize().height));
        }
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
                    jchild.setBorder(createCompoundBorder(createMatteBorder(1,1,1,1, co), jchild.getBorder()));
                    jchild.setToolTipText(jchild.getSize().width + "," + jchild.getSize().height);
                }
            }
        }
    }
}
