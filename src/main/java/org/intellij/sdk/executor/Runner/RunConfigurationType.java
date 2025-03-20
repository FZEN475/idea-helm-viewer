package org.intellij.sdk.executor.Runner;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NotNullLazyValue;

final class RunConfigurationType extends ConfigurationTypeBase {

    static final String ID = "HelmViewerRunConfiguration";

    RunConfigurationType() {
        super(ID, "helm-viewer", "helm-viewer run configuration",
                NotNullLazyValue.createValue(() -> AllIcons.Nodes.Console));
        addFactory(new myConfigurationFactory(this));

    }

}