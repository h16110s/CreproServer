package com.example.httpreq;

public class Maxim {
    int id;
    String maxim;
    String anime;
    String person;
    String emotion;
    Maxim (int id, String maxim, String anime,String person, String emotion){
        this.id = id;
        this.maxim = maxim;
        this.anime = anime;
        this.person = person;
        this.emotion = emotion;
    }

    public int getId() { return id; }

    public String getMaxim() {
        return maxim;
    }

    public String getAnime() {
        return anime;
    }

    public String getPerson() {
        return person;
    }

    public String getEmotion() {
        return emotion;
    }

    public String getSpeechText(){
        String s = "";
        s += this.anime + "。";
        s += this.person + "の名言です。";
        s += this.maxim ;
        return s;
    }
}
