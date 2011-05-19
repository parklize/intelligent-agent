package org.protege.editor.owl.swcl.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.util.OWLDataTypeUtils;
import org.protege.editor.owl.swcl.model.Variable;
import org.protege.editor.owl.swcl.utils.CheckBoxRenderer;
import org.protege.editor.owl.swcl.utils.CheckButtonEditor;
import org.protege.editor.owl.swcl.utils.OWLClassHelper;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

/**
 * 
 * Author: parklize
 * Date: 2011.04.20~
 * Description: SWCL view component in Protege
 * 
 */

public class ExampleViewComponent extends AbstractOWLViewComponent implements ActionListener{
	
	// default codes
    private static final long serialVersionUID = -4515710047558710080L;
    private static final Logger log = Logger.getLogger(ExampleViewComponent.class);
    
    // components for our view
    private JPanel menuPanel = null; // menu panel located in top
    private JPanel buttonsPanel = null; // buttons panel located at east of menu panel
    private JPanel constraintsPanel = null; // constraintsPanel contains constraints scroll pane
    private JScrollPane constraintsScrollPane = null; // constriantsScrollPane contains constraints table
    private JTable constraintsTable = null; // constraints table
    private JButton addConstraintButton = null; // add constraint button
    private JButton generateSWCLButton = null; // button to generate SWCL code
    private OWLModelManager owlModelManager = null;
    private OWLOntology owl = null;
    private OWLClassHelper owlClassHelper = null;
    
    // global variables
	private ArrayList<Variable> variablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private Iterator variablesIterator = variablesList.iterator();

    // convinience class for querying the asserted subsumption hierarchy directly
//    private OWLObjectHierarchyProvider<OWLClass> assertedHierarchyProvider;
    
    // provides string renderings of Classes / Properties / Individuals. reflecting the current output setting
//    private OWLModelManagerEntityRenderer ren;

    @Override
    protected void disposeOWLView() {
    }

    @Override
    protected void initialiseOWLView() throws Exception {
    	
    	// set layout 
        setLayout(new BorderLayout());
        
        // add panels to the protege jPanel
        add(getMenuPanel(),BorderLayout.NORTH);
        add(getConstraintsPanel(),BorderLayout.CENTER);
        
// EDITING...
       // access to the ontologies, reasoners, search renderings, change management etc.
       owlModelManager = getOWLModelManager();
//       Set<OWLOntology> activeOntologies = owlModelManager.getActiveOntologies();
//       
//       // dump active ontology present
//       for(OWLOntology owl:activeOntologies){
//    	   OWLOntologyManager manager = owl.getOWLOntologyManager();
//    	   manager.saveOntology(owl,new SystemOutDocumentTarget());
//       }
       
       // get all properties in ontology
        owl = owlModelManager.getActiveOntology();
        OWLOntologyManager manager = owl.getOWLOntologyManager();
        OWLDataFactory odf = manager.getOWLDataFactory();
        Set d = owl.getDataPropertiesInSignature();
        Set c = owl.getClassesInSignature();
        Set<OWLIndividual> i = null;
        Iterator it = d.iterator();
        Iterator it2 = c.iterator();
        Iterator it3 = null;
        while(it.hasNext()){
        	System.out.println(it.next().toString());
        }
        while(it2.hasNext()){
//        	System.out.println(it2.next().toString());// get all classes
System.out.println("=============");
        	OWLClassImpl owlClass = (OWLClassImpl) it2.next();
        	this.owlClassHelper = new OWLClassHelper(owlClass);
        	i = owlClass.getIndividuals(owl);
            it3 = i.iterator();
            while(it3.hasNext()){
System.out.println("IRI:"+owlClass.getIRI());
System.out.println("Class Name:"+this.owlClassHelper.getClassName());
System.out.println(owlClass.toStringID()+" individuals");
System.out.println(it3.next());
            }
        }

        
        
        // default codes
        log.info("Example View Component initialized");
    }
    
    // initialize the menu panel
    private JPanel getMenuPanel(){
    	
    	if(menuPanel == null){
    		
    	   menuPanel = new JPanel();
    	   menuPanel.setLayout(new BorderLayout());
    	   menuPanel.setPreferredSize(new Dimension(0,20));
    	   menuPanel.add(getButtonsPanel(),BorderLayout.EAST);
    	   
    	}
    	
    	return menuPanel;
    	
    }
    
    // initialize the buttons panel
    private JPanel getButtonsPanel(){
    	
    	if(buttonsPanel == null){
    		
    		buttonsPanel = new JPanel();
    		buttonsPanel.setLayout(new GridLayout());
    		buttonsPanel.setPreferredSize(new Dimension(60,0));
    		buttonsPanel.add(getAddConstraintButton());
    		buttonsPanel.add(getGenerateSWCLButton());
    	}
    	
    	return buttonsPanel;
    	
    }
    
	// initialize the add constraint button
	private JButton getAddConstraintButton(){
		
		if(addConstraintButton == null){
			
			addConstraintButton = new JButton();
			addConstraintButton.setText("+");
			addConstraintButton.addActionListener(this);// add event listener
		}
		
		return addConstraintButton;
		
	}
	
	// initialize the generate SWCL code button
	private JButton getGenerateSWCLButton(){
		
		if(generateSWCLButton == null){
			
			generateSWCLButton = new JButton();
			generateSWCLButton.setText("D");
			generateSWCLButton.addActionListener(this);// add event listener
			
		}
		
		return generateSWCLButton;
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
			final String[] colHeads = {"Enabled","Constraint Name", "Constraint"};
			// Initialize data with null
			final Object[][] data = null;
			
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			constraintsTable = new JTable(model);
			
			TableColumn tableColumn = constraintsTable.getColumn("Enabled");
			tableColumn.setMaxWidth(50);
			tableColumn.setMinWidth(50);
			tableColumn.setCellEditor(new CheckButtonEditor(new JCheckBox()));
			tableColumn.setCellRenderer(new CheckBoxRenderer());// renderer for displaying the checkbox component in the cell
			
		}
		
		return constraintsTable;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		DefaultTableModel tableModel = (DefaultTableModel) constraintsTable.getModel();
		
		// the event of clicking the + button, add one row to constraints table
		if(e.getActionCommand().equals("+")){
			// set alignment of jcheckbox to center
			JCheckBox jb = new JCheckBox();
			jb.setHorizontalAlignment(SwingConstants.CENTER);
			tableModel.addRow(new Object[]{jb,"",""});
			
			// create add constraint component
			AddConstraintsComponent acc = new AddConstraintsComponent(this.owl,this.variablesList);
			acc.setVisible(true);
		}
		
		// the event of clicking the G button, generate the SWCL code
		if(e.getActionCommand().endsWith("D")){
			int rowCount = tableModel.getRowCount();// =no. of constraints 
			for(int i=0;i<rowCount;i++){
				
				JCheckBox jcb = (JCheckBox) tableModel.getValueAt(i, 0);
				System.out.println(jcb.isSelected());
				System.out.println(tableModel.getValueAt(i, 1));
				System.out.println(tableModel.getValueAt(i, 2));
				
			}
		}
		
	}

}