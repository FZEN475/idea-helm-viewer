package org.intellij.sdk.executor.Handlers;

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.intellij.sdk.executor.Action;
import org.jetbrains.annotations.NotNull;

public class MyBackspaceHandler extends BackspaceHandlerDelegate {

    @Override
    public void beforeCharDeleted(char c, @NotNull PsiFile psiFile, @NotNull Editor editor) {
    }

    @Override
    public boolean charDeleted(char c, @NotNull PsiFile psiFile, @NotNull Editor editor) {
        Action.run(psiFile,editor);

        return false;
    }
}
