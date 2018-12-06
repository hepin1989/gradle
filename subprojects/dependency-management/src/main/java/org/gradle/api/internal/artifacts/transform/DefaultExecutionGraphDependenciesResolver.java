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

package org.gradle.api.internal.artifacts.transform;

import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ProjectComponentIdentifier;
import org.gradle.api.artifacts.result.DependencyResult;
import org.gradle.api.artifacts.result.ResolvedComponentResult;
import org.gradle.api.artifacts.result.ResolvedDependencyResult;
import org.gradle.api.internal.artifacts.ResolverResults;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.artifact.SelectedArtifactSet;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.artifact.VisitedArtifactSet;
import org.gradle.api.internal.tasks.TaskDependencyContainer;
import org.gradle.api.internal.tasks.TaskDependencyResolveContext;
import org.gradle.api.internal.tasks.WorkNodeAction;
import org.gradle.api.specs.Specs;

import java.util.HashSet;
import java.util.Set;

public class DefaultExecutionGraphDependenciesResolver implements ExecutionGraphDependenciesResolver {
    private final ComponentIdentifier componentIdentifier;
    private final VisitedArtifactSet visitedArtifacts;
    private final ResolverResults results;
    private final WorkNodeAction graphResolveAction;
    private Set<ComponentIdentifier> buildDependencies;

    public DefaultExecutionGraphDependenciesResolver(ComponentIdentifier componentIdentifier, ResolverResults results, WorkNodeAction graphResolveAction) {
        this.componentIdentifier = componentIdentifier;
        this.visitedArtifacts = results.getVisitedArtifacts();
        this.results = results;
        this.graphResolveAction = graphResolveAction;
    }

    @Override
    public TaskDependencyContainer computeDependencyNodes(TransformationStep transformationStep) {
        if (!transformationStep.requiresDependencies()) {
            return TaskDependencyContainer.EMPTY;
        } else {
            return new TaskDependencyContainer() {
                @Override
                public void visitDependencies(TaskDependencyResolveContext context) {
                    if (buildDependencies == null) {
                        buildDependencies = computeProjectDependencies(componentIdentifier, results.getResolutionResult().getAllComponents());
                    }
                    if (!buildDependencies.isEmpty()) {
                        SelectedArtifactSet projectArtifacts = visitedArtifacts.select(Specs.satisfyAll(), transformationStep.getFromAttributes(), element -> {
                            return buildDependencies.contains(element);
                        }, true);
                        context.add(projectArtifacts);
                    }
                    context.add(graphResolveAction);
                }
            };
        }
    }

    private static Set<ComponentIdentifier> computeProjectDependencies(ComponentIdentifier componentIdentifier, Set<ResolvedComponentResult> componentResults) {
        ResolvedComponentResult targetComponent = null;
        for (ResolvedComponentResult component : componentResults) {
            if (component.getId().equals(componentIdentifier)) {
                targetComponent = component;
                break;
            }
        }
        if (targetComponent == null) {
            throw new AssertionError("Could not find component " + componentIdentifier + " in provided results.");
        }
        Set<ComponentIdentifier> buildDependencies = new HashSet<>();
        collectDependenciesIdentifiers(buildDependencies, new HashSet<>(), targetComponent.getDependencies());
        return buildDependencies;
    }

    private static void collectDependenciesIdentifiers(Set<ComponentIdentifier> dependenciesIdentifiers, Set<ComponentIdentifier> visited, Set<? extends DependencyResult> dependencies) {
        for (DependencyResult dependency : dependencies) {
            if (dependency instanceof ResolvedDependencyResult) {
                ResolvedDependencyResult resolvedDependency = (ResolvedDependencyResult) dependency;
                ResolvedComponentResult selected = resolvedDependency.getSelected();
                if (selected.getId() instanceof ProjectComponentIdentifier) {
                    dependenciesIdentifiers.add(selected.getId());
                }
                if (visited.add(selected.getId())) {
                    // Do not traverse if seen already
                    collectDependenciesIdentifiers(dependenciesIdentifiers, visited, selected.getDependencies());
                }
            }
        }
    }

}