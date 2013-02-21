
package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowAnalysisSavedParameters implements Serializable, IShowPlaceParameters{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4221470333954907316L;
	/**
	 * 
	 */
	private UserAnalysisSaveDTO dto;
	public ShowAnalysisSavedParameters(UserAnalysisSaveDTO dto)
	{
		this.dto = dto;
	}
	
	public UserAnalysisSaveDTO getDto() {
		return dto;
	}

	public void setDto(UserAnalysisSaveDTO dto) {
		this.dto = dto;
	}

	@Override
	public String toHash() {
		int hash = 1;
		hash = 17*hash + dto.hashCode();
		return String.valueOf(hash);
	}
	
	}
