package org.intellij.sdk.executor.Runner;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.intellij.sdk.executor.myRunConfigurationOptions;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class myConfigurationFactory extends ConfigurationFactory {
    private static final String NAME = "HELM-EDITOR";
    protected myConfigurationFactory(ConfigurationType type) {
        super(type);

    }

    @Override
    public @NotNull @Nls String getName() {
        return NAME;
    }

    @Override
    public @NotNull String getId() {
        return RunConfigurationType.ID;
    }

    @NotNull
    @Override
    public myRunConfiguration createTemplateConfiguration(
            @NotNull Project project) {
        return new myRunConfiguration(project, this, "Helm-viewer");
    }

    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return myRunConfigurationOptions.class;
    }

}