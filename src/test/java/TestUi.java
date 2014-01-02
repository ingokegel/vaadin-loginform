import com.ejt.vaadin.loginform.LoginForm;
import com.ejt.vaadin.loginform.LoginMode;
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
        mainLayout.addComponent(new Label("Direct mode:"));
        mainLayout.addComponent(new SimpleLoginForm(LoginMode.DIRECT));
        mainLayout.addComponent(new Label("Deferred mode:"));
        mainLayout.addComponent(new SimpleLoginForm(LoginMode.DEFERRED));
        setContent(mainLayout);
    }

    public class SimpleLoginForm extends LoginForm {
        private LoginMode loginMode;

        public SimpleLoginForm(LoginMode loginMode) {
            this.loginMode = loginMode;
            VerticalLayout layout = new VerticalLayout();
            layout.setSpacing(true);
            layout.setMargin(true);

            layout.addComponent(getUserNameField());
            layout.addComponent(getPasswordField());
            layout.addComponent(getLoginButton());
            setLoginMode(loginMode);

            setContent(layout);
        }

        @Override
        protected void login() {
            mainLayout.addComponent(new Label(
                "Logged in with user name " + getUserNameField().getValue() +
                " and password of length " + getPasswordField().getValue().length() +
                ", login mode " + loginMode
            ));
        }
    }
}
