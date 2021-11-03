class User {
    private String firstName;
    private String lastName;

    public User() {
        this.firstName = "";
        this.lastName = "";
    }

    public void setFirstName(String firstName) {
        if (firstName != null && !"".equals(firstName)) {
            this.firstName = firstName;
        }
    }

    public void setLastName(String lastName) {
        if (lastName != null && !"".equals(lastName)) {
            this.lastName = lastName;
        }
    }

    public String getFullName() {
        if ("".equals(this.firstName) && "".equals(this.lastName)) {
            return "Unknown";
        } else if ("".equals(this.lastName)) {
            return this.firstName;
        } else if ("".equals(this.firstName)) {
            return this.lastName;
        } else {
            return this.firstName + " " + this.lastName;
        }
    }
}