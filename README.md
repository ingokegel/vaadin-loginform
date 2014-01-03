# Vaadin LoginForm

## Usage
Vaadin LoginForm is an add-on for [Vaadin](https://vaadin.com). It requires at least Vaadin 7.
Specifically it will not work with Vaadin 6. To use it, you have to compile your own widgetset, using the
precompiled widget set will not work with this addon.

To create a login form that supports auto-completion and auto-fill for modern versions of Firefox, Chrome, IE and
Safari, derive from `com.ejt.vaadin.loginform.VerticalLoginForm` or `com.ejt.vaadin.loginform.HorizontalLoginForm`,
like this:

    public class SimpleLoginForm extends VerticalLoginForm {

        @Override
        protected void login(String userName, String password) {
            System.err.println(
                "Logged in with user name " + userName +
                " and password of length " + password.length()
            );
        }
    }

For arbitrary layouts, extend `com.ejt.vaadin.loginform.LoginForm` and implement `createContent` as well:

    public abstract class MyLoginForm extends LoginForm {
        @Override
        protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.setMargin(true);

            layout.addComponent(userNameField);
            layout.addComponent(passwordField);
            layout.addComponent(loginButton);
            layout.setComponentAlignment(loginButton, Alignment.BOTTOM_LEFT);
            return layout;
        }

        @Override
        protected void login(String userName, String password) {
            System.err.println(
                "Logged in with user name " + userName +
                " and password of length " + password.length()
            );
        }
    }


The `userNameField`, `passwordField` and `loginButton` components are specially prepared to work with password managers.
You can override `createUserNameField`, `createPasswordField` and `createLoginButton` to replace the components
with your own implementations or to add styles. To change captions, you can override `getUserNameFieldCaption`,
`getPasswordFieldCaption` and `getLoginButtonCaption`.

In technical terms, the add-on wraps the login UI in an HTML form element that submits a POST request to a dummy resource.
The text field for user name and the password field receive special attributes so that they are recognized by the
password manager.

See the `TestUi` class for a runnable example.

## Build instructions

The addon is built with gradle. The default task builds the distribution JAR file in `build\libs`.
To compile the widgetset for running the Tomcat run configuration in the IntelliJ IDEA project,
run the "compileWidgetSet" gradle task first. No gradle installation is required, executing

    gradlew

in the root directory will download gradle and build the jar file.


## License

Vaadin LoginForm is released under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).