package com.mresearch.databank.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("serial")
public class ComparativeSearchParamsDTO implements Serializable{
    private double barrier_variance;
    public ComparativeSearchParamsDTO()
    {
    }

    /**
     * @return the barrier_variance
     */
    public double getBarrier_variance() {
        return barrier_variance;
    }

    /**
     * @param barrier_variance the barrier_variance to set
     */
    public void setBarrier_variance(double barrier_variance) {
        this.barrier_variance = barrier_variance;
    }
}
