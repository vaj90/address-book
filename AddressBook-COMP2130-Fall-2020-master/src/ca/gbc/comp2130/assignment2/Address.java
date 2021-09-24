/*
Name: Allan John Valiente, Student ID: 101285226
Name: Farah Sheherin, Student ID: 101297029
*/

package ca.gbc.comp2130.assignment2;

public class Address {
    public String streetInfo1;
    public String streetInfo2;
    public String city;
    public String postalCode;
    public String province;
    public String country;

    public Address(String st1, String st2, String city, String postCode, String prov, String country) {
        this.streetInfo1 = st1;
        this.streetInfo2 = st2;
        this.city = city;
        this.postalCode = postCode;
        this.province = prov;
        this.country = country;
    }

    @Override
    public String toString() {
        return streetInfo1 +  " " +
                (streetInfo2 != "" ? streetInfo2 + " ": " ") +
                city + " " +
                province + " " +
                postalCode + " " +
                country;
    }
}

