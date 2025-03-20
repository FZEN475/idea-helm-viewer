package org.intellij.sdk.executor.Runner;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.runners.ExecutionEnvironment;

import com.intellij.execution.util.ExecUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.EditorTextField;
import org.intellij.sdk.executor.Action;
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
        GeneralCommandLine commandLine = Action.prepareCommandLine(false,this.getProject());
        System.out.println(commandLine.getCommandLineString());
        System.out.println(project.getBasePath());
        ProcessOutput result = null;
        try {
            result = ExecUtil.execAndGetOutput(commandLine);
        } catch (ExecutionException e) {
            //throw new RuntimeException(e);
        }
        Integer exitCode = result.getExitCode();
        String STDOut = result.getStdout();
        String STDError = result.getStderr();
        System.out.println(exitCode);
        System.out.println(STDOut);
        System.out.println(STDError);
        switch(exitCode) {
            case 0:
                ApplicationManager.getApplication().invokeLater(() -> {
                    WriteAction.run(() -> {
                        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Helm viewer");
                        EditorTextField etf = (EditorTextField)toolWindow.getContentManager().getContents()[0].getComponent();
                        etf.setAutoscrolls(true);
                        etf.getDocument().setText(STDOut);
                    });
                });
                break;
            default:
                ApplicationManager.getApplication().invokeLater(() -> {
                    WriteAction.run(() -> {
                        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Helm viewer");
                        EditorTextField etf = (EditorTextField)toolWindow.getContentManager().getContents()[0].getComponent();
                        etf.setAutoscrolls(true);
                        etf.getDocument().setText("Exit code: "+exitCode+"\n\n\n"+STDOut+"\n\n"+STDError);
                    });
                });
                ApplicationManager.getApplication().invokeLater(() -> {
                    Notifications.Bus.notify(new Notification("Viewer Notification Group",
                            "Exit code: "+exitCode, STDOut+"\n\n"+STDError, NotificationType.ERROR), project);
                });
        }
        return null;
    }

}