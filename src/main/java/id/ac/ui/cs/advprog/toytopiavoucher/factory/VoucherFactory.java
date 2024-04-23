package id.ac.ui.cs.advprog.toytopiavoucher.factory;

import id.ac.ui.cs.advprog.toytopiavoucher.model.TermsConditions;

import java.util.Map;

public class TermsConditionsFactory {
    public TermsConditions create(Map<String, Object> request) {
        String code = (String)request.get("code");
        double discount = (Double)request.get("discount");
        Map<String, Object> termsConditionsData = (Map<String, Object>)request.get("termsConditions");
        for () {

        }
        return null;
    }
}
