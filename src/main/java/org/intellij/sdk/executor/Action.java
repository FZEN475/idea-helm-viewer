package org.intellij.sdk.executor;

import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.intellij.sdk.executor.Runner.myConfigurationFactory;
import org.intellij.sdk.executor.Runner.myRunConfiguration;
import org.intellij.sdk.settings.AppSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class Action {
    public static void run(@NotNull PsiFile file, Editor editor){

        Project project = file.getProject();
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());

        FileDocumentManager.getInstance().saveDocument(editor.getDocument());
        if (!state.chart_path.isEmpty() && !state.helm_path.isEmpty()
                && file.getVirtualFile().getPath().contains(project.getBasePath()+state.chart_path)){
            ConfigurationType type = ConfigurationTypeUtil.findConfigurationType("HelmViewerRunConfiguration");

            Arrays.stream(type.getConfigurationFactories()).forEach(e -> {
                if (e instanceof myConfigurationFactory) {
                    myRunConfiguration rc = (myRunConfiguration) e.createConfiguration("helm template",
                            RunManager.getInstance(project).getConfigurationTemplate(e).getConfiguration());
                    try {
                        rc.checkConfiguration();
                    } catch (RuntimeConfigurationException ex) {
                        throw new RuntimeException(ex);
                    }
                    RunnerAndConfigurationSettings racs  = RunManager.getInstance(project).createConfiguration(rc,e);

                    ProgramRunnerUtil.executeConfiguration(racs, DefaultRunExecutor.getRunExecutorInstance());

                }

            });
        }
    }
}
