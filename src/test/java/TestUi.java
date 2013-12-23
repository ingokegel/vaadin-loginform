import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class TestUi extends UI{
    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new SimpleLoginForm(layout));
        setContent(layout);
    }
}
