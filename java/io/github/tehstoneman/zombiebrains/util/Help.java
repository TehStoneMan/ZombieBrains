package io.github.tehstoneman.zombiebrains.util;


public class Help {
        public static void p(StackTraceElement[] x) {
                Help.p(x[1]);
                // for (StackTraceElement y : x){
                // Help.p(y);
                // }
                Help.p("---");
        }

        public static void p(Point x) {
                if (x != null) {
                        System.out.println(x.x + " " + x.y);
                } else {
                        System.out.println("null");
                }
        }

        public static void p(String txt, Point x) {
                System.out.print(txt + " ");
                System.out.println(x.x + " " + x.y);
        }

        public static void p(int x) {
                System.out.println(x);
        }

        public static void p(boolean x) {
                System.out.println(x);
        }

        public static void p(float x) {
                System.out.println(x);
        }

        public static void p(String x) {
                System.out.println(x);
        }

        public static void p(String[] k) {
                System.out.print("[");
                for (int i = 0; i < k.length; i++) {
                        if (i == k.length - 1)
                                System.out.print(k[i]);
                        else
                                System.out.print(k[i] + ", ");
                }
                System.out.println("]");
        }

        public static void p(Point[] k) {
                System.out.print("[");
                for (int i = 0; i < k.length; i++) {
                        if (i == k.length - 1)
                                System.out.print(k[i]);
                        else
                                System.out.print(k[i] + ", ");
                }
                System.out.println("]");
        }

        public static void p(Object[][] x) {
                String s = "[";
                for (Object[] k : x) {
                        s = s + "[";
                        for (Object c : k) {
                                if (c == null)
                                        s = s + ";,";
                                else
                                        s = s + c.toString() + ",";
                        }
                        s = s + "]\n";
                }
                s = s + "].";
                System.out.println(s);
        }

        public static void p(Object[] x) {
                String s = "[";
                for (Object c : x) {
                        if (c == null)
                                s = s + ";,";
                        else
                                s = s + c.toString() + ",";
                }
                s = s + "]";
                System.out.println(s);
        }

        public static void p(Object x) {
                System.out.println(x);
        }

        /*
        public static void p(Vector<NetworkData> p) {
                String s = "[";
                for (NetworkData c : p) {
                        s = s + c.toString() + ",";
                }
                s = s + "]";
                System.out.println(s);
        }
        */
}