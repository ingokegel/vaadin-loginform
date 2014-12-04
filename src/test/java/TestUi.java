import com.ejt.vaadin.loginform.DefaultHorizontalLoginForm;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

@Theme("valo")
public class TestUi extends UI {

    private VerticalLayout mainLayout;

    @Override
    protected void init(VaadinRequest request) {
        mainLayout = new VerticalLayout();
        mainLayout.setMargin(true);
        mainLayout.addComponent(new Label("Vaadin button:"));
        mainLayout.addComponent(new SimpleLoginForm());
        mainLayout.addComponent(new Label("Native login button:"));
        mainLayout.addComponent(new NativeLoginForm());
        setContent(mainLayout);
    }

    private class SimpleLoginForm extends DefaultHorizontalLoginForm {

        @Override
        protected void login(String userName, String password) {
            mainLayout.addComponent(new Label(
                "Logged in with user name " + userName +
                " and password of length " + password.length()
            ));
        }
    }

    private class NativeLoginForm extends SimpleLoginForm {

        @Override
        protected Button createLoginButton() {
            return new NativeButton("Login");
        }
    }
}
