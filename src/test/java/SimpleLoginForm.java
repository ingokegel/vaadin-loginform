import com.ejt.vaadin.loginform.LoginForm;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import sun.print.resources.serviceui;

public class SimpleLoginForm extends LoginForm {

    private VerticalLayout parentLayout;

    public SimpleLoginForm(VerticalLayout parentLayout) {
        this.parentLayout = parentLayout;
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(getUserNameField());
        layout.addComponent(getPasswordField());
        layout.addComponent(getLoginButton());

        setContent(layout);
    }

    @Override
    protected void login() {
        parentLayout.addComponent(new Label(
            "Logged in with user name " + getUserNameField().getValue() + " and password of length " + getPasswordField().getValue().length()
        ));
    }
}
