package com.domain.ports.outgoing.productinformation;

public class ProductInformation {
    private int productId;
    private String code;
    private String name;
    private String rating;
    private int polishCapitalPercentage;
    private Boolean hiresInPoland;
    private Boolean rndInPoland;
    private Boolean registeredInPoland;
    private Boolean partOfGlobalCorporation;
    private String description;

    public ProductInformation(int productId, String code, String name, String rating, int polishCapitalPercentage, Boolean hiresInPoland, Boolean rndInPoland, Boolean registeredInPoland, Boolean partOfGlobalCorporation, String description) {
        this.productId = productId;
        this.code = code;
        this.name = name;
        this.rating = rating;
        this.polishCapitalPercentage = polishCapitalPercentage;
        this.hiresInPoland = hiresInPoland;
        this.rndInPoland = rndInPoland;
        this.registeredInPoland = registeredInPoland;
        this.partOfGlobalCorporation = partOfGlobalCorporation;
        this.description = description;
    }

    public ProductInformation() {
    }

    public int getProductId() {
        return productId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public int getPolishCapitalPercentage() {
        return polishCapitalPercentage;
    }

    public Boolean getHiresInPoland() {
        return hiresInPoland;
    }

    public Boolean getRndInPoland() {
        return rndInPoland;
    }

    public Boolean getRegisteredInPoland() {
        return registeredInPoland;
    }

    public Boolean getPartOfGlobalCorporation() {
        return partOfGlobalCorporation;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ProductInformation{" +
                "productId=" + productId +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", rating='" + rating + '\'' +
                ", polishCapitalPercentage=" + polishCapitalPercentage +
                ", hiresInPoland=" + hiresInPoland +
                ", rndInPoland=" + rndInPoland +
                ", registeredInPoland=" + registeredInPoland +
                ", partOfGlobalCorporation=" + partOfGlobalCorporation +
                ", description='" + description + '\'' +
                '}';
    }
}
