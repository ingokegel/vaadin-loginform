/*
 * Copyright 2013 Ingo Kegel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ejt.vaadin.loginform;

import com.ejt.vaadin.loginform.shared.LoginFormConnector;
import com.ejt.vaadin.loginform.shared.LoginFormRpc;
import com.ejt.vaadin.loginform.shared.LoginFormState;
import com.vaadin.server.*;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Login form with auto-completion and auto-fill for all major browsers.
 * Derive from this class and implement the abstract
 * {@link #createContent(com.vaadin.ui.TextField, com.vaadin.ui.PasswordField, com.vaadin.ui.Button)} method
 * to build the layout using the text fields and login button that are passed to that method.
 * The supplied components are specially treated so that they work with password managers.
 * <p/>
 * Implement the abstract {@link #login(String, String)} method to handle a login. User name and password are
 * passed to this method.
 * <p/>
 * To customize the fields or to replace them with your own implementations, you can override
 * {@link #createUserNameField()}, {@link #createPasswordField()} and {@link #createLoginButton()}.
 * These methods are called automatically and cannot be called by your code.
 * Captions can be reset by overriding {@link #getUserNameFieldCaption()}, {@link #getPasswordFieldCaption()}
 * and {@link #getLoginButtonCaption()}.
 */
public abstract class LoginForm extends AbstractSingleComponentContainer {

    private static final Method ON_LOGIN_METHOD;

    static {
        try {
            ON_LOGIN_METHOD = LoginListener.class.getDeclaredMethod("onLogin", new Class[] {LoginEvent.class});
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException("Internal error finding methods in LoginListener");
        }
    }

    private static final RequestHandler REQUEST_HANDLER = new RequestHandler() {
        @Override
        public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {
            if (LoginFormConnector.LOGIN_URL.equals(request.getPathInfo())) {
                response.setContentType("text/html; charset=utf-8");
                response.setCacheTime(-1);
                PrintWriter writer = response.getWriter();
                writer.append("<html>Success</html>");
                return true;
            } else {
                return false;
            }
        }
    };

    private boolean initialized;

    protected LoginForm() {
    }

    /**
     * Create the content for the login form with the supplied user name field, password field and the login button.
     * You cannot use any other text fields or buttons for this purpose. To replace these components with your own
     * implementations, override {@link #createUserNameField()}, {@link #createPasswordField()} and
     * {@link #createLoginButton()}. If you only want to change the default captions, override
     * {@link #getUserNameFieldCaption()}, {@link #getPasswordFieldCaption()} and {@link #getLoginButtonCaption()}.
     * You do not have to use the login button in your layout.
     *
     * @param userNameField the user name text field
     * @param passwordField the password field
     * @param loginButton   the login button
     * @return
     */
    protected abstract Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton);

    /**
     * You can override this method to handle the login directly without using the event mechanism.
     * The login event will only be fired, if you call super.login(..) in your overriding method.
     * This method is called after the dummy POST request that triggers the password manager has been completed.
     *
     * @param userName the user name
     * @param password the password
     */
    protected void login(String userName, String password) {
        fireEvent(new LoginEvent(LoginForm.this, userName, password));
    }

    /**
     * Customize the user name field. Only for overriding, do not call.
     *
     * @return the user name field
     */
    protected TextField createUserNameField() {
        checkInitialized();
        TextField field = new TextField(getUserNameFieldCaption());
        field.focus();
        return field;
    }

    /**
     * Override to change the caption of the user name field. The default value is "User name".
     *
     * @return the caption
     */
    protected String getUserNameFieldCaption() {
        return "User name";
    }

    /**
     * Customize the password field. Only for overriding, do not call.
     *
     * @return the password field
     */
    protected PasswordField createPasswordField() {
        checkInitialized();
        return new PasswordField(getPasswordFieldCaption());
    }

    /**
     * Override to change the caption of the password field. The default value is "Password".
     *
     * @return the caption
     */
    protected String getPasswordFieldCaption() {
        return "Password";
    }

    /**
     * Customize the login button. Only for overriding, do not call.
     *
     * @return the login button
     */
    protected Button createLoginButton() {
        checkInitialized();
        return new Button(getLoginButtonCaption());
    }

    /**
     * Override to change the caption of the login button. The default value is "Login".
     *
     * @return the caption
     */
    protected String getLoginButtonCaption() {
        return "Login";
    }

    @Override
    protected LoginFormState getState() {
        return (LoginFormState)super.getState();
    }

    @Override
    public void attach() {
        super.attach();
        init();
    }

    /**
     * Clears the contents of both the user name and the password text fields
     **/
    public void clear() {
        clear(getUserNameField());
        clear(getPasswordField());
    }

    private void clear(AbstractTextField textField) {
        if (textField != null) {
            textField.clear();
        }
    }

    private void checkInitialized() {
        if (initialized) {
            throw new IllegalStateException("Already initialized. The create methods may not be called explicitly.");
        }
    }

    private void init() {
        if (initialized) {
            return;
        }

        LoginFormState state = getState();
        state.userNameFieldConnector = createUserNameField();
        state.passwordFieldConnector = createPasswordField();
        state.loginButtonConnector = createLoginButton();

        state.contextPath = getContextPath();

        if (!VaadinSession.getCurrent().getRequestHandlers().contains(REQUEST_HANDLER)) {
            VaadinSession.getCurrent().addRequestHandler(REQUEST_HANDLER);
        }
        registerRpc(new LoginFormRpc() {
            @Override
            public void submitCompleted() {
                login();
            }
        });

        initialized = true;

        setContent(createContent(getUserNameField(), getPasswordField(), getLoginButton()));
    }

    private String getContextPath() {
        String contextPath = getContextPathFromService();
        if (contextPath.endsWith("/")) {
            return contextPath.substring(0, contextPath.length() - 1);
        } else {
            return contextPath;
        }
    }

    private String getContextPathFromService() {
        VaadinService service = VaadinService.getCurrent();
        if (service instanceof VaadinPortletService) {
            PortletRequest portletRequest = VaadinPortletService.getCurrentPortletRequest();
            if (portletRequest != null) {
                return portletRequest.getContextPath();
            } else {
                return null;
            }
        } else if (service instanceof VaadinServletService) {
            HttpServletRequest servletRequest = VaadinServletService.getCurrentServletRequest();
            if (servletRequest != null) {
                return servletRequest.getContextPath();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private TextField getUserNameField() {
        return (TextField)getState().userNameFieldConnector;
    }

    private PasswordField getPasswordField() {
        return (PasswordField)getState().passwordFieldConnector;
    }

    private Button getLoginButton() {
        return (Button)getState().loginButtonConnector;
    }

    private void login() {
        login(getUserNameField().getValue(), getPasswordField().getValue());
    }

    /**
     * Adds a listener to handle the login.
     *
     * @param listener the listener to be added
     */
    public void addLoginListener(LoginListener listener) {
        addListener(LoginEvent.class, listener, ON_LOGIN_METHOD);
    }

    /**
     * Removes a listener
     *
     * @param listener the listener to be removed
     */
    public void removeLoginListener(LoginListener listener) {
        removeListener(LoginEvent.class, listener, ON_LOGIN_METHOD);
    }

    /**
     * Login listener listen LoginEvents sent from
     * CustomLoginForm
     */
    public interface LoginListener extends Serializable {

        /**
         * This method is fired for each login.
         * Note that the invocation is asynchronous since it is triggered by a dummy form submission, so
         * Vaadin TestBench will not wait for it automatically.
         *
         * @param event
         */
        void onLogin(final LoginEvent event);
    }

    /**
     * This event is sent when the login form is submitted.
     */
    public static class LoginEvent extends Event {

        private final String userName;
        private final String password;

        private LoginEvent(final Component source, final String userName, final String password) {
            super(source);
            this.userName = userName;
            this.password = password;
        }

        /**
         * Get the user name entered by the user.
         *
         * @return the user name
         */
        public String getUserName() {
            return userName;
        }

        /**
         * Get the password entered by the user.
         *
         * @return the password value
         */
        public String getPassword() {
            return password;
        }
    }
}