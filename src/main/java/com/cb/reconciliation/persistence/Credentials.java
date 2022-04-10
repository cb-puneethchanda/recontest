package com.cb.reconciliation.persistence;

import com.cb.reconciliation.utils.ErrorJSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.json.JSONException;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "chargebee")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {

    @Id
    @Column(name="CBID", nullable=false, unique=true)
    private String id;
    private String chargebeeSiteName;
    private String APIKey;
    @OneToMany(mappedBy = "cbCredentials", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThirdPartyCredentials> accountingSystem;
    @OneToMany(mappedBy = "cbCredentials", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ThirdPartyCredentials> paymentSystem;
    private Timestamp createdAt;



    public static org.json.simple.JSONObject errorJSON(String id) throws JSONException {
        ErrorJSON errorJSON = new ErrorJSON("INVALID_ID", " id " + id + " doesnt exist");
        return errorJSON.toJSON();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChargebeeSiteName() {
        return chargebeeSiteName;
    }

    public void setChargebeeSiteName(String chargebeeSiteName) {
        this.chargebeeSiteName = chargebeeSiteName;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public List<ThirdPartyCredentials> getAccountingSystem() {
        return accountingSystem;
    }

    public void setAccountingSystem(List<ThirdPartyCredentials> accountingSystem) {
        this.accountingSystem = accountingSystem;
    }

    public List<ThirdPartyCredentials> getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(List<ThirdPartyCredentials> paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

