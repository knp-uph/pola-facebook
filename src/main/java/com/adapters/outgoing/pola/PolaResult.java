package com.adapters.outgoing.pola;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolaResult {
    private int product_id;
    private String code;
    private String name;
    private String card_type;
    private int plScore;
    private String altText;
    private int plCapital;
    private int plWorkers;
    private int plRnD;
    private int plRegistered;
    private int plNotGlobEnt;
    private String description;
    private Map<String, String> sources; //from: https://stackoverflow.com/questions/31498867/gson-deserialize-json-with-nested-map
    private String report_text;
    private String report_button_text;
    private String report_button_type;

    public int getProductId() {
        return product_id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCardType() {
        return card_type;
    }

    public int getPlScore() {
        return plScore;
    }

    public String getAltText() {
        return altText;
    }

    public int getPlCapital() {
        return plCapital;
    }

    public int getPlWorkers() {
        return plWorkers;
    }

    public int getPlRnD() {
        return plRnD;
    }

    public int getPlRegistered() {
        return plRegistered;
    }

    public int getPlNotGlobEnt() {
        return plNotGlobEnt;
    }

    public boolean isPlWorkers() {
        return plWorkers == 100;
    }

    public boolean isPlRnD() {
        return plRnD == 100;
    }

    public boolean isPlRegistered() {
        return plRegistered == 100;
    }

    public boolean isPlNotGlobEnt() {
        return plNotGlobEnt == 100;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getSources() {
        return sources;
    }

    public String getReportText() {
        return report_text;
    }

    public String getReportButtonText() {
        return report_button_text;
    }

    public String getReportButtonType() {
        return report_button_type;
    }

    @Override
    public String toString() {
        return "PolaResult{" +
                "product_id=" + product_id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", card_type='" + card_type + '\'' +
                ", plScore=" + plScore +
                ", altText='" + altText + '\'' +
                ", plCapital=" + plCapital +
                ", plWorkers=" + plWorkers +
                ", plRnD=" + plRnD +
                ", plRegistered=" + plRegistered +
                ", plNotGlobEnt=" + plNotGlobEnt +
                ", description='" + description + '\'' +
                ", sources=" + sources +
                ", report_text='" + report_text + '\'' +
                ", report_button_text='" + report_button_text + '\'' +
                ", report_button_type='" + report_button_type + '\'' +
                '}';
    }
}
