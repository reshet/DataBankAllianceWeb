package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowCatalogParameters implements Serializable, IShowPlaceParameters{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4221470333954907316L;
	/**
	 * 
	 */
	private ArrayList<Long> ids;
	private ArrayList<String> idents;
	public ShowCatalogParameters(ArrayList<Long> ids,ArrayList<String> idents)
	{
		this.ids =ids;
		this.idents = idents;
	}
	public ArrayList<Long> getIds() {
		return ids;
	}
	public void setIds(ArrayList<Long> ids) {
		this.ids = ids;
	}
	public ArrayList<String> getIdents() {
		return idents;
	}
	public void setIdents(ArrayList<String> idents) {
		this.idents = idents;
	}
	@Override
	public String toHash() {
		int hash = 1;
		hash = 17*hash + ids.hashCode();
		hash = 31*hash + idents.hashCode();
		return String.valueOf(hash);
	}
	
	}
