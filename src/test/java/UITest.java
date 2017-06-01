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
import java.awt.font.TextAttribute;
import java.util.Collections;

import static java.util.Collections.singletonMap;
import static javax.swing.BorderFactory.*;
import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.Box.createHorizontalGlue;
import static javax.swing.Box.createVerticalStrut;
import static net.sourceforge.jnlp.util.Boxer.*;

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

//                    Boxer.highlightBorders(frame.getContentPane());

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
        JMenuBar menu = new JMenuBar();
        menu.add(new JMenu("File"));
        frame.setJMenuBar(menu);
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
        JPanel wrapper = verticalPanel();
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
        JPanel jp = horizontalPanel();
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
        jp.add(createHorizontalGlue());
        jp.setMaximumSize(new Dimension(jp.getMaximumSize().width, jp.getMinimumSize().height));
        return jp;
    }

    private static JComponent createRightBox() {
        JPanel jp = verticalPanel();
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
        JPanel jp = verticalPanel();
        jp.setBorder(createEmptyBorder(5, 5 , 5, 5));

        // master labels
        JLabel labelMaster1 = createFormLabel("Master1", TextAttribute.WEIGHT_BOLD);
        JLabel labelMaster2 = createFormLabel("2", TextAttribute.WEIGHT_BOLD);
        JLabel labelMaster3 = createFormLabel("Master 3 label", TextAttribute.WEIGHT_BOLD);

        int masterWidth = equaliseWidth(labelMaster1, labelMaster2, labelMaster3);
        masterWidth += HORIZONTAL_SPACER_WIDTH;

        // master 1
        JPanel mp1 = createMasterPanel(jp, labelMaster1);
        addChildren1(mp1, masterWidth);
        jp.add(createSeparatorPanel());

        // master 2
        JPanel mp2 = createMasterPanel(jp, labelMaster2);
        addChildren2(mp2, masterWidth);
        jp.add(createSeparatorPanel());

        // master 3
        JPanel mp3 = createMasterPanel(jp, labelMaster3);
        addChildren2(mp3, masterWidth);

        return jp;
    }

    private static void addChildren1(JPanel parent, int masterLabelWidth) {
        JLabel labelChild1 = createFormLabel("Child1");
        addChildEntry(parent, labelChild1, masterLabelWidth);

        JLabel labelChild2 = createFormLabel("2");
        addChildEntry(parent, labelChild2, masterLabelWidth);

        JLabel labelChild3 = createFormLabel("Child 3 label");
        addChildEntry(parent, labelChild3, masterLabelWidth);

        equaliseWidth(labelChild1, labelChild2, labelChild3);
    }

    private static void addChildren2(JPanel parent, int masterLabelWidth) {
        JLabel labelChild1 = createFormLabel("Child1");
        addChildEntry(parent, labelChild1, masterLabelWidth);

        JLabel labelChild2 = createFormLabel("2");
        addChildEntry(parent, labelChild2, masterLabelWidth);

        equaliseWidth(labelChild1, labelChild2);
    }

    private static void addChildEntry(JPanel parent, JLabel label, int masterLabelWidth) {
        JPanel panel = horizontalPanel(parent);
        panel.add(slimHorizontalStrut(masterLabelWidth));
        panel.add(label);
        panel.add(slimHorizontalStrut());
        panel.add(new JCheckBox());
        panel.add(createHorizontalGlue());
    }

    private static JPanel createMasterPanel(JPanel parent, JLabel label) {
        JPanel jp = verticalPanel(parent);
//        jp.setBorder(createMatteBorder(1, 1, 1, 1, jp.getBackground().darker()));

        JPanel panel = horizontalPanel(jp);
        panel.add(label);
        panel.add(slimHorizontalStrut());
        panel.add(new JCheckBox());
        panel.add(createHorizontalGlue());

        return jp;
    }

    private static JPanel createSeparatorPanel() {
        JPanel panel = horizontalPanel();
        panel.add(createHorizontalGlue());
        panel.setBorder(
                createCompoundBorder(
                        createEmptyBorder(0, 0, 12, 0),
                        createCompoundBorder(
                                createMatteBorder(0, 0, 1, 0, panel.getBackground().darker()),
                                createEmptyBorder(12, 0, 0, 0))));
        return panel;
    }

    private static JLabel createFormLabel(String text) {
        return createFormLabel(text, TextAttribute.WEIGHT_REGULAR);
    }

    private static JLabel createFormLabel(String text, float weight) {
        JLabel label = new JLabel(text + ":");
        Font font = label.getFont().deriveFont(
                singletonMap(TextAttribute.WEIGHT, weight));
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private static JPanel createButtonsPanel() {
        JPanel jp = horizontalPanel();
        JButton button1 = new JButton("But");
        jp.add(button1);
        jp.add(slimHorizontalStrut());
        JButton button2 = new JButton("Button");
        jp.add(button2);
        jp.add(slimHorizontalStrut());
        JButton button3 = new JButton("Button long");
        jp.add(button3);
        Boxer.equaliseWidth(button1, button2, button3);
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
