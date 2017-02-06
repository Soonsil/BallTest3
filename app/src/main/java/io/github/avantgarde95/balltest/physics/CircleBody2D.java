package io.github.avantgarde95.balltest.physics;

/**
 * Created by avantgarde on 2017-01-20.
 */

public class CircleBody2D extends Body2D {
    private double radius;

    public CircleBody2D(double mass, Vector2D position, Vector2D velocity, double dt, double radius) {
        super(mass, position, velocity, dt);
        this.radius = radius;
    }

    private boolean checkSegmentIntersection(Vector2D startPos, Vector2D endPos) {
        Vector2D d = Vector2D.sub(endPos, startPos);
        Vector2D f = Vector2D.sub(startPos, getPosition());
        double r = radius;

        double a = Vector2D.inner(d, d);
        double b = 2.0 * Vector2D.inner(f, d);
        double c = Vector2D.inner(f, f) - r * r;

        double det = b * b - 4.0 * a * c;

        if (det < 0) {
            return false;
        } else {
            det = Math.sqrt(det);
            double t1 = (-b - det) / (2.0 * a);
            double t2 = (-b + det) / (2.0 * a);

            if (t1 >= 0 && t1 <= 1) {
                return true;
            }

            if (t2 >= 0 && t2 <= 1) {
                return true;
            }

            return false;
        }
    }

    public void collidePolygon(PolygonBody2D polygon, double preserve) {
        Vector2D[] vertices = polygon.getVertices();
        int vertexCount = vertices.length;

        for (int i = 0; i < vertexCount - 1; i++) {
            if (checkSegmentIntersection(vertices[i], vertices[i + 1])) {
                Vector2D wall = Vector2D.sub(vertices[i + 1], vertices[i]);
                Vector2D[] proj = getVelocity().projection(wall);
                setVelocity(Vector2D.sub(proj[0], Vector2D.scale(proj[1], preserve)));
            }
        }
    }
}
