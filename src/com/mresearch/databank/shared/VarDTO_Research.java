package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("serial")
public class VarDTO_Research implements Serializable{
	private long id;
	private long res_id;
	private String res_name;
	public VarDTO_Research()
	{}
	
	public VarDTO_Research(long id,long res_id,String res_name)
	{
		this();
                this.id = id;
		this.res_id = res_id;
                this.res_name = res_name;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

    /**
     * @return the res_id
     */
    public long getRes_id() {
        return res_id;
    }

    /**
     * @param res_id the res_id to set
     */
    public void setRes_id(long res_id) {
        this.res_id = res_id;
    }

    /**
     * @return the res_name
     */
    public String getRes_name() {
        return res_name;
    }

    /**
     * @param res_name the res_name to set
     */
    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

	

		
}
