
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





public class UserHistoryDTO implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 4066887588027126691L;
  private long id;
  
  private UserResearchSettingDTO current_research = new UserResearchSettingDTO();
  private UserAnalysisSaveDTO currant_var = new UserAnalysisSaveDTO();
  
  
  public UserHistoryDTO() {
  
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }



//public long getCurrant_var() {
//	return currant_var;
//}
//
//public void setCurrant_var(long currant_var) {
//	this.currant_var = currant_var;
//}
//
//public long getCurrent_research() {
//	return current_research;
//}
//
//public void setCurrent_research(long current_research) {
//	this.current_research = current_research;
//}
	
//	for(String filter:getFilters(research_id))
//	{
//		if (filters_usage.contains(getFilters(research_id).indexOf(filter)))
//		{
//			toProcess.add(filter);
//		}
//	}

    /**
     * @return the current_research
     */
    public UserResearchSettingDTO getCurrent_research() {
        return current_research;
    }

    /**
     * @param current_research the current_research to set
     */
    public void setCurrent_research(UserResearchSettingDTO current_research) {
        this.current_research = current_research;
    }

    /**
     * @return the currant_var
     */
    public UserAnalysisSaveDTO getCurrant_var() {
        return currant_var;
    }

    /**
     * @param currant_var the currant_var to set
     */
    public void setCurrant_var(UserAnalysisSaveDTO currant_var) {
        this.currant_var = currant_var;
    }
	

}
