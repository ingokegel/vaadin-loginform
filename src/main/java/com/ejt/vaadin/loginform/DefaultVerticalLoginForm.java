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

import com.vaadin.ui.*;

/**
 * Login form that lays out the text fields and the login button vertically.
 * This is a convenience class, you can extends from {@link LoginForm}
 * and override {@link LoginForm#createContent(com.vaadin.ui.TextField, com.vaadin.ui.PasswordField, com.vaadin.ui.Button)}
 * to create an arbitrary layout.
 */
public class DefaultVerticalLoginForm extends LoginForm {
    @Override
    protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        layout.addComponent(userNameField);
        layout.addComponent(passwordField);
        layout.addComponent(loginButton);
        return layout;
    }
}
