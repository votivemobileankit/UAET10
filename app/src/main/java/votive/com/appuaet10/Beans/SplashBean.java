package votive.com.appuaet10.Beans;

public class SplashBean {

    private String name;
    private int imageSource;

    public SplashBean(int imageSource) {
        this.imageSource = imageSource;
    }

    public String getName() {
        return name;
    }

    public int getImageSource() {
        return imageSource;
    }
}
