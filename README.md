# Vaadin LoginForm

## Usage
Vaadin LoginForm is an add-on for [Vaadin](https://vaadin.com). It requires at least Vaadin 7.3
Specifically it will not work with Vaadin 6. To use it, you have to compile your own widgetset, using the
precompiled widget set will not work with this addon.

To create a login form that supports auto-completion and auto-fill for modern versions of Firefox, Chrome, IE and
Safari, you can use the classes `com.ejt.vaadin.loginform.DefaultVerticalLoginForm` or 
`com.ejt.vaadin.loginform.DefaultHorizontalLoginForm` and add a listener to handle login events:

    // Add the loginForm instance below to your UI
    DefaultVerticalLoginForm loginForm = new DefaultVerticalLoginForm();
    loginForm.addLoginListener(new LoginListener() {
        @Override
        public void onLogin(LoginEvent event) {
            System.err.println(
                    "Logged in with user name " + event.getUserName() +
                            " and password of length " + event.getPassword().length());
    
        }
    });

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

        // You can also override this method to handle the login directly, instead of using the event mechanism
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