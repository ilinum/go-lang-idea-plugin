package ro.redeul.google.go.lang.psi.resolve.references;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import ro.redeul.google.go.lang.psi.GoFile;
import ro.redeul.google.go.lang.psi.processors.ResolveStates;
import ro.redeul.google.go.lang.psi.resolve.refs.TypeNameSolver;
import ro.redeul.google.go.lang.psi.resolve.refs.TypeNameReference;
import ro.redeul.google.go.lang.psi.toplevel.GoTypeNameDeclaration;
import ro.redeul.google.go.lang.psi.types.GoPsiTypeName;
import ro.redeul.google.go.lang.stubs.GoNamesCache;

import java.util.Collection;

public class BuiltinTypeNameReference extends TypeNameReference {

    public BuiltinTypeNameReference(GoPsiTypeName element) {
        super(element);
    }

    @Override
    public PsiElement resolve() {

        PsiElement element = getElement();

        TypeNameSolver processor = new TypeNameSolver(this);

        GoNamesCache namesCache = GoNamesCache.getInstance(element.getProject());

        // get the file included in the imported package name
        Collection<GoFile> files = namesCache.getBuiltinPackageFiles();

        for (GoFile file : files) {
            if (!file.processDeclarations(processor, ResolveStates.builtins(), null, element))  {
                break;
            }
        }

        return processor.getDeclaration();
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        GoPsiTypeName typeElement = getElement();

        if (element instanceof GoTypeNameDeclaration) {
            GoTypeNameDeclaration nameDeclaration = (GoTypeNameDeclaration)element;
            return typeElement.getText().equals(nameDeclaration.getName());
        }

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isSoft() {
        return false;
    }
}
