package notification_system.entities;

public class Client {

    private String clientId;
    private String clientName;
    private Long clientPhone;
    private String clientEmail;

    public Client(String clientId, String clientName, Long clientPhone, String clientEmail) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientEmail = clientEmail;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(Long clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
