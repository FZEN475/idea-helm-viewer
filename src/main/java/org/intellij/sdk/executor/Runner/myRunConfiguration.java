package org.intellij.sdk.executor.Runner;

import com.intellij.execution.Executor;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;

import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.intellij.sdk.executor.mySettingsEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class myRunConfiguration extends RunConfigurationBase<RunConfigurationOptions> {
    private final Project project;
    protected myRunConfiguration(Project project,
                               ConfigurationFactory factory,
                               String name) {
        super(project, factory, name);
        this.project = project;
    }

    @NotNull
    @Override
    public RunConfigurationOptions getOptions() {
        return super.getOptions();
    }

    @NotNull
    @Override
    public SettingsEditor<? extends myRunConfiguration> getConfigurationEditor() {
        return new mySettingsEditor(project);
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) {
        RunnerAndConfigurationSettings racs  = RunManager.getInstance(project).createConfiguration(this,this.getFactory());
        ProgramRunnerUtil.executeConfiguration(racs, DefaultRunExecutor.getRunExecutorInstance());
        return null;
    }

}