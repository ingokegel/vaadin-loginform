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
