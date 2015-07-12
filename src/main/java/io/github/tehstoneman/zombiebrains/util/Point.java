package io.github.tehstoneman.zombiebrains.util;

import java.io.Serializable;

public class Point implements Serializable {
        public static final Point ZERO = new Point(0, 0);
        public float x, y;

        public Point() {
                x = 0;
                y = 0;
        }

        public Point(Point p) {
                x = p.x;
                y = p.y;
        }

        public Point(float ix, float iy) {
                x = ix;
                y = iy;
                // StringCounter.count(System.getProperty(key));
        }

        public Point(float angle) {
                x = (float)Math.cos(angle);
                y = (float)Math.sin(angle);
        }

        public Point(String pointString) {
                String[] splitted = pointString.split(",");
                x = Float.valueOf(splitted[0]);
                y = Float.valueOf(splitted[1]);
        }

        public Point add(Point p) {
                return new Point(x + p.x, y + p.y);
        }

        public void sAdd(Point p) {
                x += p.x;
                y += p.y;
        }

        public Point sAdd2(Point p) {
                x += p.x;
                y += p.y;
                return this;
        }

        public Point sub(Point p) {
                return new Point(x - p.x, y - p.y);
        }

        public void sSub(Point p) {
                x -= p.x;
                y -= p.y;
        }

        public Point sSub2(Point p) {
                x -= p.x;
                y -= p.y;
                return this;
        }

        public float mul(Point p) {
                return (x * p.x + y * p.y);
        }

        public Point mul(float s) {
                return new Point(x * s, y * s);
        }

        public void sMul(float s) {
                x *= s;
                y *= s;
        }

        public Point sMul2(float s) {
                x *= s;
                y *= s;
                return this;
        }

        public float cross(Point p) {
                return x * p.y - y * p.x;
        }

        public float cross2(Point p) {
                return y * p.x - x * p.y;
        }

        public Point div(float s) {
                return new Point(x / s, y / s);
        }

        public Point normal() {
                return new Point(-y, x);
        }

        public Point normalize() {
                float h = (float)( 1 / Math.sqrt(x * x + y * y) );
                if (Float.isNaN(h) || Float.isInfinite(h)) {
                        return new Point();
                }
                return new Point(x * h, y * h);
        }

        public Point rotate(float cosa, float sina) {
                return new Point(x * cosa - y * sina, x * sina + y * cosa);
        }

        public Point rotate(float alpha) {
                return rotate((float)Math.cos(alpha), (float)Math.sin(alpha));
        }

        public Point sRotate2(float cosa, float sina) {
                float tmp = x;
                x = x * cosa - y * sina;
                y = tmp * sina + y * cosa;
                return this;
        }

        public void sRotate(float cosa, float sina) {
                float tmp = x;
                x = x * cosa - y * sina;
                y = tmp * sina + y * cosa;
        }

        public float square() {
                return x * x + y * y;
        }

        public float length() {
                return (float)Math.sqrt(this.square());
        }

        public float angle() {
                return (float)Math.atan2(y, x);
        }

        public float distance(Point p) {
                return (float)Math.sqrt((Math.pow(p.x - this.x,2) + Math
                                .pow(p.y - this.y,2)));
        }

        public float squaredDistance(Point p) {
                return (float)( Math.pow(x - p.x,2) + Math.pow(y - p.y,2) );
                // return p.sub(this).square();
        }

        public void from(Point p) {
                x = p.x;
                y = p.y;
        }

        public Point from2(Point p) {
                x = p.x;
                y = p.y;
                return this;
        }

        public void sCross(float a) {
                x = -y * a;
                y = x * a;
        }

        public Point sCross2(float a) {
                // Point r = new Point(-this.y, this.x);
                // return this.from2(new Point(r.x * a, r.y * a));
                /*
                 * x = -y * a; y = x * a; return this;
                 */
                return new Point(-y * a, x * a);
        }

        public void clear() {
                x = 0;
                y = 0;
        }

        public void sNormalize() {
                float h = (float)( 1.0f / Math.sqrt(x * x + y * y) );
                if (Float.isNaN(h) || Float.isInfinite(h)) {
                        x = 0;
                        y = 0;
                        return;
                }
                x *= h;
                y *= h;
        }

        /*
         * Returns the minimum distance from the Point to a line given through
         * starting point a and direction of the line b
         */
        public float distOnLineDir(Point a, Point b) {
                return (x - a.x) * b.x + (y - a.y) * b.y;
        }

        /*
         * Returns the minimum distance from the Point to a line given through
         * starting point a and second point b
         */
        public float distOnLine(Point a, Point b) {
                Point dir = b.sub(a).sNormalize2();
                return distOnLineDir(a, dir);
        }

        public Point sNormalize2() {
                float h = (float)( 1.0f / Math.sqrt(x * x + y * y) );
                if (Float.isNaN(h) || Float.isInfinite(h)) {
                        x = 0;
                        y = 0;
                        return this;
                }
                x *= h;
                y *= h;
                return this;
        }

        public void sNeg() {
                x = -x;
                y = -y;
        }

        public Point neg() {
                return new Point(-x, -y);
        }

        public void sNormal() {
                float tmp = x;
                x = -y;
                y = tmp;
        }

        public Point sNormal2() {
                float tmp = x;
                x = -y;
                y = tmp;
                return this;
        }

        public Point mulRotMatrix(float a, float b, float c, float d, float e,
                        float f) {
                /*
                 * (a b c) * (x) = (ax + cy + e) (d e f) * (y) = (bx + dy + f) (0 0 1) *
                 * (1) = ( 1 )
                 */
                return new Point(a * x + c * y + e, b * x + d * y + f);
        }

        public float distToLine(Point start, Point dir) {
                return distance(projectToLine(start, dir));
        }

        public Point projectToLine(Point start, Point dir) {
                float s = sub(start).mul(dir);
                return start.add(dir.mul(s));
        }

        public boolean isInInterval(float x1, float y1, float x2, float y2) {
                if (x1 > x || x2 < x)
                        return false;
                if (y1 > y || y2 < y)
                        return false;
                return true;
        }

        public String toString() {
                return x + " " + y;
        }

        public Point add(int i, int j) {
                return new Point(x + i, y + j);
        }

        public Point rem(int i, int j) {
                return new Point(x - i, y - j);
        }

        public String toFileString() {
                return x + "," + y;
        }

        public static Point valueOf(String value) {
                String[] splitted = value.split(",");
                return new Point(Float.valueOf(splitted[0]), Float.valueOf(splitted[1]));
        }

        public static Point mean(Point[] pointArray) {
                return sum(pointArray).mul(1.0f / pointArray.length);
        }

        private static Point sum(Point[] pointArray) {
                Point p = new Point();
                for (Point k : pointArray)
                        p.sAdd(k);
                return p;
        }
}