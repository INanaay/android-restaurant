package ca.ulaval.ima.mp;

import android.location.Location;

public class Restaurant {
    private String _id;
    private String _name;
    private Location _location;
    private String _review_count;
    private String _review_average;
    private String _image;
    private String _kitchenId;
    private String _kitchen;

    public Restaurant(String id, String name, Location location, String review_count, String review_average,
                      String image, String kitchenId, String kitchen) {
        this._id = id;
        this._name = name;
        this._location = location;
        this._review_count = review_count;
        this._review_average = review_average;
        this._image = image;
        this._kitchenId = kitchenId;
        this._kitchen = kitchen;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public Location get_location() {
        return _location;
    }

    public void set_location(Location _location) {
        this._location = _location;
    }

    public String get_review_count() {
        return _review_count;
    }

    public void set_review_count(String _review_count) {
        this._review_count = _review_count;
    }

    public String get_review_average() {
        return _review_average;
    }

    public void set_review_average(String _review_average) {
        this._review_average = _review_average;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public String get_kitchenId() {
        return _kitchenId;
    }

    public String get_kitchen() {
        return _kitchen;
    }

    public String get_id() {
        return _id;
    }
}
