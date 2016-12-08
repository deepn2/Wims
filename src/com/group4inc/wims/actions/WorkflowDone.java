package com.group4inc.wims.actions;
import com.group4inc.wims.workflow.model.WorkflowInstance;
import com.group4inc.wims.workflow.model.WorkflowTemplate;

/**
* Actions to perform when a workflow has completed.
* 
* <P>This class defines actions that can be performed once a workflow has completed.
* 
* <P>A workflow can either be archived for historical purposes or be cleared (deleted from memory).
* 
* @see Workflow
* @author Elliot Linder (eml160)
*/
public class WorkflowDone {
	
	/**
	 * Clears the data from the workflow from the system.
	 *
	 * @return  If the operation completed successfully. TRUE if completed successfully and FALSE is there were errors.
	 */
	public static boolean clearWorkflowData(String username, WorkflowInstance workflow) {
		WorkflowTemplate.removeWorkflowInstance(username, workflow);
		workflow = null;
		return true;
	}
	
	/**
	 * Archives the workflow.
	 *
	 * @return  If the operation completed successfully. TRUE if completed successfully and FALSE is there were errors.
	 */
	public static boolean archiveWorkflow(Workflow workflow) {
		return true;
	}

}
