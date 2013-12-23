import com.ejt.vaadin.loginform.LoginForm;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class TestUi extends UI{

    private VerticalLayout mainLayout;

    @Override
    protected void init(VaadinRequest request) {
        mainLayout = new VerticalLayout();
        mainLayout.setMargin(true);
        mainLayout.addComponent(new SimpleLoginForm());
        setContent(mainLayout);
    }

    public class SimpleLoginForm extends LoginForm {
        public SimpleLoginForm() {
            VerticalLayout layout = new VerticalLayout();
            layout.setSpacing(true);
            layout.setMargin(true);

            layout.addComponent(getUserNameField());
            layout.addComponent(getPasswordField());
            layout.addComponent(getLoginButton());

            setContent(layout);
        }

        @Override
        protected void login() {
            mainLayout.addComponent(new Label(
                "Logged in with user name " + getUserNameField().getValue() + " and password of length " + getPasswordField().getValue().length()
            ));
        }
    }
}
