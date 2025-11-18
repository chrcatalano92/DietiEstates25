package com.demo.dietiestates25.data.model;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    AGENCY_MANAGER("ROLE_AGENCY_MANAGER"),
    AGENT("ROLE_AGENT");

    private final String apiName;

    Role(String apiName) {
        this.apiName = apiName;
    }

    public String getApiName() { return apiName; }

    /**
     * Controlla se questo Ruolo è considerato "professionale".
     * @return true se il ruolo è ADMIN, AGENCY_MANAGER o AGENT.
     */
    public boolean isProfessional() {
        return this != USER;
    }

    /**
     * Converte una stringa (esempio: ROLE_ADMIN) nell'enum corrispondente.
     */
    public static Role fromApiName(String apiName) {
        if (apiName == null) return null;
        for (Role role : values()) {
            if(role.getApiName().equalsIgnoreCase(apiName)) {
                return role;
            }
        }
        return null;
    }
}
