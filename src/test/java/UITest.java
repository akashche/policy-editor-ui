import net.sourceforge.jnlp.security.policyeditor.PolicyEditor;
import net.sourceforge.jnlp.util.Boxer;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.BorderFactory.*;
import static javax.swing.BorderFactory.createMatteBorder;

/**
 * User: alexkasko
 * Date: 5/24/17
 */
public class UITest {

    @Before
    public void setup() {
        enableLafIfAvailable("Nimbus");
    }

    @Test
    public void test() {
//        PolicyEditor.PolicyEditorFrame pe = new PolicyEditor.PolicyEditorFrame(new PolicyEditor("target/java.policy"));
//        showAndWait(pe);
        showAndWait(testFrame());
    }

    public static void showAndWait(final JFrame frame) {
        try {
            final Thread[] edtThreadHolder = new Thread[1];
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    frame.addWindowListener(new CloseListener());
                    frame.pack();
                    frame.setSize(640, 480);
                    frame.setLocationRelativeTo(null);

                    Boxer.highlightBorders(frame.getContentPane());

                    frame.setVisible(true);
                    edtThreadHolder[0] = Thread.currentThread();
                }
            });
            // join on EDT thread here for test-only purposes
            edtThreadHolder[0].join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static class CloseListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            for (Frame fr : Frame.getFrames()) {
                fr.dispose();
            }
        }
    }

    private static JFrame testFrame() {
        JFrame frame = new JFrame();
        frame.setContentPane(createSplitPane());
        return frame;
    }

    private static JComponent createSplitPane() {
        JSplitPane sp = new JSplitPane();
        sp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        sp.setUI(new PlainSplitPaneUI());
        sp.setBorder(null);
//        sp.setDividerLocation(0.30);
        sp.setDividerSize(5);
        sp.setLeftComponent(createLeft());
        sp.setRightComponent(createRightBox());

        return sp;
    }

    private static JComponent createLeft() {
        JPanel wrapper = new JPanel();
        BoxLayout wl = new BoxLayout(wrapper, BoxLayout.Y_AXIS);
        wrapper.setLayout(wl);
        wrapper.setBorder(createEmptyBorder(5, 0, 0, 0));
        wrapper.add(Boxer.createTopBox("List name", createEmptyBorder()));

        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        wrapper.add(jp);

        JList<String> list = new JList<>(new String[]{"Entry 1", "Entry 2", "Entry 3", "Entry 4"});
        list.setCellRenderer(new LeftListCellRendered());
        list.setBorder(createMatteBorder(1, 0, 1, 0, jp.getBackground().darker()));
        jp.add(list, BorderLayout.CENTER);

        wrapper.add(createLeftBottomToolbar());

        JScrollPane sp = new JScrollPane();
        sp.setViewportView(wrapper);
        sp.setBorder(createMatteBorder(0, 0, 0, 1, jp.getBackground().darker()));
        return sp;
    }

    private static JComponent createLeftBottomToolbar() {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.X_AXIS);
        jp.setLayout(bl);
        JButton plus = new JButton("+");
        JButton minus = new JButton("-");
        int max = 0;
        if (plus.getMinimumSize().width > max) {
            max = plus.getMinimumSize().width;
        }
        if (minus.getMinimumSize().width > max) {
            max = minus.getMinimumSize().width;
        }
        Dimension dim = new Dimension(max, max);
        plus.setMinimumSize(dim);
        plus.setPreferredSize(dim);
        plus.setMaximumSize(dim);
        minus.setMinimumSize(dim);
        minus.setPreferredSize(dim);
        minus.setMaximumSize(dim);

        jp.add(plus);

        jp.add(minus);
        jp.add(Box.createHorizontalGlue());
        jp.setMaximumSize(new Dimension(jp.getMaximumSize().width, jp.getMinimumSize().height));
        return jp;
    }

    private static JComponent createRightBox() {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.Y_AXIS);
        jp.setLayout(bl);
        jp.add(Boxer.createTopBox("Panel use instructions", createMatteBorder(0, 0, 1, 0, jp.getBackground().darker())));
        jp.add(createContent());
        jp.add(Box.createVerticalGlue());
        jp.add(Boxer.createBottomBox(createButtonsPanel()));
        jp.setBorder(createEmptyBorder(5, 5, 5, 5));

        JScrollPane sp = new JScrollPane();
        sp.setViewportView(jp);
        sp.setBorder(createMatteBorder(0, 1, 0, 0, jp.getBackground().darker()));

        return sp;
    }

    public static Component createContent() {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.Y_AXIS);
        jp.setLayout(bl);
        jp.add(createForm1());
        jp.add(Box.createVerticalStrut(10));
        jp.add(createForm2());
        jp.add(Box.createVerticalStrut(10));
        jp.add(createForm3());
        return jp;
    }

    public static JPanel createForm1() {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.X_AXIS);
        jp.setLayout(bl);
        jp.add(new JButton("one1"));
        jp.add(new JButton("two1"));
        jp.add(new JButton("three1"));
        jp.add(Box.createHorizontalGlue());
        return jp;
    }

    public static JPanel createForm2() {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.X_AXIS);
        jp.setLayout(bl);
        jp.add(new JButton("one2"));
        jp.add(new JButton("two2"));
        jp.add(new JButton("three2"));
        jp.add(Box.createHorizontalGlue());
        return jp;
    }

    public static JPanel createForm3() {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.X_AXIS);
        jp.setLayout(bl);
        jp.add(new JButton("one3"));
        jp.add(new JButton("two3"));
        jp.add(new JButton("three3"));
        jp.add(Box.createHorizontalGlue());
        return jp;
    }

    private static JPanel createButtonsPanel() {
        JPanel jp = new JPanel();
        BoxLayout bl = new BoxLayout(jp, BoxLayout.X_AXIS);
        jp.setLayout(bl);
        jp.add(new JButton("But"));
        jp.add(Box.createHorizontalStrut(5));
        jp.add(new JButton("Button"));
        jp.add(Box.createHorizontalStrut(5));
        jp.add(new JButton("Button long"));
        Boxer.equaliseWidth(jp, JButton.class);
        return jp;
    }

    private static void enableLafIfAvailable(String lafName) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (lafName.equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    private static class LeftListCellRendered extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component res = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (1 == index % 2) {
                res.setBackground(new Color(0xf6f2f6));
            }
            return res;
        }
    }

    private static class PlainSplitPaneUI extends BasicSplitPaneUI {
        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new BasicSplitPaneDivider(this) {
                public void setBorder(Border b) {
                }
            };
        }
    }
}
