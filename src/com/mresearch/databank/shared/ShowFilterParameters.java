package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ShowFilterParameters implements Serializable, IShowPlaceParameters{
	public HashMap<String, String> getMapping() {
		return mapping;
	}

	public void setMapping(HashMap<String, String> mapping) {
		this.mapping = mapping;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4221470333954907316L;
	/**
	 * 
	 */
	private  ArrayList<FilterBaseDTO> filters;
	 private HashMap<String, String> mapping;
	public ArrayList<FilterBaseDTO> getFilters() {
		return filters;
	}

	public void setFilters(ArrayList<FilterBaseDTO> filters) {
		this.filters = filters;
	}
	private Set<String> idents;
	public ShowFilterParameters(ArrayList<FilterBaseDTO> filters,Set<String> idents,HashMap<String, String> mapping)
	{
		this.filters = filters;
		this.idents = idents;
		this.mapping = mapping;
	}
	
	public Set<String> getIdents() {
		return idents;
	}
	public void setIdents(Set<String> idents) {
		this.idents = idents;
	}
	@Override
	public String toHash() {
		int hash = 1;
		hash = 17*hash + filters.hashCode();
		hash = 31*hash + idents.hashCode();
		return String.valueOf(hash);
	}
	
	}
