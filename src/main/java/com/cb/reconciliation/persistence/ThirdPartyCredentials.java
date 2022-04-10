package com.cb.reconciliation.persistence;

import com.cb.reconciliation.utils.ErrorJSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "thirdparty")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyCredentials {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private String Id;
    private String type;
    @Column(columnDefinition = "TEXT")
    @Convert(converter= JSONObjectConverter.class)
    private JSONObject credentials_JSON;
    private Timestamp createdAt;
    @ManyToOne
    @JoinColumn(name = "CREDS", referencedColumnName = "CBID")
    private Credentials cbCredentials;


    public static org.json.simple.JSONObject errorJSON(String id) throws JSONException {
        ErrorJSON errorJSON = new ErrorJSON("INVALID_ID", " id " + id + " doesnt exist");
        return errorJSON.toJSON();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getCredentials_JSON() {
        return credentials_JSON;
    }

    public void setCredentials_JSON(JSONObject credentials_JSON) {
        this.credentials_JSON = credentials_JSON;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Credentials getCbCredentials() {
        return cbCredentials;
    }

    public void setCbCredentials(Credentials cbCredentials) {
        this.cbCredentials = cbCredentials;
    }
}
