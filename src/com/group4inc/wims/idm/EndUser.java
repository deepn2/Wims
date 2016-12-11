package com.group4inc.wims.idm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.group4inc.wims.workflow.model.WorkflowInstance;
import com.group4inc.wims.workflow.model.WorkflowTemplate;

/**
* EndUser Object.
* 
* This class defines EndUsers in the context of WIMS. This class extends the User class and inherits its functionality.
* EndUsers are the backbone of the user system. EndUsers log into the system and instantaite and participate in workflows. 
*  
* @author Joseph Kotzker (jgk73)
*/
public class EndUser extends User {

	public EndUser(String name, String email, String username, String password, String initdomain) {
		super(name, email, username, password, initdomain);
	}
	

	/**
	 * Get the list of all active workflows pertaining to this user
	 * 
	 * @return list of active workflows associated with the EndUser
	 */
	public List<WorkflowInstance> getActiveWorkflows() {
		
		String myName = this.getName();
		
		// Instantiate the list of workflow instances to be returned
		List<WorkflowInstance> activeWorkflows = null;
		
		// Get domains user is a member of
		Map<String, Domain> membership = this.getDomain();
		Iterator<Entry<String, Domain>> it = membership.entrySet().iterator();
		while (it.hasNext()) {
			
	        Map.Entry pair = (Map.Entry)it.next();
	        Domain dom = (Domain) pair.getValue();
	        
	        // get all templates associated with this domain
	        List<WorkflowTemplate> temps = dom.getWorkflowTemplates();
	        Iterator<WorkflowTemplate> templateIt = temps.iterator();
	        while (templateIt.hasNext()) {
	        	
	        	// for each template, see if user is associated with it
	        	WorkflowTemplate template = templateIt.next();
	        	List<String> users = template.getUsers();
	        	if(users.contains(myName)) {
	        		
	        		// if user is associated with the template, get all instances associated with both the template and the user
	        		List<WorkflowInstance> instances = template.getWorkflowInstance(myName);
	        		activeWorkflows.addAll(instances);
	        	}
	        	
	        }
	        
	    }
				
		return activeWorkflows;
	}

}
