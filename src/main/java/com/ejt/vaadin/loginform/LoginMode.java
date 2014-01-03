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

/**
 * Determines the way in which the login form reacts to a login on the server side.
 * The login mode is set by calling {@link LoginForm#setLoginMode(LoginMode)}.
 */
public enum LoginMode {
    /**
     * Direct mode means that {@link LoginForm#login(String, String)} will be called as soon as the user clicks on the login
     * button or presses the enter key in the user name or password text fields.
     * In direct mode, you cannot change the URL in the {@link LoginForm#login(String, String)} method, otherwise the
     * password manager will not be triggered.
     * <p/>
     * This is the default mode for a new login form instance.
     */
    DIRECT,

    /**
     * Deferred mode means that {@link LoginForm#login(String, String)} will be called after the dummy form submission that
     * triggers the password manager has completed. In deferred mode, it is possible to change the URL
     * in the {@link LoginForm#login(String, String)} method. The drawbacks with resepect to deferred mode are the following:
     * <ul>
     * <li>There will be a slight UI lag between the user action and the UI change</li>
     * <li>Any UI change
     * resulting from the login is not a direct consequence of the user input. If you use Vaadin TestBench, you
     * have to add your own code to wait for any UI changes.</li>
     * </ul>
     */
    DEFERRED;
}
