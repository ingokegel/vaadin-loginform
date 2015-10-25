import com.ejt.vaadin.loginform.*;
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
        final SimpleLoginForm loginForm = new SimpleLoginForm();
        loginForm.clear(); // should work even if not displayed
        mainLayout.addComponent(loginForm);
        mainLayout.addComponent(new Label("Native login button:"));
        mainLayout.addComponent(new NativeLoginForm());
        setContent(mainLayout);
    }

    private class SimpleLoginForm extends DefaultHorizontalLoginForm {

        public SimpleLoginForm() {
            addLoginListener(new LoginListener() {
                @Override
                public void onLogin(LoginEvent event) {
                    mainLayout.addComponent(new Label(
                            "Logged in with user name " + event.getUserName() +
                                    " and password of length " + event.getPassword().length()
                    ));
                }
            });

        }

        @Override
        protected HorizontalLayout createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
            HorizontalLayout layout = super.createContent(userNameField, passwordField, loginButton);
            Button clearButton = new Button("Clear");
            clearButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    clear();
                }
            });
            layout.addComponent(clearButton);
            layout.setComponentAlignment(clearButton, Alignment.BOTTOM_LEFT);
            return layout;
        }

    }

    private class NativeLoginForm extends SimpleLoginForm {

        @Override
        protected Button createLoginButton() {
            return new NativeButton("Login");
        }
    }
}
