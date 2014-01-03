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

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Login form with auto-completion and auto-fill for all major browsers.
 * Derive from this class and implement the abstract
 * {@link #createContent(com.vaadin.ui.TextField, com.vaadin.ui.PasswordField, com.vaadin.ui.Button)} method
 * to build the layout using the text fields and login button that are passed to that method.
 * The supplied components are specially treated so that they work with password managers.
 * <p>
 * Implement the abstract {@link #login(String, String)} method to handle a login. User name and password are
 * passed to this method. If you need to change the URL as part of the login procedure, call
 * {@link #setLoginMode(LoginMode)} with the argument {@link LoginMode#DEFERRED} in your implementation of
 * {@link #createContent(com.vaadin.ui.TextField, com.vaadin.ui.PasswordField, com.vaadin.ui.Button) createContent}.
 * <p>
 * To customize the fields or to replace them with your own implementations, you can override
 * {@link #createUserNameField()}, {@link #createPasswordField()} and {@link #createLoginButton()}.
 * These methods are called automatically and cannot be called by your code.
 * Captions can be reset by overriding {@link #getUserNameFieldCaption()}, {@link #getPasswordFieldCaption()}
 * and {@link #getLoginButtonCaption()}.
 */
public abstract class LoginForm extends AbstractSingleComponentContainer {

    private boolean initialized;
    private LoginMode loginMode = LoginMode.DIRECT;

    protected LoginForm() {
    }

    /**
     * Create the content for the login form with the supplied user name field, password field and the login button.
     * You cannot use any other text fields or buttons for this purpose. To replace these components with your own
     * implementations, override {@link #createUserNameField()}, {@link #createPasswordField()} and
     * {@link #createLoginButton()}. If you only want to change the default captions, override
     * {@link #getUserNameFieldCaption()}, {@link #getPasswordFieldCaption()} and {@link #getLoginButtonCaption()}.
     * You do not have to use the login button in your layout.
     * @param userNameField the user name text field
     * @param passwordField the password field
     * @param loginButton the login button
     * @return
     */
    protected abstract Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton);

    /**
     * Implement to handle the login. In {@link com.ejt.vaadin.loginform.LoginMode#DEFERRED deferred mode}, this method
     * is called after the dummy POST request that triggers the password manager has been completed.
     * In {@link com.ejt.vaadin.loginform.LoginMode#DIRECT direct mode} (the default setting),
     * it is called directly when the user hits the enter key or clicks on the login button. In tha latter case,
     * you cannot change the URL in the method or the password manager will not be triggered.
     * @param userName the user name
     * @param password the password
     */
    protected abstract void login(String userName, String password);

    /**
     * Returns the {@link LoginMode} for this login form.
     * The default is {@link LoginMode#DIRECT}.
     * @return the login mode
     */
    public LoginMode getLoginMode() {
        return loginMode;
    }

    /**
     * Set the {@link LoginMode} for this login form.
     * The default is {@link LoginMode#DIRECT}
     * @param loginMode the login mode
     */
    public void setLoginMode(LoginMode loginMode) {
        this.loginMode = loginMode;
    }

    /**
     * Customize the user name field. Only for overriding, do not call.
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
     * @return the caption
     */
    protected String getUserNameFieldCaption() {
        return "User name";
    }

    /**
     * Customize the password field. Only for overriding, do not call.
     * @return the password field
     */
    protected PasswordField createPasswordField() {
        checkInitialized();
        return new PasswordField(getPasswordFieldCaption());
    }

    /**
     * Override to change the caption of the password field. The default value is "Password".
     * @return the caption
     */
    protected String getPasswordFieldCaption() {
        return "Password";
    }

    /**
     * Customize the login button. Only for overriding, do not call.
     * @return the login button
     */
    protected Button createLoginButton() {
        checkInitialized();
        return new Button(getLoginButtonCaption());
    }

    /**
     * Override to change the caption of the login button. The default value is "Login".
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

        String contextPath = VaadinService.getCurrentRequest().getContextPath();
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        state.contextPath = contextPath;

        VaadinSession.getCurrent().addRequestHandler(new RequestHandler() {
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
        });

        registerRpc(new LoginFormRpc() {
            @Override
            public void submitted() {
                if (loginMode == LoginMode.DIRECT) {
                    login();
                }
            }

            @Override
            public void submitCompleted() {
                if (loginMode == LoginMode.DEFERRED) {
                    login();
                }
            }
        });

        initialized = true;

        setContent(createContent(getUserNameField(), getPasswordField(), getLoginButton()));
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

}
