package io.github.avantgarde95.balltest.physics2d;

/**
 * Created by avantgarde on 2017-02-11.
 */

public class CirclePolygonCollision2D {
    private CircleBody2D circle;

    public CirclePolygonCollision2D(CircleBody2D circle) {
        this.circle = circle;
    }

    private boolean isTouchingLine(Vector2D lineStart, Vector2D lineEnd) {
        Vector2D d = lineEnd.sub(lineStart);
        Vector2D f = lineStart.sub(circle.getPosition());
        float r = circle.getRadius();

        float a = d.inner(d);
        float b = 2.0f * f.inner(d);
        float c = f.inner(f) - r * r;

        float det = b * b - 4.0f * a * c;

        if (det < 0) {
            return false;
        } else {
            det = (float) Math.sqrt(det);
            float t1 = (-b - det) / (2.0f * a);
            float t2 = (-b + det) / (2.0f * a);

            if (t1 >= 0 && t1 <= 1) {
                return true;
            }

            if (t2 >= 0 && t2 <= 1) {
                return true;
            }

            return false;
        }
    }

    private void rebound(Vector2D lineStart, Vector2D lineEnd, float preserve) {
        Vector2D d = lineEnd.sub(lineStart);
        Vector2D[] p = circle.getVelocity().proj(d);
        Vector2D v1 = p[0];
        Vector2D v2 = p[1];

        float r = circle.getRadius();

        float x0 = circle.getPosition().getX();
        float y0 = circle.getPosition().getY();

        float x1 = lineStart.getX();
        float y1 = lineStart.getY();

        float x2 = lineEnd.getX();
        float y2 = lineEnd.getY();

        float l = d.norm();
        float f = v2.norm();

        if (l != 0 && f != 0) {
            float q = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1) / l;
            circle.setPosition(circle.getPosition().sub(v2.scale((r - q) / f)));
        }

        circle.setVelocity(v1.sub(v2.scale(preserve)));
    }

    public boolean collidePolygon(PolygonBody2D other, float preserve) {
        float[][] vertices = other.getVertices();
        int vertexCount = vertices.length;
        float x = other.getPosition().getX();
        float y = other.getPosition().getY();

        for (int i = 0; i < vertexCount - 1; i++) {
            Vector2D lineStart = new Vector2D(vertices[i][0] + x, vertices[i][1] + y);
            Vector2D lineEnd = new Vector2D(vertices[i + 1][0] + x, vertices[i + 1][1] + y);

            if (isTouchingLine(lineStart, lineEnd)) {
                rebound(lineStart, lineEnd, preserve);
                return true;
            }
        }

        if (vertexCount > 1) {
            Vector2D lineStart = new Vector2D(vertices[vertexCount - 1][0] + x, vertices[vertexCount - 1][1] + y);
            Vector2D lineEnd = new Vector2D(vertices[0][0] + x, vertices[0][1] + y);

            if (isTouchingLine(lineStart, lineEnd)) {
                rebound(lineStart, lineEnd, preserve);
                return true;
            }
        }

        return false;
    }
}
