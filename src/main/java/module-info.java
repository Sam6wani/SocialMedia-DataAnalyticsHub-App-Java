module com.sarmad.dataanalyticshub {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.sarmad.dataanalyticshub to javafx.fxml;
    exports com.sarmad.dataanalyticshub;
    opens com.sarmad.dataanalyticshub.registration to javafx.fxml;
    exports com.sarmad.dataanalyticshub.registration;
    opens com.sarmad.dataanalyticshub.login to javafx.fxml;
    exports com.sarmad.dataanalyticshub.login;
    opens com.sarmad.dataanalyticshub.dashboard to javafx.fxml;
    exports com.sarmad.dataanalyticshub.dashboard;
    opens com.sarmad.dataanalyticshub.models to javafx.fxml;
    exports com.sarmad.dataanalyticshub.models;
    opens com.sarmad.dataanalyticshub.pieChart to javafx.fxml;
    exports com.sarmad.dataanalyticshub.pieChart;
}