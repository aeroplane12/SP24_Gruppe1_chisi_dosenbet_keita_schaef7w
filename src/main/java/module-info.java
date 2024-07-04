module sw.praktikum.spinfood {
    requires javafx.controls;
    requires javafx.fxml;


    opens sw.praktikum.spinfood to javafx.fxml;
    exports sw.praktikum.spinfood;
}