package com.ejt.vaadin.loginform.shared;

import com.ejt.vaadin.loginform.LoginForm;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.ejt.vaadin.loginform.client.LoginFormContainerGWT;

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
public class LoginFormContainerConnector extends AbstractSingleComponentContainerConnector {

    public static final String LOGIN_URL = "/loginForm";

    private VTextField passwordField;
    private VTextField userField;

    @Override
    public void updateCaption(ComponentConnector connector) {

    }

    @Override
    public LoginFormContainerGWT getWidget() {
        return (LoginFormContainerGWT)super.getWidget();
    }

            @Override
    public LoginFormContainerState getState() {
        return (LoginFormContainerState)super.getState();
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

        LoginFormContainerState state = getState();
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
                        valuesChanged();
                        getWidget().submit();
                    }
                }
            });

            Element element = textField.getElement();
            element.setId(id);
            DOM.setElementAttribute(element, "name", id);
            DOM.setElementAttribute(element, "autocomplete", "on");

            return textField;
        } else {
            return null;
        }
    }

    private void addSubmitButtonClickHandler(Connector buttonConnector) {
        if (buttonConnector != null) {
            VButton button = ((ButtonConnector)buttonConnector).getWidget();
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    // Vaadin click listener is fired first, so it's too late to call valuesChanged() here
                    getWidget().submit();
                }
            });

            button.addFocusHandler(new FocusHandler() {
                @Override
                public void onFocus(FocusEvent event) {
                    valuesChanged();
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
