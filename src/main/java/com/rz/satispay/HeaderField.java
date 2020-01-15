package com.rz.satispay;

import java.util.Objects;

public class HeaderField {

    private String header;
    private String value;

    private HeaderField() {

    }

    public HeaderField(String header, String value) {
        this.header = header.toLowerCase().trim();
        this.value = value.trim();
    }

    public String getHeader() {
        return header;
    }

    private void setHeader(String header) {
        this.header = header;
    }

    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", header, value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;

        if (other == this)
            return true;

        if (!(other instanceof HeaderField))
            return false;

        return this.header.equals(((HeaderField) other).header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }
}
