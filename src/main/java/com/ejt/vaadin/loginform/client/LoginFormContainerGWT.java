package com.ejt.vaadin.loginform.client;

import com.google.gwt.user.client.ui.FormPanel;
import com.ejt.vaadin.loginform.shared.LoginFormContainerConnector;

public class LoginFormContainerGWT extends FormPanel {
    public LoginFormContainerGWT() {
        getElement().setId("loginForm");
        setMethod(METHOD_POST);
        setAction(LoginFormContainerConnector.LOGIN_URL);
    }
}
