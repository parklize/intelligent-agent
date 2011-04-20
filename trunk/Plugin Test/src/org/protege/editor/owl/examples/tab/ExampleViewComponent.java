package org.protege.editor.owl.examples.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.OWLModelManagerImpl;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.protege.editor.owl.ui.renderer.OWLModelManagerEntityRenderer;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLClass;



public class ExampleViewComponent extends AbstractOWLViewComponent {
	// default codes
    private static final long serialVersionUID = -4515710047558710080L;
    private static final Logger log = Logger.getLogger(ExampleViewComponent.class);
    
    // components for our view
    private JPanel containerPanel = null; // 
    private JPanel menuPanel = null; // menu panel located in top
    private JPanel constraintsPanel = null; // constraintsPanel contains constraints scroll pane
    private JScrollPane constraintsScrollPane = null; // constriantsScrollPane contains constraints table
    private JTable constraintsTable = null; //constraints table

    // convinience class for querying the asserted subsumption hierarchy directly
//    private OWLObjectHierarchyProvider<OWLClass> assertedHierarchyProvider;
    
    // provides string renderings of Classes / Properties / Individuals. reflecting the current output setting
//    private OWLModelManagerEntityRenderer ren;

    @Override
    protected void disposeOWLView() {
    }

    @Override
    protected void initialiseOWLView() throws Exception {
    	
        setLayout(new BorderLayout());
        // generated code
        containerPanel = getContainerPanel();
        add(new JScrollPane(getConstraintsPanel()),BorderLayout.CENTER);
        
        // default codes
        log.info("Example View Component initialized");
    }
    
    // generated codes
    protected OWLClass updateView(OWLClass selectedClass){
    	log.info("updateView is called");
//    	namesComponent.setText("updateView");
    	if(selectedClass != null){
    		System.out.println(selectedClass.toString());
    	}
    	return selectedClass;
    }
    
    // render the class and recursively all of its subclasses
//    private void render(OWLClass selectedClass, int indent){
//    	for(int i=0; i<indent; i++){
//    		namesComponent.append("\t");
//    	}
//    	namesComponent.append(ren.render(selectedClass));
//    	namesComponent.append("\n");
//    	// the hierarchy provider gets subclasses for us
//    	for(OWLClass sub: assertedHierarchyProvider.getChildren(selectedClass)){
//    		render(sub,indent+1);
//    	}
//    }
    
    // initialize the container panel
    private JPanel getContainerPanel(){
    	if(containerPanel == null){
    	   containerPanel = new JPanel();
//    	   containerPanel.add(getMenuPanel());
    	   containerPanel.add(getConstraintsPanel());
    	}
    	return containerPanel;
    }
    // initialize the menu panel
    private JPanel getMenuPanel(){
    	if(menuPanel == null){
    	   menuPanel = new JPanel();
    	}
    	return menuPanel;
    }
    // initialize the constraints panel
    private JPanel getConstraintsPanel(){
    	if(constraintsPanel == null){
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			constraintsPanel = new JPanel();
			constraintsPanel.setLayout(new GridBagLayout());
			constraintsPanel.add(getConstraintsScrollPane(), gridBagConstraints);
    	}
    	return constraintsPanel;
    }
    // initialize the constraints scroll pane
    private JScrollPane getConstraintsScrollPane(){
    	if(constraintsScrollPane == null){
    	   constraintsScrollPane = new JScrollPane();
    	   constraintsScrollPane.setViewportView(getConstraintsTable());
    	}
    	return constraintsScrollPane;
    }
    // initialize the constraints table
	private JTable getConstraintsTable() {
		if (constraintsTable == null) {
			// Initialize column headings
			final String[] colHeads = {"Constraint Name", "Constraint"};
			// Initialize data
			final Object[][] data = {{"row1-col1", "row1-col2"},{"row2-col1", "row2-col2"}};
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			constraintsTable = new JTable(model);
		}
		return constraintsTable;
	}


}