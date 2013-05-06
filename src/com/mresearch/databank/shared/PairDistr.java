/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mresearch.databank.shared;

import java.util.ArrayList;

/**
 *
 * @author reshet
 */
public class PairDistr {
    public ArrayList<Double> distr,valid_distr;
    public PairDistr(ArrayList<Double> distr,ArrayList<Double> valid_distr)
    {
        this.distr = distr;
        this.valid_distr = valid_distr;
    }
}
