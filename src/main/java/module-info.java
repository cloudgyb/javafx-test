module com.github.cloudgyb.javafxtest {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires java.sql;

    opens com.github.cloudgyb.javafxtest to javafx.fxml;
    exports com.github.cloudgyb.javafxtest;
    exports com.github.cloudgyb.javafxtest.view;
    exports com.github.cloudgyb.javafxtest.domain;
    exports com.github.cloudgyb.javafxtest.viewmodel;
    opens com.github.cloudgyb.javafxtest.view to javafx.fxml;
}