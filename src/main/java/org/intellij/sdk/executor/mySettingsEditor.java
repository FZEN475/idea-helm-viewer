package org.intellij.sdk.executor;

import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.intellij.sdk.executor.Runner.myRunConfiguration;
import org.intellij.sdk.settings.AppSettingsComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class mySettingsEditor extends SettingsEditor<myRunConfiguration> {
    private final AppSettingsComponent settingsPanel;

    public mySettingsEditor(Project project) {
        settingsPanel = new AppSettingsComponent(this,project);
    }

    @Override
    protected void resetEditorFrom(@NotNull myRunConfiguration RunConfiguration) {
        settingsPanel.reset();
    }

    @Override
    protected void applyEditorTo(@NotNull myRunConfiguration RunConfiguration) {
        settingsPanel.apply();
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        settingsPanel.reset();
        return settingsPanel.getPanel();
    }

}