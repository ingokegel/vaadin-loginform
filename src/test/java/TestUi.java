import com.ejt.vaadin.loginform.DefaultHorizontalLoginForm;
import com.ejt.vaadin.loginform.DefaultVerticalLoginForm;
import com.ejt.vaadin.loginform.LoginForm;
import com.ejt.vaadin.loginform.LoginMode;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

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

    public class SimpleLoginForm extends DefaultHorizontalLoginForm {
        private LoginMode loginMode;

        public SimpleLoginForm(LoginMode loginMode) {
            this.loginMode = loginMode;
            setLoginMode(loginMode);
        }

        @Override
        protected void login(String userName, String password) {
            mainLayout.addComponent(new Label(
                "Logged in with user name " + userName +
                " and password of length " + password.length() +
                ", login mode " + loginMode
            ));
        }
    }
}
