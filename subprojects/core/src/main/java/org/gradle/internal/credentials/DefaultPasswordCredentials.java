/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.internal.credentials;

import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.internal.provider.DefaultProvider;
import org.gradle.api.provider.Provider;

public class DefaultPasswordCredentials implements PasswordCredentials {
    private Provider<String> usernameProvider;
    private Provider<String> passwordProvider;

    public DefaultPasswordCredentials() {
    }

    public DefaultPasswordCredentials(String username, String password) {
        this.usernameProvider = new DefaultProvider<>(() -> username);
        this.passwordProvider = new DefaultProvider<>(() -> password);
    }

    @Override
    public String getUsername() {
        if (usernameProvider != null) {
            return usernameProvider.get();
        }
        return null;
    }

    @Override
    public void setUsername(String username) {
        this.usernameProvider = new DefaultProvider<>(() -> username);
    }

    public Provider<String> getUsernameProvider() {
        return usernameProvider;
    }

    @Override
    public void setUsername(Provider<String> username) {
        this.usernameProvider = username;
    }

    @Override
    public String getPassword() {
        if (passwordProvider != null) {
            return passwordProvider.get();
        }
        return null;
    }

    @Override
    public void setPassword(String password) {
        this.passwordProvider = new DefaultProvider<>(() -> password);
    }

    @Override
    public void setPassword(Provider<String> password) {
        this.passwordProvider = password;
    }

    public Provider<String> getPasswordProvider() {
        return passwordProvider;
    }

    @Override
    public String toString() {
        return String.format("Credentials [username: %s]", usernameProvider != null ? usernameProvider.get() : null);
    }
}
