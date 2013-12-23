package com.ejt.vaadin.loginform.client;

import com.ejt.vaadin.loginform.shared.LoginFormConnector;
import com.google.gwt.user.client.ui.FormPanel;

public class LoginFormGWT extends FormPanel {
    public LoginFormGWT() {
        getElement().setId("loginForm");
        setMethod(METHOD_POST);
        setAction(LoginFormConnector.LOGIN_URL);
    }
}
