import net.sourceforge.jnlp.util.Boxer;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.font.TextAttribute;

import static java.util.Collections.singletonMap;
import static javax.swing.BorderFactory.*;
import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.Box.createHorizontalGlue;
import static net.sourceforge.jnlp.util.Boxer.*;

/**
 * User: alexkasko
 * Date: 5/24/17
 */
public class LayoutTest {

    @Before
    public void setup() {
        enableLafIfAvailable("Nimbus");
    }

    @Test
    public void test() {
        TestUtils.showAndWait(testFrame());
    }

    private static JFrame testFrame() {
        JFrame frame = new JFrame();
        frame.setContentPane(createSplitPane());
        JMenuBar menu = new JMenuBar();
        menu.add(new JMenu("PEFileMenu"));
        frame.setJMenuBar(menu);
        return frame;
    }

    private static JComponent createSplitPane() {
        JSplitPane sp = new JSplitPane();
        sp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        sp.setUI(new PlainSplitPaneUI());
        sp.setBorder(null);
        sp.setDividerSize(5);
        sp.setLeftComponent(createLeft());
        sp.setRightComponent(createRightBox());

        return sp;
    }

    private static JComponent createLeft() {
        JPanel wrapper = verticalPanel();
        wrapper.setBorder(createEmptyBorder(5, 0, 0, 0));
        wrapper.add(Boxer.createTopBox("PEEntriesLabel", createEmptyBorder()));

        JList<String> list = new JList<>(new String[]{"PEGlobalSettings", "Entry 2", "Entry 3", "Entry 4"});
        list.setCellRenderer(new LeftListCellRendered());
        list.setBorder(createMatteBorder(1, 0, 1, 0, wrapper.getBackground().darker()));
        list.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        wrapper.add(list);

        wrapper.add(createLeftBottomToolbar());

        JScrollPane sp = new JScrollPane();
        sp.setViewportView(wrapper);
        sp.setBorder(createMatteBorder(0, 0, 0, 1, wrapper.getBackground().darker()));
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
        jp.add(Boxer.createTopBox("PECheckboxLabel", createMatteBorder(0, 0, 1, 0, jp.getBackground().darker())));
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

        // group labels
        JLabel labelNetwork = createFormLabel("PENetwork", TextAttribute.WEIGHT_BOLD);
        JLabel labelGReadFileSystem = createFormLabel("PEGReadFileSystem", TextAttribute.WEIGHT_BOLD);
        JLabel labelGWriteFileSystem = createFormLabel("PEGWriteFileSystem", TextAttribute.WEIGHT_BOLD);
        JLabel labelGAccessUnownedCode = createFormLabel("GAccessUnownedCode", TextAttribute.WEIGHT_BOLD);
        JLabel labelGMediaAccess = createFormLabel("GMediaAccess", TextAttribute.WEIGHT_BOLD);
        JLabel labelAWTPermission = createFormLabel("AWTPermission", TextAttribute.WEIGHT_BOLD);

        int masterWidth = equaliseWidth(labelNetwork, labelGReadFileSystem, labelGWriteFileSystem,
                labelGAccessUnownedCode, labelGMediaAccess, labelAWTPermission);
        masterWidth += HORIZONTAL_SPACER_WIDTH;

        { // network and awt
            JPanel panelNetworkAwt = verticalPanel(jp);
            JPanel panel = horizontalPanel(panelNetworkAwt);
            panel.add(labelNetwork);
            panel.add(slimHorizontalStrut());
            panel.add(new JCheckBox());
            panel.add(slimHorizontalStrut());
            panel.add(labelAWTPermission);
            panel.add(slimHorizontalStrut());
            panel.add(new JCheckBox());
            panel.add(slimHorizontalStrut(HORIZONTAL_ENDER_WIDTH));
            panel.add(createHorizontalGlue());
            jp.add(createSeparatorPanel());
        }

        { // read file system
            JPanel panelGReadFileSystem = createMasterPanel(jp, labelGReadFileSystem);
            addReadFileSystem(panelGReadFileSystem, masterWidth);
            jp.add(createSeparatorPanel());
        }

        { // write file system
            JPanel panelGWriteFileSystem = createMasterPanel(jp, labelGWriteFileSystem);
            addWriteFileSystem(panelGWriteFileSystem, masterWidth);
            jp.add(createSeparatorPanel());
        }

        { // access unowned code
            JPanel panelGAccessUnownedCode = createMasterPanel(jp, labelGAccessUnownedCode);
            addAccessUnownedCode(panelGAccessUnownedCode, masterWidth);
            jp.add(createSeparatorPanel());
        }

        { // media access
            JPanel panelGMediaAccess = createMasterPanel(jp, labelGMediaAccess);
            addMediaAccess(panelGMediaAccess, masterWidth);
        }

        return jp;
    }

    private static void addReadFileSystem(JPanel parent, int groupLabelWidth) {
        JLabel labelReadFiles = createFormLabel("PEReadFiles");
        JLabel labelReadSystemFiles = createFormLabel("PEReadSystemFiles");
        JLabel labelGetEnv = createFormLabel("PEGetEnv");
        equaliseWidth(labelReadFiles, labelReadSystemFiles, labelGetEnv);

        JLabel labelReadProps = createFormLabel("PEReadProps");
        JLabel labelReadTempFiles = createFormLabel("PEReadTempFiles");
        equaliseWidth(labelReadProps, labelReadTempFiles);

        addChildEntry(parent, groupLabelWidth, labelReadFiles, labelReadProps);
        addChildEntry(parent, groupLabelWidth, labelReadSystemFiles, labelReadTempFiles);
        addChildEntry(parent, groupLabelWidth, labelGetEnv);
    }

    private static void addWriteFileSystem(JPanel parent, int groupLabelWidth) {
        JLabel labelWriteFiles = createFormLabel("PEWriteFiles");
        JLabel labelWriteProps = createFormLabel("PEWriteProps");
        JLabel labelWriteTempFiles = createFormLabel("PEWriteTempFiles");
        JLabel labelExec = createFormLabel("PEExec");
        equaliseWidth(labelWriteFiles, labelWriteProps, labelWriteTempFiles, labelExec);

        JLabel labelDeleteFiles = createFormLabel("PEDeleteFiles");
        JLabel labelWriteSystemFiles = createFormLabel("PEWriteSystemFiles");
        JLabel labelDeleteTempFiles = createFormLabel("PEDeleteTempFiles");
        equaliseWidth(labelDeleteFiles, labelWriteSystemFiles, labelDeleteTempFiles);

        addChildEntry(parent, groupLabelWidth, labelWriteFiles, labelDeleteFiles);
        addChildEntry(parent, groupLabelWidth, labelWriteProps, labelWriteSystemFiles);
        addChildEntry(parent, groupLabelWidth, labelWriteTempFiles, labelDeleteTempFiles);
        addChildEntry(parent, groupLabelWidth, labelExec);
    }

    private static void addAccessUnownedCode(JPanel parent, int groupLabelWidth) {
        JLabel labelReflection = createFormLabel("PEReflection");
        JLabel labelClassInPackage = createFormLabel("PEClassInPackage");
        JLabel labelAccessThread = createFormLabel("PEAccessThread");
        equaliseWidth(labelReflection, labelClassInPackage, labelAccessThread);

        JLabel labelClassLoader = createFormLabel("PEClassLoader");
        JLabel labelDeclaredMembers = createFormLabel("PEDeclaredMembers");
        JLabel labelAccessThreadGroups = createFormLabel("PEAccessThreadGroups");
        equaliseWidth(labelClassLoader, labelDeclaredMembers, labelAccessThreadGroups);

        addChildEntry(parent, groupLabelWidth, labelReflection, labelClassLoader);
        addChildEntry(parent, groupLabelWidth, labelClassInPackage, labelDeclaredMembers);
        addChildEntry(parent, groupLabelWidth, labelAccessThread, labelAccessThreadGroups);
    }

    private static void addMediaAccess(JPanel parent, int groupLabelWidth) {
        JLabel labelPlayAudio = createFormLabel("PEPlayAudio");
        JLabel labelPrint = createFormLabel("PEPrint");
        equaliseWidth(labelPlayAudio, labelPrint);

        JLabel labelRecordAudio = createFormLabel("PERecordAudio");
        JLabel labelClipboard = createFormLabel("PEClipboard");
        equaliseWidth(labelRecordAudio, labelClipboard);

        addChildEntry(parent, groupLabelWidth, labelPlayAudio, labelRecordAudio);
        addChildEntry(parent, groupLabelWidth, labelPrint, labelClipboard);
    }

    private static void addChildEntry(JPanel parent, int groupLabelWidth, JLabel label1) {
        addChildEntry(parent, groupLabelWidth, label1, null);
    }

    private static void addChildEntry(JPanel parent, int groupLabelWidth, JLabel label1, JLabel label2) {
        JPanel panel = horizontalPanel(parent);
        panel.add(slimHorizontalStrut(groupLabelWidth));
        panel.add(label1);
        panel.add(slimHorizontalStrut());
        panel.add(new JCheckBox());
        if (null != label2) {
            panel.add(slimHorizontalStrut());
            panel.add(label2);
            panel.add(slimHorizontalStrut());
            panel.add(new JCheckBox());
        }
        panel.add(slimHorizontalStrut(HORIZONTAL_ENDER_WIDTH));
        panel.add(createHorizontalGlue());
    }

    private static JPanel createMasterPanel(JPanel parent, JLabel label) {
        JPanel jp = verticalPanel(parent);

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
                        createEmptyBorder(0, 0, VERTICAL_SPACER_HEIGHT, 0),
                        createCompoundBorder(
                                createMatteBorder(0, 0, 1, 0, panel.getBackground().darker()),
                                createEmptyBorder(VERTICAL_SPACER_HEIGHT, 0, 0, 0))));
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
        JButton buttonApply = new JButton("ButApply");
        jp.add(buttonApply);
        jp.add(slimHorizontalStrut());
        JButton buttonClose = new JButton("ButC");
        jp.add(buttonClose);
        Boxer.equaliseWidth(buttonApply, buttonClose);
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
