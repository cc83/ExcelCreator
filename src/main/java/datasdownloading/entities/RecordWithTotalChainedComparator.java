package main.java.datasdownloading.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum RecordWithTotalChainedComparator implements Comparator<RecordWithTotal>{

        PUBLISHER_NAME {
            public int compare(RecordWithTotal o1, RecordWithTotal o2) {
                return o1.getPublisherName().compareTo(o2.getPublisherName());
            }},
        TOTAL {
            public int compare(RecordWithTotal o1, RecordWithTotal o2) {
                return o1.getTotalImp() - o2.getTotalImp();
            }},
        TOTAL_DESC {
                public int compare(RecordWithTotal o1, RecordWithTotal o2) {
                    return o2.getTotalImp() - o1.getTotalImp();
        }};

        public static Comparator<RecordWithTotal> descending(final Comparator<RecordWithTotal> other) {
            return new Comparator<RecordWithTotal>() {
                public int compare(RecordWithTotal o1, RecordWithTotal o2) {
                    return -1 * other.compare(o1, o2);
                }
            };
        }

        public static Comparator<RecordWithTotal> getComparator(final RecordWithTotalChainedComparator... multipleOptions) {
            return new Comparator<RecordWithTotal>() {
                public int compare(RecordWithTotal o1, RecordWithTotal o2) {
                    for (RecordWithTotalChainedComparator option : multipleOptions) {
                        int result = option.compare(o1, o2);
                        if (result != 0) {
                            return result;
                        }
                    }
                    return 0;
                }
            };
        }
    
    
        
        public static void main(String [] args){
            RecordWithTotal r1 = new RecordWithTotal(100);
            r1.setPublisherName("meurguez");
            
            RecordWithTotal r2 = new RecordWithTotal(200);
            r2.setPublisherName("meurguez");
            
            
            RecordWithTotal r3 = new RecordWithTotal(200);
            r3.setPublisherName("chine");
            
            List<RecordWithTotal> merg = new ArrayList<>();
            merg.add(r1);
            merg.add(r2);
            merg.add(r3);
            
            Collections.sort(merg,getComparator(PUBLISHER_NAME,TOTAL_DESC));
            
            System.out.println(merg);
        }
        
}
