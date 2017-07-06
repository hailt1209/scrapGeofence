package com.dbmethods.scrap.web.rest.vm;

import java.io.Serializable;

public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    private double distance;

    private StoreDetail store;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    

    public StoreDetail getStore() {
        return store;
    }

    public void setStore(StoreDetail store) {
        this.store = store;
    }



    public class StoreDetail implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String group;
        private String subGroup;
        private String companyName;
        private String brand;
        private String country;
        private String countryCode;
        private String faxNumber;
        private String postalCode;
        private double longitude;
        private String locationName;
        private String locationCode;
        private double latitude;
        private String phoneNumber;
        private String address1;
        private String address2;
        private String stateCode;
        private String city;
        
        

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getSubGroup() {
            return subGroup;
        }

        public void setSubGroup(String subGroup) {
            this.subGroup = subGroup;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
        
        

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getFaxNumber() {
            return faxNumber;
        }

        public void setFaxNumber(String faxNumber) {
            this.faxNumber = faxNumber;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getLocationCode() {
            return locationCode;
        }

        public void setLocationCode(String locationCode) {
            this.locationCode = locationCode;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

    }
}
