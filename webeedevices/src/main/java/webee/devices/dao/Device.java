package webee.devices.dao;

public class Device {


    private String timestamp;
    private String mac;
    private Long id;
    public static final String MAC = "mac";
    public static final String TIMESTAMP = "timestump";
    public static final String ID = "id";

    private Device(Builder builder) {
        this.mac = builder.mac;
        this.timestamp = builder.timestamp;
        this.id = builder.id;

    }
    public static class Builder {
        private String mac;
        private String timestamp;
        private Long id;

        public Builder mac(String mac) {
            this.mac = mac;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }


        public Builder id(Long id) {
            this.id = id;
            return this;
        }


        public Device build() {
            return new Device(this);
        }
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String toString() {
        return "Mac: " + mac + ", Timestamp: " + timestamp;
    }


}
