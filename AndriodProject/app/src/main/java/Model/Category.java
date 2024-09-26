package Model;


import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("idCategory")
    String idCategory;

    @SerializedName("strCategory")
    String strCategory;

    @SerializedName("strCategoryThumb")
    String strCategoryThumb;

    @SerializedName("strCategoryDescription")
    String strCategoryDescription;


        public String getIdCategory() {
            return idCategory;
        }

        public void setIdCategory(String idCategory) {
            this.idCategory = idCategory;
        }

        public String getStrCategory() {
            return strCategory;
        }

        public void setStrCategory(String strCategory) {
            this.strCategory = strCategory;
        }

        public String getStrCategoryThumb() {
            return strCategoryThumb;
        }

        public void setStrCategoryThumb(String strCategoryThumb) {
            this.strCategoryThumb = strCategoryThumb;
        }

        public String getStrCategoryDescription() {
            return strCategoryDescription;
        }

        public void setStrCategoryDescription(String strCategoryDescription) {
            this.strCategoryDescription = strCategoryDescription;
        }
    }

