package ru.betry;

import java.util.ArrayList;
import java.util.List;

public class User {
   private String login;
   private String password;
    private List<Post> postList;

    User(){
        postList = new ArrayList<>();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addPost(Post post){
        this.postList.add(post);
    }
    User(String login, String password){
        postList = new ArrayList<>();
        this.login = login;
        this.password = password;
    }
}
