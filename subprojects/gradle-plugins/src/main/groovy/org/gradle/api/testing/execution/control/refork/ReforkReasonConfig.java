/*
 * Copyright 2009 the original author or authors.
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
package org.gradle.api.testing.execution.control.refork;

import java.io.Serializable;

/**
 * @author Tom Eyckmans
 */
public interface ReforkReasonConfig extends Serializable {

    ReforkReasonKey getKey();

    /**
     *
     * Each implementation of this interface should have the following methods to control serialization: 
     *
     * private void writeObject(ObjectOutputStream out) throws IOException {
     *
     * }
     *
     * private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
     *
     * }
     *
     */
}
