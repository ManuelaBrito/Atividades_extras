module org.com.br.CRUD {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.com.br.CRUD to javafx.fxml;
    opens org.com.br.CRUD.model to javafx.base;
    exports org.com.br.CRUD;
    exports org.com.br.CRUD.controller;
    opens org.com.br.CRUD.controller to javafx.fxml;
}
