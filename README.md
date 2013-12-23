# Vaadin LoginForm

## Usage
Vaadin LoginForm is an add-on for [Vaadin](https://vaadin.com). It requires at least Vaadin 7.
Specifically it will not work with Vaadin 6. To use it, you have to compile your own widgetset, using the
precompiled widget set will not work with this addon.

To create a login form that supports auto-completion and auto-fill for modern versions of Firefox, Chrome, IE and
Safari, derive from com.ejt.vaadin.loginform.LoginForm, like this:

    public class SimpleLoginForm extends LoginForm {

        public SimpleLoginForm() {
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
            System.err.println(
                "Logged in with user name " + getUserNameField().getValue() +
                " and password of length " + getPasswordField().getValue().length()
            );
        }
    }

Use the `getUserNameField`, `getPasswordField` and `getLoginButton` to get the components that work with password managers,
add them to your layout and call setContent on the LoginForm instance. In the implementation of the abstract `login`
method, query the values of the user name and password fields in order to validate the login.

In technical terms, the add-on wraps the login UI in an HTML form element that submits a POST request to a dummy resource.
The text field for user name and the password field have special attributes so that they are recognized by the password manager.

See the `TestUi` class for a runnable example.

## Build instructions

The addon is built with gradle. The default task builds the distribution JAR file in `build\libs`.
To compile the widgetset for running the Tomcat run configuration in the IntelliJ IDEA project,
run the "compileWidgetSet" gradle task first. No gradle installation is required, executing

    gradlew

in the root directory will download gradle and build the jar file.
