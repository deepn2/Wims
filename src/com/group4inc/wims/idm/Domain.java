package com.group4inc.wims.idm;

import java.util.ArrayList;
import java.util.List;

import com.group4inc.wims.workflow.WorkflowTemplate;

/**
* Domain Object.
* 
* <P>This class defines Domains in the context of WIMS.
* 
* <P>A user is part of a Domain, which is the shared "instance" of WIMS for all users who are a part of the user's organization
* 
* @see User
* @see Group
* @author Elliot Linder (eml160)
*/
public class Domain {
	
	/**the name of the Domain (e.g. all_overriders)*/
	private String name;
	private List<WorkflowTemplate> workflowTemplates;
	
	
	/**
	 * Constructor for Domain objects.
	 *
	 * @param  name the name of the Domain to be constructed
	 */
	public Domain(String name) {
		name = this.name;
		IdMSerDB.addDomainToDomainDB(this);
	}
	
	/**
	 * Returns the Domain's name.
	 *
	 * @return      the name of the Domain
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns an ArrayList of **individual** Users that are members of the Domain. 
	 *
	 * @return      the ArrayList of Users in the Domain.
	 */
	public ArrayList<User> getUsersInDomain() {
		ArrayList<User> out = new ArrayList<User>();
		//
		return out;
	}

}
