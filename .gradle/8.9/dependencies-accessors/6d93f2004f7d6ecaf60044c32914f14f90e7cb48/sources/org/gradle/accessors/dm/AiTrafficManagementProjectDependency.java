package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.internal.artifacts.dependencies.ProjectDependencyInternal;
import org.gradle.api.internal.artifacts.DefaultProjectDependencyFactory;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.api.internal.catalog.DelegatingProjectDependency;
import org.gradle.api.internal.catalog.TypeSafeProjectDependencyFactory;
import javax.inject.Inject;

@NonNullApi
public class AiTrafficManagementProjectDependency extends DelegatingProjectDependency {

    @Inject
    public AiTrafficManagementProjectDependency(TypeSafeProjectDependencyFactory factory, ProjectDependencyInternal delegate) {
        super(factory, delegate);
    }

    /**
     * Creates a project dependency on the project at path ":composeApp"
     */
    public ComposeAppProjectDependency getComposeApp() { return new ComposeAppProjectDependency(getFactory(), create(":composeApp")); }

    /**
     * Creates a project dependency on the project at path ":server"
     */
    public ServerProjectDependency getServer() { return new ServerProjectDependency(getFactory(), create(":server")); }

    /**
     * Creates a project dependency on the project at path ":shared"
     */
    public SharedProjectDependency getShared() { return new SharedProjectDependency(getFactory(), create(":shared")); }

}
