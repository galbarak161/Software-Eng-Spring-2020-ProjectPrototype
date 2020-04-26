module project.Prototype {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.logging;
	requires javafx.base;

    opens project.Prototype to javafx.fxml;
    exports project.Prototype;
}