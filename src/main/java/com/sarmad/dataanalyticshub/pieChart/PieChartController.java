package com.sarmad.dataanalyticshub.pieChart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PieChartController implements Initializable {
    @FXML
    public PieChart pieChart;

    private Stage stage;

    private ObservableList<PieChart.Data> pieChartData;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setData(Integer slice1, Integer slice2, Integer slice3){
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("0-99", slice1),
                new PieChart.Data("100-999", slice2),
                new PieChart.Data("> 1000", slice3)
                );

        pieChart.setData(pieChartData);
    }
    public void closePieChartDialog(ActionEvent actionEvent) {
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
