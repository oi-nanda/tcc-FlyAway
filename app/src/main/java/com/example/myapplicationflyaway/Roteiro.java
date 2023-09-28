package com.example.myapplicationflyaway;

public class Roteiro {

    private String titulo, data, lugar;
    private Boolean salvo;
    private int imageId;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Boolean getSalvo() {
        return salvo;
    }

    public void setSalvo(Boolean salvo) {
        this.salvo = salvo;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Roteiro(String titulo, String data, String lugar, Boolean salvo, int imageId) {
        this.titulo = titulo;
        this.data = data;
        this.lugar = lugar;
        this.salvo = salvo;
        this.imageId = imageId;
    }
}
