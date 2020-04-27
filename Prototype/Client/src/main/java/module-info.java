module project.Prototype {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.logging;
	requires javafx.base;
	requires javafx.graphics;

    opens project.Prototype to javafx.fxml;
    exports project.Prototype;
}