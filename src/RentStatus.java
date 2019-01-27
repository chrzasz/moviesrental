public enum RentStatus {
    IN_RENT("In rent"),
    RETURNED("Returned");

    String status;

    RentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
