import org.gradle.gradlebuild.unittestandcompile.ModuleType

/*
 * Copyright 2017 the original author or authors.
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
plugins {
    id("java-library")
    id("gradlebuild.strict-compile")
    id("gradlebuild.classycle")
}

dependencies {
    api(project(":baseServices"))
    api(project(":messaging"))
    api(project(":native"))
    api(project(":resources"))
    api(project(":logging"))
    api(library("jcip"))

    implementation(library("commons_collections"))
    implementation(library("commons_io"))
    implementation(library("commons_lang"))
}

gradlebuildJava {
    moduleType = ModuleType.CORE
}

testFixtures {
    from(":core")
}
