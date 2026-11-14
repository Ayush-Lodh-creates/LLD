package dating_app.entities;

import dating_app.enums.Gender;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private final String name;
    private final int age;
    private final Gender gender;
    private final String bio;
    private final List<String> photos = new ArrayList<>();
    private final List<Interest> interests = new ArrayList<>();
    private final Location location;

    public UserProfile(String name, int age, Gender gender, String bio, Location location) {
        if (age < 18) throw new IllegalArgumentException("User must be at least 18");
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.location = location;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public Gender getGender() { return gender; }
    public String getBio() { return bio; }
    public Location getLocation() { return location; }
    public List<String> getPhotos() { return List.copyOf(photos); }
    public List<Interest> getInterests() { return List.copyOf(interests); }
    public void addPhoto(String photo) { photos.add(photo); }
    public void addInterest(Interest interest) { interests.add(interest); }

    public void display() {
        System.out.println(name + ", " + age + ", " + gender + " - " + bio);
    }
}

