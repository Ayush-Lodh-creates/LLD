package amazon_locker.entities;

public class AccessCode {

    private Integer code;
    private Long createdOn;
    private Long expiresOn;

    public AccessCode(Integer code, Long createdOn, Long expiresOn) {
        this.code = code;
        this.createdOn = createdOn;
        this.expiresOn = expiresOn;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Long expiresOn) {
        this.expiresOn = expiresOn;
    }
}
