
/** 
 * Copyright 2010 Daniel Guermeur and Amy Unruh
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   See http://connectrapp.appspot.com/ for a demo, and links to more information 
 *   about this app and the book that it accompanies.
 */
package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;





public class UserResearchSettingDTO implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 4066887588027126691L;
  private long id;
  
  private SocioResearchDTO researh;
  private Integer weights_use = 0;
  private Integer filters_use = 0;
  private ArrayList<String> filters = new ArrayList<String>();
  private ArrayList<Integer> filters_usage = new ArrayList<Integer>();
  private Integer weights_var_id = 0;
  public UserResearchSettingDTO() {
  
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

public Integer getWeights_use() {
	return weights_use;
}

public void setWeights_use(Integer weights_use) {
	this.weights_use = weights_use;
}

public Integer getFilters_use() {
	return filters_use;
}

public void setFilters_use(Integer filters_use) {
	this.filters_use = filters_use;
}

public ArrayList<String> getFilters() {
	return filters;
}

public void setFilters(ArrayList<String> filters) {
	this.filters = filters;
}


public ArrayList<Integer> getFilters_usage() {
	return filters_usage;
}



public void setFilters_usage(ArrayList<Integer> filters_usage) {
	this.filters_usage = filters_usage;
}

public ArrayList<String> getFiltersToProcess() {
    ArrayList<String> toProcess = new ArrayList<String>();
    int i = 0;
    for(Integer filter_use:filters_usage)
    {
            if (filter_use.equals(new Integer((Integer)1))) 
            {
                    toProcess.add(filters.get(i));
            }
            i++;
    }
    return toProcess;
}
public ArrayList<Long> getFiltersToProcessIndecies() {
    ArrayList<Long> toProcess = new ArrayList<Long>();
    int i = 0;
    for(Integer filter_use:filters_usage)
    {
            if (filter_use.equals(new Integer((Integer)1))) 
            {
                    toProcess.add(new Long(i));
            }
            i++;
    }
    return toProcess;
}



  

    /**
     * @return the researh
     */
    public SocioResearchDTO getResearh() {
        return researh;
    }

    /**
     * @param researh the researh to set
     */
    public void setResearh(SocioResearchDTO researh) {
        this.researh = researh;
    }

    /**
     * @return the weights_var_id
     */
    public Integer getWeights_var_id() {
        return weights_var_id;
    }

    /**
     * @param weights_var_id the weights_var_id to set
     */
    public void setWeights_var_id(Integer weights_var_id) {
        this.weights_var_id = weights_var_id;
    }
}
