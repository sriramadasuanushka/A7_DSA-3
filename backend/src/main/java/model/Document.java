package model;

public class Document {

    private String news;
    private String actualCategory;
    private String predictedCategory;
    private boolean userAdded;

    public Document(String news, String actualCategory) {

        this.news = news;
        this.actualCategory = actualCategory;
        this.predictedCategory = "";
        this.userAdded = false;
    }

    public String getNews() {
        return news;
    }

    public String getActualCategory() {
        return actualCategory;
    }

    public String getPredictedCategory() {
        return predictedCategory;
    }

    public boolean isUserAdded() {
        return userAdded;
    }

    public void setPredictedCategory(
            String predictedCategory) {

        this.predictedCategory = predictedCategory;
    }

    public void setUserAdded(boolean userAdded) {

        this.userAdded = userAdded;
    }
}