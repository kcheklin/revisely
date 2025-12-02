package com.example.myapp;

public class Review {

    private int avatarId;
    private String name;
    private String content;
    private int star;
    private int likes;

    public Review(int avatarId, String name, String content, int star, int likes) {
        this.avatarId = avatarId;
        this.name = name;
        this.content = content;
        this.star = star;
        this.likes = likes;
    }

    public int getAvatarId() { return avatarId; }
    public String getName() { return name; }
    public String getContent() { return content; }
    public int getStar() { return star; }
    public int getLikes() { return likes; }
}
