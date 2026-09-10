package com.sdover.hotelapi.dto;

import java.util.List;

public class CheckinRequest {

    private Integer numPax;
    private List<AcompananteRequest> acompanantes;

    public CheckinRequest () {};

    public CheckinRequest (Integer numPax,
        List<AcompananteRequest> acompanantes) {

            this.numPax = numPax;
            this.acompanantes = acompanantes;
        }
    
    public Integer getNumPax() {
        return numPax;
    }

    public void setNumPax(Integer numPax) {
        this.numPax = numPax;
    }

    public List<AcompananteRequest> getAcompanantes() {
        return acompanantes;
    }

    public void setAcompanantes(List<AcompananteRequest> acompanantes) {
        this.acompanantes = acompanantes;
    }

}
