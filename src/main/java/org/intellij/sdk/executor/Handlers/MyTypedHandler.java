package org.intellij.sdk.executor.Handlers;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import org.intellij.sdk.executor.Action;
import org.jetbrains.annotations.NotNull;

/**
 * This is a custom {@link TypedHandlerDelegate} that handles actions activated keystrokes in the editor.
 * The execute method inserts a fixed string at Offset 0 of the document.
 * Document changes are made in the context of a write action.
 */
final class MyTypedHandler extends TypedHandlerDelegate {

    @NotNull
    @Override
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        
        Action.run(file,editor);

        return Result.CONTINUE;
    }

}