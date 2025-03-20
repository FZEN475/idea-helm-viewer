package org.intellij.sdk.executor;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.util.ExecUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.intellij.sdk.settings.AppSettings;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TaskUpdateDependency extends Task.Backgroundable {

    public TaskUpdateDependency(Project project, String title ){
        super(project, title, true);


    }

    private @NotNull GeneralCommandLine prepareCommandLine(){
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        GeneralCommandLine commandLine =
                new GeneralCommandLine(state.helm_path);

        commandLine.addParameter("dependency");
        commandLine.addParameter("update");
        if (!state.ca_path.isEmpty()){
            commandLine.addParameter("--ca-file");
            commandLine.addParameter(state.ca_path);
        }
        if (!state.user.isEmpty()){
            commandLine.addParameter("--username");
            commandLine.addParameter(state.user);
        }
        if (!state.password.isEmpty()){
            commandLine.addParameter("--password");
            commandLine.addParameter(state.password);
        }
        commandLine.addParameter("."+state.chart_path);
        commandLine.setCharset(StandardCharsets.UTF_8);
        commandLine.setWorkDirectory(this.getProject().getBasePath());
        return commandLine;
    }

    @Override
    public void run(@NotNull ProgressIndicator progressIndicator) {
        progressIndicator.setText("Start update.");
        progressIndicator.setFraction(0.10);
        GeneralCommandLine commandLine = prepareCommandLine();
        ProcessOutput result = null;
        try {
            result = ExecUtil.execAndGetOutput(commandLine);
        } catch (ExecutionException e) {
            //throw new RuntimeException(e);
        }
        int exitCode = result.getExitCode();
        String STDOut = result.getStdout();
        String STDError = result.getStderr();
        if (exitCode == 0){
            ApplicationManager.getApplication().invokeLater(() -> {
                Notifications.Bus.notify(new Notification("Viewer Notification Group",
                        "Dependency is up to date!", NotificationType.INFORMATION), this.getProject());
            });
        }else {
            ApplicationManager.getApplication().invokeLater(() -> {
                Notifications.Bus.notify(new Notification("Viewer Notification Group",
                        "Exit code: "+exitCode, STDOut+"\n\n"+STDError, NotificationType.ERROR), this.getProject());
            });
        }
        // Finished
        progressIndicator.setFraction(1.0);
        progressIndicator.setText("Finished.");
    }
}
