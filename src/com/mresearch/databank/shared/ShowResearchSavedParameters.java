package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowResearchSavedParameters implements Serializable, IShowPlaceParameters{
	public UserResearchSettingDTO getDto() {
		return dto;
	}

	public void setDto(UserResearchSettingDTO dto) {
		this.dto = dto;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4221470333954907316L;
	/**
	 * 
	 */
	private UserResearchSettingDTO dto;
	public ShowResearchSavedParameters(UserResearchSettingDTO dto)
	{
		this.dto = dto;
	}
	
	@Override
	public String toHash() {
		int hash = 1;
		hash = 17*hash + dto.hashCode();
		return String.valueOf(hash);
	}
	
	}