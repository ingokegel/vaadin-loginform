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

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.*;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Login form with auto-completion and auto-fill for all major browsers.
 * Derive from this class and build a layout in the constructor, then call
 * setContent() with that layout.
 * <p>
 * When building the layou, use the {@link #getUserNameField()}, {@link #getPasswordField()} and
 * {@link #getLoginButton()} methods to get the fields that are specially treated so that they work with
 * password managers.
 * <p>
 * Implement the abstract {@link #login()} method to handle a login. User name and password are available
 * from <tt>getUserNameField().getValue()</tt> and <tt>getPasswordField().getValue()</tt>.
 * <p>
 * To customize the fields, you can override {@link #createUserNameField()}, {@link #createPasswordField()} and
 * {@link #createLoginButton()}. These methods are called automatically and cannot be called by your code.
 *
 */
public abstract class LoginForm extends AbstractSingleComponentContainer {

    private boolean initialized;

    protected LoginForm() {
        LoginFormState state = getState();
        state.userNameFieldConnector = createUserNameField();
        state.passwordFieldConnector = createPasswordField();
        state.loginButtonConnector = createLoginButton();

        String contextPath = VaadinService.getCurrentRequest().getContextPath();
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        state.contextPath = contextPath;

        addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                login();
            }
        });

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
            public void submitCompleted() {
                LoginForm.this.submitCompleted();
            }
        });

        initialized = true;
    }

    protected abstract void login();

    /**
     * If you have to change the URL after the login, you cannot do that in the implementation of {@link #login()},
     * because the POST request from the dummy form that triggers the password manager could be canceled.
     * Instead, you can then override this method to change the URL once the browser has completed the submission
     * of the dummy form.
     */
    protected void submitCompleted() {
    }

    /**
     * Get the user name field. Call when building the layout and when validating a login.
     * @return the user name field
     */
    protected TextField getUserNameField() {
        return (TextField)getState().userNameFieldConnector;
    }

    /**
     * Get the password field. Call when building the layout and when validating a login.
     * @return the password field
     */
    protected PasswordField getPasswordField() {
        return (PasswordField)getState().passwordFieldConnector;
    }

    /**
     * Get the login button. Call when building the layout.
     * @return the password field
     */
    protected Button getLoginButton() {
        return (Button)getState().loginButtonConnector;
    }

    /**
     * Customize the user name field. Only for overriding, do not call.
     * @return the user name field
     */
    protected TextField createUserNameField() {
        checkInitialized();
        TextField field = new TextField("User name");
        field.focus();
        return field;
    }

    /**
     * Customize the password field. Only for overriding, do not call.
     * @return the password field
     */
    protected PasswordField createPasswordField() {
        checkInitialized();
        return new PasswordField("Password");
    }

    /**
     * Customize the login button. Only for overriding, do not call.
     * @return the login button
     */
    protected Button createLoginButton() {
        checkInitialized();
        Button button = new Button("Login", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                login();
            }
        });
        return button;
    }

    private void checkInitialized() {
        if (initialized) {
            throw new IllegalStateException("Already initialized. Call the getter method instead of the create method.");
        }
    }

    @Override
    protected LoginFormState getState() {
        return (LoginFormState)super.getState();
    }
}
