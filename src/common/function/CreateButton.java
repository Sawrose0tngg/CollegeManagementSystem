package common.function;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.sun.prism.impl.Disposer;
import database.handler.DatabaseHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import manage.student.HomeController;
import modules.CourseModel;

public class CreateButton extends TableCell<Disposer.Record, Boolean> {

    DatabaseHandler handler;
    JFXButton btn;

    public CreateButton(String name) {

        btn = new JFXButton(name);
        if ("Edit".equals(name)) {
            btn.setStyle("-fx-background-color:  rgb(0, 170, 207); -fx-text-fill: #fff");
        } else if ("Delete".equals(name)) {
            btn.setStyle("-fx-background-color: #F87951; -fx-text-fill: #fff");
        }
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                CourseModel model = (CourseModel) CreateButton.this.getTableView().getItems().get(CreateButton.this.getIndex());

                if (name.equals("Edit")) {
                    String query = "select * from course where courseList = '" + model.getCourse() + "'";
                    new HomeController().popupDialog();
                    handler = DatabaseHandler.getInstance();
                    ResultSet rs = handler.execQuery(query);
                    try {
                        if (rs.next()) {
                            HomeController.courseName.setText(rs.getString("courseList"));
                            HomeController.courseYear.setText(String.valueOf(rs.getInt("courseDuration")));
                            if (rs.getInt("flag") == 1) {
                                HomeController.checkBox.setSelected(true);
                            } else {
                                HomeController.checkBox.setSelected(false);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CreateButton.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (name.equals("Delete")) {
                    handler = DatabaseHandler.getInstance();
                    int counter = 0;
                    String sql = "select * from course where courseList = '" + model.getCourse() + "'";
                    ResultSet rs = handler.execQuery(sql);
                    String confirm = AlertMaker.confirmMessage("Confirmation", "Are you sure want to delete");
                    if (confirm.equals("YES")) {
                        try {
                            if (rs.next()) {
                                int val1 = rs.getInt("courseDuration");
                                int val2 = rs.getInt("flag");
                                String sql1 = "select * from course where courseDuration='" + val1 + "' and flag = '" + val2 + "'";
                                ResultSet rs1 = handler.execQuery(sql1);
                                while (rs1.next()) {
                                    counter++;
                                }
                                if (counter == 1) {
                                    if (val2 == 1) {
                                        handler.execAction("drop table " + val1 + "semester");
                                    } else if (val2 == 0) {
                                        handler.execAction("drop table " + val1 + "year");
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(CreateButton.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String query = "delete from course where courseList = '" + model.getCourse() + "'";
                        handler = DatabaseHandler.getInstance();
                        if (handler.execAction(query)) {
                            new HomeController().fillTable();
                            AlertMaker.showSimpleAlert("Success", "Successfully Deleted");
                        }
                    }
                }
            }

        });

    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if (!empty) {
            setGraphic(btn);
        } else {
            setGraphic(null);
        }
    }

}
