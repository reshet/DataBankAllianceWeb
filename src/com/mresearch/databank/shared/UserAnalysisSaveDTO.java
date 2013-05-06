
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







public class UserAnalysisSaveDTO implements Serializable, ICatalogizable {

  /**
	 * 
	 */
	public static enum User2DD_Choice {FREQ,PERC_ROW,PERC_COL,PERC_ALL}; 
	private User2DD_Choice user2dd_choice = User2DD_Choice.FREQ;
	
  public static String DISTR_TYPE_1D = "1D";
  public static String DISTR_TYPE_2D = "2D";   
	private static final long serialVersionUID = 4066887588027126691L;
  private long id;
  
  private String name,distr_type;
  private UserResearchSettingDTO seting;
  private VarDTO_Detailed var_1;
  private VarDTO_Detailed var_2;
  private ArrayList<Double> distribution;
  private ArrayList<Double> valid_distribution;
  
  
  public ArrayList<Double> getValid_distribution() {
	return valid_distribution;
}

public void setValid_distribution(ArrayList<Double> valid_distribution) {
	this.valid_distribution = valid_distribution;
}

public UserAnalysisSaveDTO() {
  
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

	
//	for(String filter:getFilters(research_id))
//	{
//		if (filters_usage.contains(getFilters(research_id).indexOf(filter)))
//		{
//			toProcess.add(filter);
//		}
//	}

    /**
     * @return the seting
     */
    public UserResearchSettingDTO getSeting() {
        return seting;
    }

    /**
     * @param seting the seting to set
     */
    public void setSeting(UserResearchSettingDTO seting) {
        this.seting = seting;
    }

    /**
     * @return the var_1
     */
    public VarDTO_Detailed getVar_1() {
        return var_1;
    }

    /**
     * @param var_1 the var_1 to set
     */
    public void setVar_1(VarDTO_Detailed var_1) {
        this.var_1 = var_1;
    }

    /**
     * @return the var_2
     */
    public VarDTO_Detailed getVar_2() {
        return var_2;
    }

    /**
     * @param var_2 the var_2 to set
     */
    public void setVar_2(VarDTO_Detailed var_2) {
        this.var_2 = var_2;
    }

    /**
     * @return the distribution
     */
    public ArrayList<Double> getDistribution() {
        return distribution;
    }

    /**
     * @param distribution the distribution to set
     */
    public void setDistribution(ArrayList<Double> distribution) {
        this.distribution = distribution;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the distr_type
     */
    public String getDistr_type() {
        return distr_type;
    }

    /**
     * @param distr_type the distr_type to set
     */
    public void setDistr_type(String distr_type) {
        this.distr_type = distr_type;
    }

	@Override
	public String getTextRepresent() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return id;
	}

	public User2DD_Choice getUser2dd_choice() {
		return user2dd_choice;
	}

	public void setUser2dd_choice(User2DD_Choice user2dd_choice) {
		this.user2dd_choice = user2dd_choice;
	}
	

}
