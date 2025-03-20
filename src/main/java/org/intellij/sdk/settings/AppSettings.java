package org.intellij.sdk.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

@State(
        name = "org.intellij.sdk.settings.AppSettings",
        storages = @Storage("SdkSettingsPlugin.xml")
)

public class AppSettings implements PersistentStateComponent<AppSettings.State> {

    public static class State {
        @NonNls
        public String chart_path = "/chart/";
        public String values_path = "/chart/values.yaml";
        public String helm_path = "";
        public String ca_path = "";
        public String user = "";
        public String password = "";

    }

    private State myState = new State();

    public static AppSettings getInstance() {
        return ApplicationManager.getApplication()
                .getService(AppSettings.class);
    }

    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }
}
