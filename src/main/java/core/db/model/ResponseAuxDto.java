package core.db.model;

import java.io.Serializable;

public class ResponseAuxDto implements Serializable {
    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    private String encrypt;

    public ResponseAuxDto(String search, String encrypt) {
        this.search = search;
        this.encrypt = encrypt;
    }

    public String toString(){

        return "searched: "+ search + ", encrypted: " + encrypt;

    }
}