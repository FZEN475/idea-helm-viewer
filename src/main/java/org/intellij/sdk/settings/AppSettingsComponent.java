package org.intellij.sdk.settings;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentValidator;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.intellij.sdk.executor.TaskUpdateDependency;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.io.File;
import java.util.Objects;

public class AppSettingsComponent {
    private static final String MESSAGE = "File not exist!";
    private final AppSettings.State state =
            Objects.requireNonNull(AppSettings.getInstance().getState());
    private final JPanel myMainPanel;
    private final JBTextField chartPathField = new JBTextField();
    private final JBTextField valuesPathField = new JBTextField();
    private final TextFieldWithBrowseButton helmPath = new TextFieldWithBrowseButton();
    private final TextFieldWithBrowseButton caFile = new TextFieldWithBrowseButton();
    private final JBTextField user = new JBTextField();
    private final JPasswordField password = new JPasswordField();
    private final JButton Apply = new JButton();
    private final JButton Refrash = new JButton();
    public AppSettingsComponent(Disposable parentDisposable,Project project){
        new ComponentValidator(parentDisposable).withValidator(() -> {
            if (StringUtil.isNotEmpty(chartPathField.getText())) {
                try {
                    Boolean pathExists = new File(project.getBasePath()+chartPathField.getText()).exists();
                    Boolean chartExists = new File(project.getBasePath()+chartPathField.getText()+"Chart.yaml").exists();
                    if (pathExists && chartExists){
                        return null;
                    }else {
                        return new ValidationInfo("Check Chart.yaml exists. ("+
                                project.getBasePath()+chartPathField.getText()+"Chart.yaml"+")", chartPathField);
                    }
                } catch (NumberFormatException nfe) {
                    return new ValidationInfo(nfe.getMessage(), chartPathField);
                }
            } else {
                return null;
            }
        }).installOn(chartPathField);
        chartPathField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ComponentValidator.getInstance(chartPathField)
                        .ifPresent(ComponentValidator::revalidate);
            }
        });

        new ComponentValidator(parentDisposable).withValidator(() -> {
            if (StringUtil.isNotEmpty(valuesPathField.getText())) {
                try {
                    boolean valuesExists = new File(project.getBasePath()+valuesPathField.getText()).exists();
                    if (valuesExists){
                        return null;
                    }else {
                        return new ValidationInfo("Check values exists. ("+
                                project.getBasePath()+valuesPathField.getText()+")", valuesPathField);
                    }
                } catch (NumberFormatException nfe) {
                    return new ValidationInfo(nfe.getMessage(), valuesPathField);
                }
            } else {
                return null;
            }
        }).installOn(valuesPathField);
        valuesPathField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ComponentValidator.getInstance(valuesPathField)
                        .ifPresent(ComponentValidator::revalidate);
            }
        });

        helmPath.addBrowseFolderListener(new TextBrowseFolderListener(
                new FileChooserDescriptor(true,false,false,false,false,false)));
        new ComponentValidator(parentDisposable).withValidator(() -> {
            if (StringUtil.isNotEmpty(chartPathField.getText())) {
                try {
                    if (new File(helmPath.getText()).exists()){
                        return null;
                    }else {
                        return new ValidationInfo(MESSAGE, helmPath.getTextField());
                    }

                } catch (NumberFormatException nfe) {
                    return new ValidationInfo(nfe.getMessage(), helmPath.getTextField());
                }
            } else {
                return null;
            }
        }).installOn(helmPath.getTextField());
        helmPath.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ComponentValidator.getInstance(helmPath.getTextField())
                        .ifPresent(ComponentValidator::revalidate);
            }
        });

        JButton updateDependency = new JButton();
        updateDependency.setText("Update chart dependency.");
        updateDependency.addActionListener(e -> {
            TaskUpdateDependency taskUpdateDependency = new TaskUpdateDependency(project,"Update chart dependency.");
            ProgressManager.getInstance().run(taskUpdateDependency);

        });
        caFile.addBrowseFolderListener(new TextBrowseFolderListener(
                new FileChooserDescriptor(true,false,false,false,false,false)));
        new ComponentValidator(parentDisposable).withValidator(() -> {
            if (StringUtil.isNotEmpty(caFile.getText())) {
                try {
                    if (new File(caFile.getText()).exists() || caFile.getText().isEmpty()){
                        return null;
                    }else {
                        return new ValidationInfo(MESSAGE, caFile.getTextField());
                    }

                } catch (NumberFormatException nfe) {
                    return new ValidationInfo(nfe.getMessage(), caFile.getTextField());
                }
            } else {
                return null;
            }
        }).installOn(caFile.getTextField());
        caFile.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ComponentValidator.getInstance(caFile.getTextField())
                        .ifPresent(ComponentValidator::revalidate);
            }
        });

        Apply.setText("Apply");
        Apply.setVisible(false);
        Apply.addActionListener(e ->{
                    this.apply();
                }
        );

        Refrash.setText("Refrash");
        Refrash.setVisible(false);
        Refrash.addActionListener(e ->{
            this.reset();
                }
        );
        JPanel controllPanel = new JPanel();
        controllPanel.add(Apply);
        controllPanel.add(Refrash);
        controllPanel.setLayout(new BoxLayout(controllPanel, BoxLayout.X_AXIS));

        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Project chart path:"), chartPathField, 1, true)
                .addLabeledComponent(new JBLabel("Values path:"), valuesPathField, 1, true)
                .addLabeledComponent(new JBLabel("Helm path:"), helmPath, 1, true)
                .addComponent(updateDependency,1)
                .addLabeledComponent(new JBLabel("Ca.crt (optional):"), caFile, 1, true)
                .addLabeledComponent(new JBLabel("User (optional):"), user, 1, true)
                .addLabeledComponent(new JBLabel("Password (optional):"), password, 1, true)
                .addComponent( controllPanel,1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public boolean isModified(){
        return !chartPathField.getText().equals(state.chart_path)
                || !valuesPathField.getText().equals(state.values_path)
                || !helmPath.getText().equals(state.helm_path)
                || !caFile.getText().equals(state.ca_path)
                || !user.getText().equals(state.user)
                || !String.valueOf(password.getPassword()).equals(state.password)
                ;
    }

    public void apply(){
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        if (chartPathField.isValid()){
            state.chart_path = chartPathField.getText();
        }
        if (valuesPathField.isValid() ){
            state.values_path = valuesPathField.getText();
        }

        if (helmPath.isValid()){
            state.helm_path = helmPath.getText();
        }
        if (caFile.isValid()){
            state.ca_path = caFile.getText();
        }
        if (user.isValid()){
            state.user = user.getText();
        }
        if (password.isValid()){
            state.password = String.valueOf(password.getPassword());
        }
    }

    public void reset(){
        chartPathField.setText(state.chart_path);
        valuesPathField.setText(state.values_path);
        helmPath.setText(state.helm_path);
        caFile.setText(state.ca_path);
        user.setText(state.user);
        password.setText(state.password);
    }

    public void disposeUIResources(){
        chartPathField.setText("");
        valuesPathField.setText("");
        helmPath.setText("");
        caFile.setText("");
        user.setText("");
        password.setText("");
    }

    public JPanel getPanel() {
        return myMainPanel;
    }
    public JComponent getPreferredFocusedComponent() {
        return chartPathField;
    }

    public void addApplyButton(){
        Apply.setVisible(true);
        Refrash.setVisible(true);
    }
}
