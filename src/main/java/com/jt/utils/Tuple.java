package com.jt.utils;

public class Tuple {
    public static class Tuple2<A,B>{
        public Tuple2(A item1, B item2) {
            this.item1 = item1;
            this.item2 = item2;
        }

        protected A item1;
        protected B item2;

        public A getItem1() {
            return item1;
        }

        public void setItem1(A item1) {
            this.item1 = item1;
        }

        public B getItem2() {
            return item2;
        }

        public void setItem2(B item2) {
            this.item2 = item2;
        }
    }

    public static class Tuple3<A,B,C> extends Tuple2<A,B>{
        public Tuple3(A item1, B item2, C item3) {
            super(item1, item2);
            this.item3 = item3;
        }

        protected C item3;

        public C getItem3() {
            return item3;
        }

        public void setItem3(C item3) {
            this.item3 = item3;
        }
    }

    public static class Tuple4<A,B,C,D> extends Tuple3<A,B,C>{
        public Tuple4(A item1, B item2, C item3, D item4) {
            super(item1, item2, item3);
            this.item4 = item4;
        }

        protected D item4;

        public D getItem4() {
            return item4;
        }

        public void setItem4(D item4) {
            this.item4 = item4;
        }
    }
}
