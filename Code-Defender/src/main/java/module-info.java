module org.example.codedefender {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.codedefender to javafx.fxml;
    exports Controller;
    exports Module;
    exports View;
}