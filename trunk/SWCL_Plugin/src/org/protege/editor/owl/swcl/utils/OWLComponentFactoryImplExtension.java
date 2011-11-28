package org.protege.editor.owl.swcl.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.protege.editor.core.ProtegeApplication;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLClassDescriptionEditor;
import org.protege.editor.owl.ui.editor.OWLClassExpressionEditor;
import org.protege.editor.owl.ui.editor.OWLClassExpressionEditorPlugin;
import org.protege.editor.owl.ui.editor.OWLClassExpressionEditorPluginLoader;
import org.protege.editor.owl.ui.selector.OWLClassSelectorPanel;
import org.protege.editor.owl.ui.selector.OWLDataPropertySelectorPanel;
import org.protege.editor.owl.ui.selector.OWLIndividualSelectorPanel;
import org.protege.editor.owl.ui.selector.OWLObjectPropertySelectorPanel;
import org.protege.editor.owl.ui.util.OWLComponentFactory;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * @author parklize
 * @version 1.0, 2011-11-23
 */
public class OWLComponentFactoryImplExtension implements OWLComponentFactory {
    private OWLEditorKit eKit;

    private OWLClassSelectorPanel classSelectorPanel;

    private OWLObjectPropertySelectorPanel objectPropertySelectorPanel;

    private OWLDataPropertySelectorPanel dataPropertySelectorPanel;

    private OWLIndividualSelectorPanel individualSelectorPanel;

    private List<OWLClassExpressionEditorPlugin> descriptionEditorPlugins;
    
    public OWLComponentFactoryImplExtension(OWLEditorKit eKit) {
        this.eKit = eKit;
    }


    public OWLClassDescriptionEditor getOWLClassDescriptionEditor(OWLClassExpression expr) {
        return getOWLClassDescriptionEditor(expr, null);
    }

    @SuppressWarnings("unchecked")
    public OWLClassDescriptionEditor getOWLClassDescriptionEditor(OWLClassExpression expr, AxiomType type) {
        OWLClassDescriptionEditor editor = new OWLClassDescriptionEditor(eKit, expr);
        Iterator it = getDescriptionEditorPlugins().iterator();
        if (it.hasNext()) {
            try {
                if (type == null || ((OWLClassExpressionEditorPlugin) it.next()).isSuitableFor(type)){
                    OWLClassExpressionEditor editorPanel = ((OWLClassExpressionEditorPlugin) it.next()).newInstance();
                    if (type != null){
                        editorPanel.setAxiomType(type);
                    }
                    editorPanel.initialise();
                    editor.addPanel(editorPanel);
                }
            }
            catch (Throwable e) { // be harsh if any problems with a plugin
                ProtegeApplication.getErrorLog().logError(e);
            }
        }
        editor.selectPreferredEditor();
        return editor;
    }

    public OWLClassSelectorPanel getOWLClassSelectorPanel() {
        if (classSelectorPanel == null) {
            classSelectorPanel = new OWLClassSelectorPanel(eKit);
        }
        return classSelectorPanel;
    }


    public OWLObjectPropertySelectorPanel getOWLObjectPropertySelectorPanel() {
        if (objectPropertySelectorPanel == null) {
            objectPropertySelectorPanel = new OWLObjectPropertySelectorPanel(eKit);
        }
        return objectPropertySelectorPanel;
    }


    public OWLDataPropertySelectorPanel getOWLDataPropertySelectorPanel() {
        if (dataPropertySelectorPanel == null) {
            dataPropertySelectorPanel = new OWLDataPropertySelectorPanel(eKit);
        }
        return dataPropertySelectorPanel;
    }


    public OWLIndividualSelectorPanel getOWLIndividualSelectorPanel() {
        if (individualSelectorPanel == null) {
            individualSelectorPanel = new OWLIndividualSelectorPanel(eKit);
        }
        return individualSelectorPanel;
    }


    public void dispose() {
        if (classSelectorPanel != null) {
            classSelectorPanel.dispose();
        }
        if (objectPropertySelectorPanel != null) {
            objectPropertySelectorPanel.dispose();
        }
        if (dataPropertySelectorPanel != null) {
            dataPropertySelectorPanel.dispose();
        }
        if (individualSelectorPanel != null) {
            individualSelectorPanel.dispose();
        }
    }


    public List<OWLClassExpressionEditorPlugin> getDescriptionEditorPlugins() {
        if (descriptionEditorPlugins == null){
            OWLClassExpressionEditorPluginLoader loader = new OWLClassExpressionEditorPluginLoader(eKit);
            descriptionEditorPlugins = new ArrayList<OWLClassExpressionEditorPlugin>(loader.getPlugins());
            Comparator<OWLClassExpressionEditorPlugin> clsDescrPluginComparator = new Comparator<OWLClassExpressionEditorPlugin>(){
                public int compare(OWLClassExpressionEditorPlugin p1, OWLClassExpressionEditorPlugin p2) {
                    return p1.getIndex().compareTo(p2.getIndex());
                }
            };
            Collections.sort(descriptionEditorPlugins, clsDescrPluginComparator);
        }
        return descriptionEditorPlugins;
    }
}