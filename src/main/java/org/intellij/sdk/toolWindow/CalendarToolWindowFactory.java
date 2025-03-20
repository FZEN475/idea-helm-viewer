package org.intellij.sdk.toolWindow;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.intellij.sdk.settings.AppSettingsComponent;
import org.jetbrains.annotations.NotNull;

public class CalendarToolWindowFactory implements ToolWindowFactory, DumbAware {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        Language language = Language.findLanguageByID("yaml");
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText("idea-helm-viewer.yaml", language,"");
        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);

        EditorTextField etf = new EditorTextField(document,project, language.getAssociatedFileType(),true,false);

        TextRange tr = new TextRange(0,document.getTextLength());
        etf.addSettingsProvider(editor -> {
            editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
            editor.setVerticalScrollbarVisible(true);
            editor.setHorizontalScrollbarVisible(true);
            editor.getSettings().setAdditionalLinesCount(2);
            editor.getSettings().setWhitespacesShown(false);
            editor.getSettings().setLineNumbersShown(true);
            editor.getSettings().setVariableInplaceRenameEnabled(true);
            editor.getSettings().setAutoCodeFoldingEnabled(true);
            editor.getSettings().setBlinkCaret(true);
            editor.getSettings().setCustomSoftWrapIndent(4);
            editor.getSettings().setAdditionalPageAtBottom(true);
            editor.getSettings().setIndentGuidesShown(true);
            editor.getSettings().setFoldingOutlineShown(true);
            editor.getSettings().setLineMarkerAreaShown(true);
            editor.getSettings().setShowIntentionBulb(true);
            editor.getSettings().setTabSize(4);
            editor.getSettings().setWheelFontChangeEnabled(true);
            editor.getSettings().setRightMarginShown(true);
            editor.getSettings().setUseTabCharacter(true);
            editor.getSettings().setSmartHome(true);
            editor.getSettings().setRightMargin(10);
            editor.getSettings().setDndEnabled(true);
            editor.getSettings().setCamelWords(true);
            editor.getSettings().setAutoCodeFoldingEnabled(true);
            editor.getCaretModel().moveToOffset(tr.getStartOffset());
            editor.getScrollingModel().scrollVertically(document.getTextLength() - 1);
            editor.getSelectionModel().setSelection(tr.getStartOffset(),tr.getEndOffset());

        });
        Content content = ContentFactory.getInstance().createContent(etf, "Viewer", true);
        toolWindow.getContentManager().addContent(content);
        AppSettingsComponent settingsPanel = new AppSettingsComponent(toolWindow.getDisposable(),project);
        settingsPanel.reset();
        settingsPanel.addApplyButton();
        Content tab2 = ContentFactory.getInstance().createContent(settingsPanel.getPanel(), "Settings", true);
        toolWindow.getContentManager().addContent(tab2);
    }

}