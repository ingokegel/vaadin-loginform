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

package com.ejt.vaadin.loginform.shared;

import com.ejt.vaadin.loginform.LoginForm;
import com.ejt.vaadin.loginform.client.LoginFormGWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import com.google.gwt.user.client.ui.FormPanel;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractSingleComponentContainerConnector;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VTextField;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.Connect;

@Connect(LoginForm.class)
public class LoginFormConnector extends AbstractSingleComponentContainerConnector {

    public static final String LOGIN_URL = "/loginForm";

    private VTextField passwordField;
    private VTextField userField;
    private LoginFormRpc loginFormRpc;

    @Override
    public void updateCaption(ComponentConnector connector) {

    }

    @Override
    public LoginFormGWT getWidget() {
        return (LoginFormGWT)super.getWidget();
    }

    @Override
    protected void init() {
        super.init();

        loginFormRpc = getRpcProxy(LoginFormRpc.class);
        getWidget().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                loginFormRpc.submitCompleted();
            }
        });
    }

    @Override
    public LoginFormState getState() {
        return (LoginFormState)super.getState();
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        ComponentConnector content = getContent();
        if (content != null) {
            getWidget().setWidget(getContentWidget());
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        LoginFormState state = getState();
        userField = configureTextField(state.userNameFieldConnector, "username");
        passwordField = configureTextField(state.passwordFieldConnector, "password");
        addSubmitButtonClickHandler(state.loginButtonConnector);
    }

    private VTextField configureTextField(Connector connector, String id) {
        if (connector != null) {
            VTextField textField = ((TextFieldConnector)connector).getWidget();

            textField.addKeyDownHandler(new KeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event) {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        login();
                    }
                }
            });

            Element element = textField.getElement();
            String externalId = element.getId();
            if (externalId == null || externalId.isEmpty() || externalId.startsWith("gwt-")) {
                element.setId(id);
            }
            DOM.setElementAttribute(element, "name", id);
            DOM.setElementAttribute(element, "autocomplete", "on");

            return textField;
        } else {
            return null;
        }
    }

    private void login() {
        getWidget().submit();
        valuesChanged();
        loginFormRpc.submitted();
    }

    private void addSubmitButtonClickHandler(Connector buttonConnector) {
        if (buttonConnector != null) {
            VButton button = ((ButtonConnector)buttonConnector).getWidget();
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    login();
                }
            });
        }
    }

    private void valuesChanged() {
        if (passwordField != null) {
            passwordField.valueChange(true);
        }
        if (userField != null) {
            userField.valueChange(true);
        }
    }

}
