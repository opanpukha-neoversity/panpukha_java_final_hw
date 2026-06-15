package ua.university.sms.model.entity;

public enum Grade {
    A(4.0),
    B(3.0),
    C(2.0),
    D(1.0),
    F(0.0),
    NA(null);

    private final Double points;

    Grade(Double points) {
        this.points = points;
    }

    public boolean affectsGpa() {
        return points != null;
    }

    public double points() {
        return points == null ? 0.0 : points;
    }
}
