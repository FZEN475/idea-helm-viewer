package org.intellij.sdk.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class AppSettingsConfigurable implements SearchableConfigurable, Configurable.NoScroll, Disposable {
    private AppSettingsComponent mySettingsComponent;
    private final Project project;

    public AppSettingsConfigurable(Project project) {
        this.project = project;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Helm Viewer Settings.";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent(this::disposeUIResources, project);
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        return mySettingsComponent.isModified();
    }

    @Override
    public void apply() {
        mySettingsComponent.apply();
    }

    @Override
    public void reset() {
        mySettingsComponent.reset();
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent.disposeUIResources();
    }

    @Override
    public void dispose() {

    }

    @Override
    public @NotNull @NonNls String getId() {
        return "";
    }
}
