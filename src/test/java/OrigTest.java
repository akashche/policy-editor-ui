import net.sourceforge.jnlp.security.policyeditor.PolicyEditor;
import org.junit.Test;

/**
 * User: alexkasko
 * Date: 6/1/17
 */
public class OrigTest {
    @Test
    public void test() {
        PolicyEditor.PolicyEditorFrame pe = new PolicyEditor.PolicyEditorFrame(new PolicyEditor("target/java.policy"));
        TestUtils.showAndWait(pe);
    }
}
