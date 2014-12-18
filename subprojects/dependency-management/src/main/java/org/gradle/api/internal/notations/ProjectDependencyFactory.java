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
package org.gradle.api.internal.notations;

import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.internal.artifacts.DefaultProjectDependencyFactory;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.internal.typeconversion.*;
import org.gradle.api.tasks.Optional;

import java.util.Collection;
import java.util.Map;

public class ProjectDependencyFactory {
    private final DefaultProjectDependencyFactory factory;

    public ProjectDependencyFactory(DefaultProjectDependencyFactory factory) {
        this.factory = factory;
    }

    public ProjectDependency createFromMap(ProjectFinder projectFinder, Map<? extends String, ?> map) {
        return NotationParserBuilder.toType(ProjectDependency.class)
                .converter(new ProjectDependencyMapNotationConverter(projectFinder, factory)).toComposite().parseNotation(map);
    }

    static class ProjectDependencyMapNotationConverter extends MapNotationConverter<ProjectDependency> {

        private final ProjectFinder projectFinder;
        private final DefaultProjectDependencyFactory factory;

        public ProjectDependencyMapNotationConverter(ProjectFinder projectFinder, DefaultProjectDependencyFactory factory) {
            this.projectFinder = projectFinder;
            this.factory = factory;
        }

        protected ProjectDependency parseMap(@MapKey("path") String path, @Optional @MapKey("configuration") String configuration) {
            return factory.create(projectFinder.getProject(path), configuration);
        }

        public void describe(Collection<String> candidateFormats) {
            candidateFormats.add("Map with mandatory 'path' and optional 'configuration' key, e.g. [path: ':someProj', configuration: 'someConf']");
        }
    }
}
